package com.deontch.characterdetail.domain.usecases

import com.deontch.common.domain.MovieCharacterRepository
import com.deontch.models.MovieCharacterDomainModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetMovieCharacterByNameUseCaseTest {

    private lateinit var movieCharacterRepository: MovieCharacterRepository
    private lateinit var getMovieCharacterByNameUseCase: GetMovieCharacterByNameUseCase

    @Before
    fun setUp() {
        movieCharacterRepository = mockk()
        getMovieCharacterByNameUseCase = GetMovieCharacterByNameUseCase(movieCharacterRepository)
    }

    @Test
    fun `invoke() should return character when repository finds a match by name`() = runTest {
        val characterName = "Jon Snow"
        val expectedCharacter = MovieCharacterDomainModel(
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
        every { movieCharacterRepository.getCharacterByName(characterName) } returns flowOf(
            expectedCharacter
        )

        val actualCharacter = getMovieCharacterByNameUseCase(characterName).first()

        Assert.assertEquals(expectedCharacter, actualCharacter)

        coVerify(exactly = 1) { movieCharacterRepository.getCharacterByName(characterName) }
    }


    @Test
    fun `invoke() should propagate exception when repository throws an error`() = runTest {
        val characterName = "ErrorCase"
        val expectedException = RuntimeException("Database lookup failed")
        every { movieCharacterRepository.getCharacterByName(characterName) } returns flow { throw expectedException }

        val exception = try {
            getMovieCharacterByNameUseCase(characterName).first()
            null
        } catch (e: Exception) {
            e
        }

        Assert.assertEquals(expectedException, exception)

        coVerify(exactly = 1) { movieCharacterRepository.getCharacterByName(characterName) }
    }
}
