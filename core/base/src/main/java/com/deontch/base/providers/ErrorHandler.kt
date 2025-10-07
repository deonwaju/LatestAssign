package com.deontch.base.providers

interface ErrorHandler {
    fun getErrorMessage(error: Throwable): String
}
