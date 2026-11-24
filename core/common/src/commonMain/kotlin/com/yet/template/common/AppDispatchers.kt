package com.yet.template.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

/** Injected instead of `Dispatchers.*` so tests can replace them. */
class AppDispatchers(
    val default: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = ioDispatcher,
    val main: MainCoroutineDispatcher = Dispatchers.Main,
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined,
)

/** `Dispatchers.IO` where the platform has it. */
expect val ioDispatcher: CoroutineDispatcher
