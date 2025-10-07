package com.deontch.characterslist.domain.usecases

import com.deontch.common.domain.MovieCharacterRepository
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.characterslist.domain.usecase.GetMovieCharactersUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMovieCharactersUseCaseTest {

    private lateinit var movieCharacterRepository: MovieCharacterRepository
    private lateinit var getMovieCharactersUseCase: GetMovieCharactersUseCase

    @Before
    fun setUp() {
        movieCharacterRepository = mockk()
        getMovieCharactersUseCase = GetMovieCharactersUseCase(movieCharacterRepository)
    }

    @Test
    fun `invoke() should return success with character list when repository returns data`() = runTest {
        val expectedCharacters = listOf(
            MovieCharacterDomainModel(
                id = 1,
                name = "Jon Snow",
                gender = "Male",
                culture = "Northmen",
                born = "In 283 AC",
                died = "",
                titles = listOf("Lord Commander of the Night's Watch", "King in the North"),
                aliases = listOf("Lord Snow", "The White Wolf"),
                tvSeries = listOf("S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8"),
                playedBy = listOf("Kit Harington"),
                seasons = listOf(1, 2, 3, 4, 5, 6, 7, 8)
            ),
            MovieCharacterDomainModel(
                id = 2,
                name = "Daenerys Targaryen",
                gender = "Female",
                culture = "Valyrian",
                born = "In 284 AC",
                died = "In 305 AC",
                titles = listOf("Queen of the Andals and the First Men", "Mother of Dragons"),
                aliases = listOf("Dany", "Stormborn"),
                tvSeries = listOf("S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8"),
                playedBy = listOf("Emilia Clarke"),
                seasons = listOf(1, 2, 3, 4, 5, 6, 7, 8)
            )
        )
        every { movieCharacterRepository.getMovieCharacters() } returns flowOf(expectedCharacters)

        val resultFlow = getMovieCharactersUseCase()
        val actualCharacters = resultFlow.first()

        assertEquals(expectedCharacters, actualCharacters)
        
        coVerify(exactly = 1) { movieCharacterRepository.getMovieCharacters() }
    }

    @Test
    fun `invoke() should return an empty list when repository returns an empty list`() = runTest {
        val expectedEmptyList = emptyList<MovieCharacterDomainModel>()
        every { movieCharacterRepository.getMovieCharacters() } returns flowOf(expectedEmptyList)

        val resultFlow = getMovieCharactersUseCase()
        val actualCharacters = resultFlow.first()

        assertEquals(expectedEmptyList, actualCharacters)
        
        coVerify(exactly = 1) { movieCharacterRepository.getMovieCharacters() }
    }

    @Test
    fun `invoke() should propagate exception when repository throws an error`() = runTest {
        val expectedException = RuntimeException("Failed to connect to the network")
        every { movieCharacterRepository.getMovieCharacters() } returns flow { throw expectedException }

        val exception = try {
            getMovieCharactersUseCase().first()
            null
        } catch (e: Exception) {
            e
        }

        assertEquals(expectedException, exception)
        
        coVerify(exactly = 1) { movieCharacterRepository.getMovieCharacters() }
    }
}
