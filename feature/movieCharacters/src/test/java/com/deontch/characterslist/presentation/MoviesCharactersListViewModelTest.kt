package com.deontch.characterslist.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.deontch.base.contract.ViewEvent
import com.deontch.base.model.MessageState
import com.deontch.base.providers.DispatcherProvider
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.characterslist.domain.contract.MovieCharacterSideEffect
import com.deontch.characterslist.domain.contract.MovieCharacterViewAction
import com.deontch.characterslist.domain.mappers.toUiModel
import com.deontch.characterslist.domain.usecase.GetMovieCharactersUseCase
import com.deontch.characterslist.domain.usecase.SearchMovieCharactersUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesCharactersListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MovieCharactersListViewModel

    private val getMovieCharactersUseCase: GetMovieCharactersUseCase = mockk()
    private val searchMovieCharactersUseCase: SearchMovieCharactersUseCase = mockk()
    private val dispatcherProvider: DispatcherProvider = mockk {
        every { io } returns testDispatcher
        every { main } returns testDispatcher
        every { default } returns testDispatcher
    }

    private val mockCharacters = listOf(
        MovieCharacterDomainModel(
            id = 0,
            name = "Jon Snow",
            gender = "Male",
            culture = "Northmen",
            born = "In 283 AC",
            died = "",
            titles = listOf("King in the North"),
            aliases = listOf("The White Wolf"),
            tvSeries = listOf("Season 1"),
            playedBy = listOf("Kit Harington")
        )
    )

    @Before
    fun setup() {
        viewModel = MovieCharactersListViewModel(dispatcherProvider, getMovieCharactersUseCase, searchMovieCharactersUseCase)
    }
    @Before
    fun setUp() {
        // Sets the main coroutine dispatcher to our test dispatcher.
        Dispatchers.setMain(testDispatcher)
        // Mocks the dispatcher provider to return our test dispatcher for all contexts.
        every { dispatcherProvider.io } returns testDispatcher
        every { dispatcherProvider.main } returns testDispatcher
        every { dispatcherProvider.default } returns testDispatcher

        // Initialize the ViewModel with mocked dependencies.
        viewModel = MovieCharactersListViewModel(dispatcherProvider, getMovieCharactersUseCase, searchMovieCharactersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createMockDomainModel(name: String = "Jon Snow"): MovieCharacterDomainModel {
        return MovieCharacterDomainModel(
            name = name,
            gender = "Male",
            culture = "Northmen",
            seasons = listOf(1, 2, 3, 4, 5, 6, 7, 8),
            born = "In 289 AC",
            died = "",
        )
    }

    @Test
    fun `Given GetCharacters action WHEN successful THEN updates state with character list`() = runTest {
        coEvery { getMovieCharactersUseCase() } returns flow { emit(mockCharacters) }

        viewModel.onViewAction(MovieCharacterViewAction.GetCharacters)

        viewModel.state.test {
            awaitItem()

            val second = awaitItem()
            assertEquals(false, second.isLoading)
            assertEquals(mockCharacters.first().name, second.characterList.first().name)
        }
    }

    @Test
    fun `onViewAction GetCharacters WHEN repository returns data THEN updates state with character list`() = runTest {
        val characters = listOf(createMockDomainModel())
        val expectedUiModels = characters.toUiModel()
        coEvery { getMovieCharactersUseCase() } returns flowOf(characters)

        viewModel.onViewAction(MovieCharacterViewAction.GetCharacters)

        val state = viewModel.state.value
        assertTrue("isLoading should be true initially", state.isLoading)

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.state.value
        assertFalse("isLoading should be false after completion", finalState.isLoading)
        assertNull("errorState should be null on success", finalState.errorState)
        assertEquals(expectedUiModels, finalState.characterList.toList())
    }

    @Test
    fun `onViewAction GetCharacters WHEN repository throws error THEN updates state with error`() = runTest {
        val errorMessage = "No Internet Connection"
        coEvery { getMovieCharactersUseCase() } returns flow { throw Exception(errorMessage) }

        viewModel.onViewAction(MovieCharacterViewAction.GetCharacters)

        viewModel.events.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val finalState = viewModel.state.value
            assertFalse("isLoading should be false after error", finalState.isLoading)
            assertTrue("errorState should be an Inline MessageState", finalState.errorState is MessageState.Inline)
            assertEquals(errorMessage, (finalState.errorState as MessageState.Inline).message)

            val emittedEvent = awaitItem()
            assertTrue(emittedEvent is ViewEvent.Effect)
            val effect = (emittedEvent as ViewEvent.Effect).effect
            assertTrue(effect is MovieCharacterSideEffect.ShowErrorMessage)
            assertEquals(errorMessage, (effect as MovieCharacterSideEffect.ShowErrorMessage).errorMessageState.let{
                (it as MessageState.Inline).message
            } )

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onViewAction UpdateSearchQuery WHEN query is updated THEN debounces and triggers search`() = runTest {
        val query = "Arya"
        val searchResult = listOf(createMockDomainModel("Arya Stark"))
        coEvery { searchMovieCharactersUseCase(query) } returns flowOf(searchResult)

        viewModel.onViewAction(MovieCharacterViewAction.UpdateSearchQuery(query))

        assertEquals(query, viewModel.state.value.searchQuery)

        advanceTimeBy(299)
        coVerify(exactly = 0) { searchMovieCharactersUseCase(any()) }

        advanceTimeBy(1)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { searchMovieCharactersUseCase(query) }

        val finalState = viewModel.state.value
        assertFalse(finalState.isLoading)
        assertEquals(searchResult.toUiModel(), finalState.searchQueryResponse.toList())
    }

    @Test
    fun `onViewAction UpdateSearchQuery WHEN query becomes blank THEN fetches all characters`() = runTest {
        val allCharacters = listOf(createMockDomainModel("Jon Snow"), createMockDomainModel("Sansa Stark"))
        coEvery { getMovieCharactersUseCase() } returns flowOf(allCharacters)

        coEvery { searchMovieCharactersUseCase("Sansa") } returns flowOf(emptyList())

        viewModel.onViewAction(MovieCharacterViewAction.UpdateSearchQuery("Sansa"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onViewAction(MovieCharacterViewAction.UpdateSearchQuery(""))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { getMovieCharactersUseCase() }
        coVerify(exactly = 1) { searchMovieCharactersUseCase("Sansa") }

        val finalState = viewModel.state.value
        assertEquals(allCharacters.toUiModel(), finalState.characterList.toList())
    }

    @Test
    fun `onViewAction Retry WHEN last action failed THEN retries the last action successfully`() = runTest {
        val errorMessage = "Network Error"
        // 1. First call fails
        coEvery { getMovieCharactersUseCase() } returns flow { throw Exception(errorMessage) }

        // Trigger the initial action that will fail
        viewModel.onViewAction(MovieCharacterViewAction.GetCharacters)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify the error state
        var state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.errorState is MessageState.Inline)
        assertEquals(errorMessage, (state.errorState as MessageState.Inline).message)

        // 2. Setup the second call to succeed
        coEvery { getMovieCharactersUseCase() } returns flowOf(mockCharacters)

        // 3. Trigger the Retry action
        viewModel.onViewAction(MovieCharacterViewAction.Retry)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all coroutines complete

        // 4. Verify the final state is successful
        state = viewModel.state.value
        assertFalse("isLoading should be false after successful retry", state.isLoading)
        assertNull("Error state should be cleared on successful retry", state.errorState)
        assertEquals(mockCharacters.toUiModel(), state.characterList.toList())

        // Verify that the use case was called twice (initial attempt + retry)
        coVerify(exactly = 2) { getMovieCharactersUseCase() }
    }
}
