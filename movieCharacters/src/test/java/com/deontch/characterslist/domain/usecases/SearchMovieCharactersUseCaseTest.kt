package com.deontch.characterslist.domain.usecases

import com.deontch.common.domain.MovieCharacterRepository
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.characterslist.domain.usecase.SearchMovieCharactersUseCase
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

/**
 * Unit tests for the [SearchMovieCharactersUseCase].
 */
class SearchMovieCharactersUseCaseTest {

    private lateinit var movieCharacterRepository: MovieCharacterRepository
    private lateinit var searchMovieCharactersUseCase: SearchMovieCharactersUseCase

    @Before
    fun setUp() {
        movieCharacterRepository = mockk()
        searchMovieCharactersUseCase = SearchMovieCharactersUseCase(movieCharacterRepository)
    }

    @Test
    fun `invoke() should return list of characters when repository finds matches`() = runTest {
        // GIVEN: The repository is set up to return a list for a specific query.
        val query = "Snow"
        val expectedSearchResult = listOf(
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
            )
        )
        every { movieCharacterRepository.searchMovieCharacters(query) } returns flowOf(expectedSearchResult)

        // WHEN: The use case is invoked with the query.
        val resultFlow = searchMovieCharactersUseCase(query)
        val actualResult = resultFlow.first()

        // THEN: The result should match the data provided by the repository.
        assertEquals(expectedSearchResult, actualResult)
        
        // AND: Verify that the searchMovieCharacters method was called exactly once with the correct query.
        coVerify(exactly = 1) { movieCharacterRepository.searchMovieCharacters(query) }
    }

    @Test
    fun `invoke() should return an empty list when repository finds no matches`() = runTest {
        // GIVEN: The repository is set up to return an empty list for a query.
        val query = "nonexistent"
        val expectedEmptyList = emptyList<MovieCharacterDomainModel>()
        every { movieCharacterRepository.searchMovieCharacters(query) } returns flowOf(expectedEmptyList)

        // WHEN: The use case is invoked.
        val actualResult = searchMovieCharactersUseCase(query).first()

        // THEN: The result should be an empty list.
        assertEquals(expectedEmptyList, actualResult)
        
        // AND: Verify the repository method was called.
        coVerify(exactly = 1) { movieCharacterRepository.searchMovieCharacters(query) }
    }

    @Test
    fun `invoke() should propagate exception when repository throws an error`() = runTest {
        // GIVEN: The repository is set up to throw an exception during search.
        val query = "error"
        val expectedException = RuntimeException("Database search failed")
        every { movieCharacterRepository.searchMovieCharacters(query) } returns flow { throw expectedException }

        // WHEN & THEN: We expect an exception to be thrown when collecting the flow.
        val exception = try {
            searchMovieCharactersUseCase(query).first()
            null
        } catch (e: Exception) {
            e
        }

        // Assert that the caught exception is the one we expected.
        assertEquals(expectedException, exception)
        
        // AND: Verify the repository method was still called.
        coVerify(exactly = 1) { movieCharacterRepository.searchMovieCharacters(query) }
    }
}
