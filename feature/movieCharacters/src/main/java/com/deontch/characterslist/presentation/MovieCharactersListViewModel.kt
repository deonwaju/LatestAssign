package com.deontch.characterslist.presentation

import androidx.lifecycle.viewModelScope
import com.deontch.base.contract.BaseViewModel
import com.deontch.base.contract.ViewEvent
import com.deontch.base.model.MessageState
import com.deontch.base.providers.DispatcherProvider
import com.deontch.common.cleanMessage
import com.deontch.common.toImmutableList
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.characterslist.domain.contract.MovieCharacterSideEffect
import com.deontch.characterslist.domain.contract.MovieCharacterState
import com.deontch.characterslist.domain.contract.MovieCharacterViewAction
import com.deontch.characterslist.domain.mappers.toUiModel
import com.deontch.characterslist.domain.usecase.GetMovieCharactersUseCase
import com.deontch.characterslist.domain.usecase.SearchMovieCharactersUseCase
import com.deontch.ui.extensions.collectBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MovieCharactersListViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getMovieCharactersUseCase: GetMovieCharactersUseCase,
    private val searchMovieCharactersUseCase: SearchMovieCharactersUseCase,
) : BaseViewModel<MovieCharacterState, MovieCharacterViewAction>(
    MovieCharacterState.initialState,
    dispatcherProvider
) {
    private var searchJob: Job? = null

    override fun onViewAction(viewAction: MovieCharacterViewAction) {
        when (viewAction) {
            MovieCharacterViewAction.GetCharacters -> getCharacters()
            is MovieCharacterViewAction.UpdateSearchQuery -> updateSearchQuery(viewAction.query)
            MovieCharacterViewAction.SearchCharacters -> searchCharacters()
        }
    }

    private fun getCharacters() {
        launch {
            getMovieCharactersUseCase.invoke().collectBy(
                onStart = ::onLoading,
                onEach = ::processCharacterListResponse,
                onError = ::onError
            )
        }
    }

    private fun updateSearchQuery(query: String) {
        updateState { state -> state.copy(searchQuery = query) }

        // Cancel any ongoing search
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // debounce 300ms
            if (query.isNotBlank()) {
                searchCharacters()
            } else {
                getCharacters()
            }
        }
    }

    private fun searchCharacters() {
        launch {
            searchMovieCharactersUseCase.invoke(currentState.searchQuery).collectBy(
                onStart = ::onLoading,
                onEach = ::processSearchQueryResponse,
                onError = ::onError
            )
        }
    }

    private fun onLoading() {
        updateState { state ->
            state.copy(
                isLoading = true,
                errorState = null,
            )
        }
    }

    private fun onError(error: Throwable) {
        val errorMessage = error.message.orEmpty()

        updateState { state ->
            state.copy(
                isLoading = false,
                errorState = MessageState.Inline(errorMessage.cleanMessage())
            )
        }
        dispatchViewEvent(
            ViewEvent.Effect(
                MovieCharacterSideEffect.ShowErrorMessage(
                    errorMessageState = MessageState.Inline(errorMessage)
                )
            )
        )
    }

    private fun processCharacterListResponse(characters: List<MovieCharacterDomainModel>) {
        updateState { state ->
            state.copy(
                isLoading = false,
                errorState = null,
                characterList = characters.toUiModel().toImmutableList()
            )
        }
    }

    private fun processSearchQueryResponse(characters: List<MovieCharacterDomainModel>) {
        updateState { state ->
            state.copy(
                isLoading = false,
                errorState = null,
                searchQueryResponse = characters.toUiModel().toImmutableList()
            )
        }
    }
}
