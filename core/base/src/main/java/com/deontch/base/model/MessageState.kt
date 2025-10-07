package com.deontch.base.model

sealed class MessageState {
    data class Inline(val message: String, val action: (() -> Unit)? = null, val isRetryAble: Boolean = false) : MessageState()

    data class Toast(val message: String) : MessageState()
}
