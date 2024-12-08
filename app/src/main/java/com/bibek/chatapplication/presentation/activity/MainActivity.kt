package com.bibek.chatapplication.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.ConnectivityStatus
import com.bibek.chatapplication.presentation.navigation.SetupNavGraph
import com.bibek.chatapplication.presentation.theme.Primary
import com.bibek.chatapplication.utils.getStatusBarHeight
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * The main entry point of the application. This activity is responsible for setting up the Compose UI,
 * managing navigation, observing connectivity status, and displaying error/success messages.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Injected navigator to handle navigation actions.
     */
    @Inject
    lateinit var navigator: Navigator

    /**
     * Injected toaster to handle displaying error and success messages.
     */
    @Inject
    lateinit var toaster: Toaster

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val statusBarHeight = with(LocalDensity.current) { getStatusBarHeight().toDp() }
            // Set up navigation controller for the navigation graph
            val navGraphController = rememberNavController()
            val mainViewModel: MainViewModel = hiltViewModel()
            val messageBar = rememberMessageBarState()
            val isConnectivityAvailable = mainViewModel.isConnectivityAvailable

            // Initialize navigation setup
            LaunchedEffect(key1 = true) {
                navigationSetup(navGraphController)
            }

            // Scaffold to provide consistent layout structure
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                backgroundColor = Primary
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(
                            top = statusBarHeight, // Ensure the message bar doesn't overlap the status bar
                            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                ) {
                    // Display connectivity status based on the observed state
                    isConnectivityAvailable.value?.let {
                        ConnectivityStatus(it)
                    }

                    // Observe and display error messages using a custom message bar
                    LaunchedEffect(key1 = true) {
                        toaster.errorFlow.collect {
                            messageBar.addError(it)
                        }
                    }

                    // Observe and display success messages using a custom message bar
                    LaunchedEffect(key1 = true) {
                        toaster.successFlow.collect {
                            messageBar.addSuccess(it)
                        }
                    }
                    // Wrapper for displaying messages and setting up the navigation graph
                    ContentWithMessageBar(
                        messageBarState = messageBar,
                        successContainerColor = Primary,
                        successContentColor = Color.White,
                        errorContainerColor = Color.Red,
                        errorContentColor = Color.White,
                        isEnableCopy = false,
                        lottieResource = R.raw.loading_animation,
                        position = MessageBarPosition.TOP
                    ) {
                        SetupNavGraph(
                            startDestination = mainViewModel.startDestination.value.name,
                            navController = navGraphController
                        )
                    }
                }
            }
        }
    }

    /**
     * Sets up navigation actions and observes them for changes.
     *
     * @param navGraphController The navigation controller for managing the back stack and navigation actions.
     */
    private fun navigationSetup(navGraphController: NavHostController) {
        navigator.actions.onEach { action ->
            when (action) {
                // Handle back navigation
                Navigator.Action.Back -> {
                    navGraphController.popBackStack()
                }
                // Handle navigation to a specific destination
                is Navigator.Action.Navigate -> {
                    if (navGraphController.currentDestination?.route != action.destination) {
                        navGraphController.navigate(
                            route = action.destination,
                            builder = action.navOptions
                        )
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }
}

