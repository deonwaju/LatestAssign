package com.deontch.base.contract

sealed class ViewEvent {
    data class Navigate(val target: NavigationTarget) : ViewEvent()
    data class Effect(val effect: SideEffect) : ViewEvent()
}
