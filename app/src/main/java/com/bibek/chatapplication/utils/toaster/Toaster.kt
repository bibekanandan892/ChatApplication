package com.bibek.chatapplication.utils.toaster

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A utility class for displaying success and error messages using a shared flow.
 *
 * This class provides mechanisms to emit success and error messages that can be observed
 * and displayed in the UI using a `Flow`. It is annotated with `@Singleton` to ensure
 * a single instance is used throughout the application, and `@Stable` to indicate it is
 * stable for use in Compose recompositions.
 *
 * @constructor Creates an instance of [Toaster]. Dependency injection is supported via the `@Inject` annotation.
 */
@Stable
@Singleton
class Toaster @Inject constructor() {

    // Private shared flow for success messages.
    private val _successFlow = MutableSharedFlow<String>()

    /**
     * A public `Flow` for observing success messages.
     *
     * Use this flow to collect success messages emitted via the [success] method.
     */
    val successFlow: Flow<String> = _successFlow

    // Private shared flow for error messages.
    private val _errorFlow = MutableSharedFlow<String>()

    /**
     * A public `Flow` for observing error messages.
     *
     * Use this flow to collect error messages emitted via the [error] method.
     */
    val errorFlow: Flow<String> = _errorFlow

    /**
     * Emits a success message to the [successFlow].
     *
     * This method is designed to be used in a coroutine context.
     *
     * @param message The success message to emit.
     */
    suspend fun success(message: String) {
        _successFlow.emit(message)
    }

    /**
     * Emits an error message to the [errorFlow].
     *
     * This method is designed to be used in a coroutine context.
     *
     * @param message The error message to emit.
     */
    suspend fun error(message: String) {
        _errorFlow.emit(message)
    }
}
