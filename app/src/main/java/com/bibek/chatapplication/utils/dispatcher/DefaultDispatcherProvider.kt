package com.bibek.chatapplication.utils.dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Default implementation of DispatcherProvider that provides dispatchers
 * based on Kotlin's standard Dispatchers.
 *
 * This implementation is used in production code to access the default coroutine
 * dispatchers provided by the Kotlin standard library.
 *
 * @property main Uses Dispatchers.Main for main-thread operations.
 * @property default Uses Dispatchers.Default for CPU-intensive tasks.
 * @property io Uses Dispatchers.IO for IO-bound operations.
 */
class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = Dispatchers.IO
}