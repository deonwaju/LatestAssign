package com.deontch.testing

import com.deontch.base.providers.DispatcherProvider
import kotlinx.coroutines.test.TestDispatcher

class TestDispatcherProvider(
    val testDispatcher: TestDispatcher,
) : DispatcherProvider() {
    override val main = testDispatcher
    override val default = testDispatcher
    override val io = testDispatcher
}
