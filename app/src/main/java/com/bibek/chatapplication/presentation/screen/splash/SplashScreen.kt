package com.bibek.chatapplication.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.bibek.chatapplication.R
/**
 * A composable function that displays the splash screen of the application.
 *
 * The splash screen contains the app logo centered on the screen. It utilizes a `Box` layout with `fillMaxSize()`
 * modifier to occupy the full screen, and the `Image` composable is used to display the logo.
 *
 * The content description for the logo is provided using the string resource `R.string.logo` to improve accessibility.
 *
 * @Composable
 * @since 1.0
 */
@Composable
fun SplashScreen() {
    // Box layout to center the content on the screen
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Display the logo image at the center of the screen
        Image(painter = painterResource(R.drawable.ic_logo), contentDescription = stringResource(R.string.logo))
    }
}
