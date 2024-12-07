package com.bibek.chatapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bibek.chatapplication.presentation.screen.home.HomeScreen
import com.bibek.chatapplication.presentation.screen.home.HomeViewmodel
import com.bibek.chatapplication.presentation.screen.search.ChatScreen
import com.bibek.chatapplication.presentation.screen.search.SearchViewModel
import com.bibek.chatapplication.presentation.screen.signup.SignupScreen
import com.bibek.chatapplication.presentation.screen.signup.SignupViewmodel
import com.bibek.chatapplication.presentation.screen.splash.SplashScreen

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = Destination.SLASH.name) {
            SplashScreen()
        }
        composable(route = Destination.SIGNUP.name) {
            val signupViewmodel: SignupViewmodel = hiltViewModel()
            val uiState by signupViewmodel.uiState.collectAsState()
            SignupScreen(uiState = uiState, signupViewmodel::onEvent)
        }
        composable(
            route = Destination.HOME.name,
        ) {
            val homeViewmodel: HomeViewmodel = hiltViewModel()
            val uiState by homeViewmodel.uiState.collectAsState()
            HomeScreen(uiState = uiState, onEvent = homeViewmodel::onEvent)
        }
        composable(
            route = Destination.SEARCH.name
        ) { backStackEntry ->
            val searchViewModel: SearchViewModel = hiltViewModel()
            val uiState by searchViewModel.uiState.collectAsState()
            ChatScreen(uiState = uiState, onEvent = searchViewModel::onEvent)
        }
    }
}