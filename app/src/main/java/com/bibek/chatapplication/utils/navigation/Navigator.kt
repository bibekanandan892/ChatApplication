package com.bibek.chatapplication.utils.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A utility class responsible for managing navigation actions within the application.
 *
 * This class allows navigation actions to be emitted as `Flow` objects, which can then be observed
 * by other components of the application, such as a navigation controller. It provides methods for
 * navigating to specific destinations and going back in the navigation stack. The class is annotated
 * with `@Singleton` to ensure that only one instance of the `Navigator` is used throughout the application
 * and `@Stable` for Compose recomposition stability.
 *
 * @constructor Injects the dependencies required for managing navigation actions.
 */
@Stable
@Singleton
class Navigator @Inject constructor() {

    // Private shared flow for emitting navigation actions.
    private val _actions = MutableSharedFlow<Action>(
        replay = 0, // No history is kept.
        extraBufferCapacity = 10 // Allow a small buffer for actions before being observed.
    )

    /**
     * A public `Flow` that emits navigation actions (such as navigation and back).
     *
     * Collect from this flow to observe navigation actions in the app.
     */
    val actions: Flow<Action> = _actions

    /**
     * Emits a navigation action to navigate to a specific destination.
     *
     * The `destination` parameter is the target route to navigate to, and the `navOptions`
     * parameter allows custom navigation options to be provided (e.g., popBackStack, launchSingleTop).
     * This method adds the navigation action to the flow of actions.
     *
     * @param destination The target route to navigate to.
     * @param navOptions The navigation options to apply during the navigation. Defaults to no options.
     */
    fun navigate(destination: DestinationRoute, navOptions: NavOptionsBuilder.() -> Unit = {}) {
        _actions.tryEmit(
            Action.Navigate(destination = destination, navOptions = navOptions)
        )
    }

    /**
     * Emits a back action to navigate back in the navigation stack.
     *
     * This method adds the back action to the flow of actions.
     */
    fun back() {
        _actions.tryEmit(Action.Back)
    }

    /**
     * A sealed class representing possible navigation actions.
     */
    sealed class Action {
        /**
         * Represents a navigation action to a specific destination.
         *
         * @property destination The destination route to navigate to.
         * @property navOptions The navigation options to apply during the navigation.
         */
        data class Navigate(
            val destination: DestinationRoute,
            val navOptions: NavOptionsBuilder.() -> Unit
        ) : Action()

        /**
         * Represents a back action to go back in the navigation stack.
         */
        data object Back : Action()
    }
}

/**
 * Type alias for the destination route.
 *
 * This represents a route to a screen or destination in the app's navigation graph.
 */
typealias DestinationRoute = String
