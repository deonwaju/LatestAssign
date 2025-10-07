package com.deontch.characterslist.domain.contract

import androidx.compose.runtime.Immutable
import com.deontch.base.contract.SideEffect
import com.deontch.base.model.MessageState

@Immutable
sealed interface MovieCharacterSideEffect: SideEffect {
    /**
     * A side effect to show an error message to the user (e.g., in a SnackBar).
     * @param errorMessageState The state containing the message to be displayed.
     */
    data class ShowErrorMessage(val errorMessageState: MessageState) : MovieCharacterSideEffect
}
