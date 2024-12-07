package com.bibek.chatapplication.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.ConnectivityStatus
import com.bibek.chatapplication.presentation.navigation.SetupNavGraph
import com.bibek.chatapplication.presentation.theme.Primary
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var toaster: Toaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navGraphController = rememberNavController()
            val mainViewModel: MainViewModel = hiltViewModel()
            val messageBar = rememberMessageBarState()

            val isConnectivityAvailable = mainViewModel.isConnectivityAvailable

            LaunchedEffect(key1 = true) {
                navigationSetup(navGraphController)
            }

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    isConnectivityAvailable.value?.let {
                        ConnectivityStatus(it)
                    }
                    // Observes error messages and displays them using a custom message bar UI component
                    LaunchedEffect(key1 = true) {
                        toaster.errorFlow.collect {
                            messageBar.addError(it)
                        }
                    }

                    // Observes success messages and displays them using a custom message bar UI component
                    LaunchedEffect(key1 = true) {
                        toaster.successFlow.collect {
                            messageBar.addSuccess(it)
                        }
                    }
                    ContentWithMessageBar(
                        messageBarState = messageBar,
                        successContainerColor = Primary,
                        successContentColor = Color.White,
                        errorContainerColor = Color.Red,
                        errorContentColor = Color.White,
                        isEnableCopy = false,
                        lottieResource = R.raw.kotak_loading, position = MessageBarPosition.TOP
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

    private fun navigationSetup(navGraphController: NavHostController) {
        navigator.actions.onEach { action ->
            when (action) {
                Navigator.Action.Back -> {
                    navGraphController.popBackStack()
                }

                is Navigator.Action.Navigate -> {
                    if (navGraphController.currentDestination?.route != action.destination) {
                        navGraphController.navigate(
                            route = action.destination, builder = action.navOptions
                        )
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }
}
