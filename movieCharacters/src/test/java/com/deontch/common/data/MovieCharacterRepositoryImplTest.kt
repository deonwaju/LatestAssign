package com.deontch.common.data

import com.deontch.base.providers.DispatcherProvider
import com.deontch.common.Constants.NO_INTERNET
import com.deontch.localdata.dao.MovieCharactersDao
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.network.model.MovieCharacterResponse
import com.deontch.network.provider.NetworkStateProvider
import com.deontch.network.service.MovieCharacterApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class MovieCharacterRepositoryImplTest {

    private lateinit var repository: MovieCharacterRepositoryImpl
    private val movieCharacterApi: MovieCharacterApi = mockk()
    private val movieCharactersDao: MovieCharactersDao = mockk()
    private val dispatcherProvider: DispatcherProvider = mockk()
    private val networkStateProvider: NetworkStateProvider = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        every { dispatcherProvider.io } returns testDispatcher
        Dispatchers.setMain(testDispatcher)
        coEvery { dispatcherProvider.io } returns testDispatcher

        repository = MovieCharacterRepositoryImpl(
            movieCharacterApi = movieCharacterApi,
            movieCharactersDao = movieCharactersDao,
            dispatcherProvider = dispatcherProvider,
            networkStateProvider = networkStateProvider
        )
    }

    private fun createMockMovieCharacterResponse(name: String = "Jon Snow"): MovieCharacterResponse {
        return MovieCharacterResponse(
            name = name,
            gender = "Male",
            culture = "Northmen",
            born = "In 283 AC",
            died = "",
            titles = arrayListOf("Lord Commander of the Night's Watch", "King in the North"),
            aliases = arrayListOf("Lord Snow", "The White Wolf"),
            tvSeries = arrayListOf("Season 1", "Season 2", "Season 3", "Season 4", "Season 5", "Season 6", "Season 7", "Season 8"),
            playedBy = arrayListOf("Kit Harington"),
        )
    }

    private fun createMockDomainModel(name: String = "Jon Snow"): MovieCharacterDomainModel {
        return MovieCharacterDomainModel(
            name = name,
            gender = "Male",
            culture = "Northmen",
            born = "In 283 AC",
            died = "",
            titles = listOf("Lord Commander of the Night's Watch", "King in the North"),
            aliases = listOf("Lord Snow", "The White Wolf"),
            tvSeries = listOf("Season 1", "Season 2", "Season 3", "Season 4", "Season 5", "Season 6", "Season 7", "Season 8"),
            playedBy = listOf("Kit Harington"),
            seasons = listOf(1, 2, 3, 4, 5, 6, 7, 8),
        )
    }

    @Test
    fun `getMovieCharacters WHEN offline SHOULD fetch from local DB`() = runTest {
        val cachedCharacters = listOf(createMockDomainModel("Daenerys Targaryen"))
        every { networkStateProvider.isConnected } returns false
        coEvery { movieCharactersDao.getAllCharacters() } returns cachedCharacters

        val result = repository.getMovieCharacters().first()

        assertEquals(cachedCharacters, result)
        coVerify(exactly = 0) { movieCharacterApi.getCharacters() }
    }

    @Test
    fun `getMovieCharacters WHEN offline and DB is empty SHOULD throw 'NO_INTERNET' exception`() = runTest {
        every { networkStateProvider.isConnected } returns false
        coEvery { movieCharactersDao.getAllCharacters() } returns emptyList()

        val exception = assertFailsWith<Exception> {
            repository.getMovieCharacters().first()
        }

        assertEquals(NO_INTERNET, exception.message)
    }

    @Test
    fun `searchMovieCharacters SHOULD return search results from local DB`() = runTest {
        val query = "Snow"
        val searchResults = listOf(createMockDomainModel("Jon Snow"))
        coEvery { movieCharactersDao.searchCharacters(query) } returns searchResults

        val result = repository.searchMovieCharacters(query).first()

        assertEquals(searchResults, result)
        coVerify { movieCharactersDao.searchCharacters(query) }
    }


    @Test
    fun `getCharacterByName SHOULD return specific character from local DB`() = runTest {
        val characterName = "Tyrion Lannister"
        val expectedCharacter = createMockDomainModel(characterName)
        coEvery { movieCharactersDao.getCharacterByName(characterName) } returns expectedCharacter

        val result = repository.getCharacterByName(characterName).first()

        assertEquals(expectedCharacter, result)
        coVerify { movieCharactersDao.getCharacterByName(characterName) }
    }

    @Test
    fun `Given getMovieCharacters WHEN network is available THEN fetches from API and saves to DB`() = runTest {
        val mockApiResponse = listOf(createMockMovieCharacterResponse())
        val expectedDomainModel = listOf(createMockDomainModel())

        every { networkStateProvider.isConnected } returns true
        coEvery { movieCharacterApi.getCharacters() } returns mockApiResponse


        coEvery { movieCharactersDao.deleteAllCharacters() } returns 1
        coEvery { movieCharactersDao.insertCharacters(any()) } returns Unit

        val result = repository.getMovieCharacters().first()

        assertEquals(expectedDomainModel, result)
        coVerifySequence {
            networkStateProvider.isConnected
            movieCharacterApi.getCharacters()
            movieCharactersDao.deleteAllCharacters()
            movieCharactersDao.insertCharacters(expectedDomainModel)
        }
    }

    @Test
    fun `when offline, should fetch from DB`() = runTest {
        val cachedList = listOf(createMockDomainModel())
        every { networkStateProvider.isConnected } returns false
        coEvery { movieCharactersDao.getAllCharacters() } returns cachedList

        val result = repository.getMovieCharacters().first()

        assertEquals(cachedList, result)
        coVerify(exactly = 0) { movieCharacterApi.getCharacters() }
    }

    @Test
    fun `when offline and DB empty, should throw NO_INTERNET`() = runTest {
        // Given
        every { networkStateProvider.isConnected } returns false
        coEvery { movieCharactersDao.getAllCharacters() } returns emptyList()

        // When & Then
        val exception = assertFailsWith<Exception> {
            repository.getMovieCharacters().first()
        }
        assertEquals(NO_INTERNET, exception.message)
    }

    @Test
    fun `should return search results from DB`() = runTest {
        val query = "Luke"
        val searchResults = listOf(createMockDomainModel())
        every { networkStateProvider.isConnected } returns true
        coEvery { movieCharactersDao.searchCharacters(query) } returns searchResults

        val result = repository.searchMovieCharacters(query).first()

        assertEquals(searchResults, result)
        coVerify { movieCharactersDao.searchCharacters(query) }
    }

    @Test
    fun `when offline, search still queries local DB`() = runTest {
        val query = "Leia"
        val searchResults = listOf(
            MovieCharacterDomainModel(
                id = 1,
                name = "Hero",
                gender = "male",
                "duo",
                "1933",
                "1933",
                listOf("mr"),
                listOf("get"),
                listOf("i"),
                listOf("john doe"),
                listOf(1, 2),
            )
        )
        every { networkStateProvider.isConnected } returns false
        coEvery { movieCharactersDao.searchCharacters(query) } returns searchResults

        val result = repository.searchMovieCharacters(query).first()

        assertEquals(searchResults, result)
    }

    @Test
    fun `should return character by name from DB`() = runTest {
        // Given
        val name = "Han Solo"
        val expectedCharacter = MovieCharacterDomainModel(
            id = 1,
            name = "Han Solo",
            gender = "male",
            "duo",
            "1933",
            "1933",
            listOf("mr"),
            listOf("get"),
            listOf("i"),
            listOf("john doe"),
            listOf(1, 2),
        )

        coEvery { movieCharactersDao.getCharacterByName(name) } returns expectedCharacter

        val result = repository.getCharacterByName(name).first()

        assertEquals(expectedCharacter, result)
        coVerify { movieCharactersDao.getCharacterByName(name) }
    }
}
