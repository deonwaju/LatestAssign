package com.deontch.characterdetail.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.deontch.base.contract.ViewEvent
import com.deontch.base.providers.DispatcherProvider
import com.deontch.characterdetail.domain.contract.MovieCharacterDetailSideEffect
import com.deontch.characterdetail.domain.contract.MovieCharacterDetailViewAction
import com.deontch.characterdetail.domain.mapper.toDetailUiModel
import com.deontch.characterdetail.domain.usecases.GetMovieCharacterByNameUseCase
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.common.domain.MovieCharacterRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieCharacterDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val getMovieCharacterByNameUseCase: GetMovieCharacterByNameUseCase = mockk()
    private val dispatcherProvider: DispatcherProvider = mockk()

    private lateinit var viewModel: MovieCharacterDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { dispatcherProvider.io } returns testDispatcher
        every { dispatcherProvider.main } returns testDispatcher
        every { dispatcherProvider.default } returns testDispatcher

        viewModel = MovieCharacterDetailViewModel(getMovieCharacterByNameUseCase, dispatcherProvider)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to the original one to avoid test interference.
        Dispatchers.resetMain()
    }

    // Helper function to create consistent mock data for tests.
    private fun createMockDomainModel(name: String): MovieCharacterDomainModel {
        return MovieCharacterDomainModel(
            name = name,
            gender = "Female",
            culture = "Braavosi",
            born = "In 289 AC",
            died = "",
            titles = listOf("No One"),
            aliases = listOf("Arya Horseface"),
            tvSeries = listOf("Season 1", "Season 8"),
            playedBy = listOf("Maisie Williams"),
            seasons = listOf(1, 8)
        )
    }

    @Test
    fun `onViewAction GetMovieCharacterDetail WHEN repository returns data THEN updates state with character detail`() = runTest {
        val characterName = "Arya Stark"
        val mockCharacter = createMockDomainModel(characterName)
        val expectedUiModel = mockCharacter.toDetailUiModel()
        coEvery { getMovieCharacterByNameUseCase(characterName) } returns flowOf(mockCharacter)

        viewModel.state.test {
            viewModel.onViewAction(MovieCharacterDetailViewAction.GetMovieCharacterDetail(characterName))

            var currentState = awaitItem()
            assertTrue(currentState.isLoading)

            currentState = awaitItem()
            assertFalse(currentState.isLoading)
            assertEquals(expectedUiModel, currentState.characterDetail)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
