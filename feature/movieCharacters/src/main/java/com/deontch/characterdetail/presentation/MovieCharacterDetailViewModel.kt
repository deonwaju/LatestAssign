package com.deontch.characterdetail.presentation

import com.deontch.base.contract.BaseViewModel
import com.deontch.base.contract.ViewEvent
import com.deontch.base.model.MessageState
import com.deontch.base.providers.DispatcherProvider
import com.deontch.common.cleanMessage
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.common.domain.MovieCharacterRepository
import com.deontch.characterdetail.domain.contract.MovieCharacterDetailSideEffect
import com.deontch.characterdetail.domain.contract.MovieCharacterDetailState
import com.deontch.characterdetail.domain.contract.MovieCharacterDetailViewAction
import com.deontch.characterdetail.domain.mapper.toDetailUiModel
import com.deontch.characterdetail.domain.usecases.GetMovieCharacterByNameUseCase
import com.deontch.ui.extensions.collectBy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MovieCharacterDetailViewModel @Inject constructor(
    private val getMovieCharacterByNameUseCase: GetMovieCharacterByNameUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<MovieCharacterDetailState, MovieCharacterDetailViewAction>(
    MovieCharacterDetailState.initialState, dispatcherProvider
) {
    override fun onViewAction(viewAction: MovieCharacterDetailViewAction) {
        when (viewAction) {
            is MovieCharacterDetailViewAction.GetMovieCharacterDetail -> getMovieCharacterDetail(viewAction.id)
        }
    }

    private fun getMovieCharacterDetail(name: String) {
        launch {
            getMovieCharacterByNameUseCase(name).collectBy(
                onStart = ::onLoading,
                onEach = ::processMovieCharacterDetail,
                onError = ::onError
            )
        }
    }

    private fun processMovieCharacterDetail(characterDetail: MovieCharacterDomainModel) {
        updateState { state ->
            state.copy(
                isLoading = false,
                characterDetail = characterDetail.toDetailUiModel()
            )
        }
    }

    private fun onLoading() {
        updateState { state ->
            state.copy(
                isLoading = true,
            )
        }
    }

    private fun onError(error: Throwable) {
        updateState { state ->
            state.copy(
                isLoading = false,
            )
        }

        val errorMessage = error.message?.cleanMessage().orEmpty()

        dispatchViewEvent(
            ViewEvent.Effect(
                MovieCharacterDetailSideEffect.ShowErrorMessage(
                    errorMessageState = MessageState.Inline(errorMessage)
                )
            )
        )
    }
}
