package com.deontch.characterslist.domain.contract

import androidx.compose.runtime.Immutable
import com.deontch.base.contract.BaseState
import com.deontch.base.model.MessageState
import com.deontch.common.ImmutableList
import com.deontch.common.emptyImmutableList
import com.deontch.characterslist.presentation.model.MovieCharacterUiModel

@Immutable
data class MovieCharacterState(
    override val isLoading: Boolean,
    override val errorState: MessageState?,
    val searchQuery: String,
    val characterList: ImmutableList<MovieCharacterUiModel>,
    val searchQueryResponse: ImmutableList<MovieCharacterUiModel>,
): BaseState {
    companion object {
        val initialState = MovieCharacterState(
            isLoading = true,
            searchQuery = "",
            characterList = emptyImmutableList(),
            searchQueryResponse = emptyImmutableList(),
            errorState = null
        )
    }
}
