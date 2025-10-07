package com.deontch.characterslist.domain.contract

import androidx.compose.runtime.Immutable
import com.deontch.base.contract.BaseViewAction

@Immutable
sealed interface MovieCharacterViewAction: BaseViewAction {
    /**
     * Action to fetch the initial list of movie characters.
     */
    data object GetCharacters : MovieCharacterViewAction

    /**
     * Action to trigger a search based on the current searchQuery in the state.
     */
    data object SearchCharacters : MovieCharacterViewAction

    /**
     * Action to update the search query text in the state.
     * @param query The new search text from the UI.
     */
    data class UpdateSearchQuery(val query: String) : MovieCharacterViewAction
}
