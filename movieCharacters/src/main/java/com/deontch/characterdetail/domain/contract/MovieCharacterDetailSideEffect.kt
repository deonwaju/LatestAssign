package com.deontch.characterdetail.domain.contract

import com.deontch.base.contract.SideEffect
import com.deontch.base.model.MessageState

interface MovieCharacterDetailSideEffect : SideEffect {
    /**
     * Represents an effect to show an error message in the UI.
     *
     * @property errorMessageState The state of the message to be displayed.
     */
    data class ShowErrorMessage(val errorMessageState: MessageState.Inline) : MovieCharacterDetailSideEffect
}
