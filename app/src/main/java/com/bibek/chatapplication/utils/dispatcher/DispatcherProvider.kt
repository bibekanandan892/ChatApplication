package com.bibek.chatapplication.utils.dispatcher

import kotlinx.coroutines.CoroutineDispatcher


/**
 * Interface that provides CoroutineDispatcher instances for different coroutine contexts.
 * This abstraction helps in decoupling the coroutine dispatchers, making the code more
 * testable and flexible by allowing custom dispatcher implementations.
 *
 * @property main Dispatcher for main-thread operations, typically for UI interactions.
 * @property default Dispatcher optimized for CPU-intensive tasks.
 * @property io Dispatcher optimized for IO-bound operations like database or network calls.
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
}