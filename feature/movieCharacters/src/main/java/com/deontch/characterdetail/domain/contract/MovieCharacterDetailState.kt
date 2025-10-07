package com.deontch.characterdetail.domain.contract

import com.deontch.base.contract.BaseState
import com.deontch.base.model.MessageState
import com.deontch.characterdetail.presentation.model.MovieCharacterDetailUiModel

internal data class MovieCharacterDetailState(
    override val isLoading: Boolean,
    override val errorState: MessageState?,
    val characterDetail: MovieCharacterDetailUiModel?,
): BaseState {
    companion object {
        val initialState = MovieCharacterDetailState(
            isLoading = true,
            characterDetail = null,
            errorState = null,
        )
    }
}
