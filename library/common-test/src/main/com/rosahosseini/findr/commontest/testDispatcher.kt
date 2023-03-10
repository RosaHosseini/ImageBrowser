package com.rosahosseini.findr.commontest

import kotlinx.coroutines.ExperimentalCoroutinesApi
import rosa.pay.core.AppDispatchers
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

private val testScheduler = TestCoroutineScheduler()

@OptIn(ExperimentalCoroutinesApi::class)
val testDispatchers = AppDispatchers(
    main = UnconfinedTestDispatcher(testScheduler, "main-test-dispatcher"),
    io = UnconfinedTestDispatcher(testScheduler, "iO-test-dispatcher"),
    default = UnconfinedTestDispatcher(testScheduler, "default-test-dispatcher")
)