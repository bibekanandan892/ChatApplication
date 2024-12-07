package com.bibek.chatapplication.presentation.navigation

/**
 * Enum class representing various destinations within the application.
 * This enum can be used to define different screens or navigation targets in a navigation system.
 * Each value corresponds to a specific route or page within the app.
 *
 * @property SLASH Represents the root or home route of the application, typically a starting point.
 * @property SIGNUP Represents the sign-up page or screen where users can register.
 * @property HOME Represents the home page or main screen of the application.
 * @property SEARCH Represents the search screen where users can search for content.
 */
enum class Destination {
    /**
     * Represents the root or home route of the application, typically the starting point of the app's navigation.
     */
    SLASH,

    /**
     * Represents the sign-up page where users can create a new account.
     */
    SIGNUP,

    /**
     * Represents the main home screen where users can navigate to different sections of the app.
     */
    HOME,

    /**
     * Represents the search screen where users can search for content or items within the app.
     */
    SEARCH,
}
