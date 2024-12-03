package com.bibek.chatapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bibek.chatapplication.presentation.screen.home.HomeEvent
import com.bibek.chatapplication.presentation.screen.home.HomeScreen
import com.bibek.chatapplication.presentation.screen.home.HomeViewmodel
import com.bibek.chatapplication.presentation.screen.search.SearchEvent
import com.bibek.chatapplication.presentation.screen.search.SearchScreen
import com.bibek.chatapplication.presentation.screen.search.SearchViewModel
import com.bibek.chatapplication.presentation.screen.signup.SignupScreen
import com.bibek.chatapplication.presentation.screen.signup.SignupViewmodel
import com.bibek.chatapplication.utils.GENDER_KEY
import com.bibek.chatapplication.utils.NAME_KEY
import com.bibek.chatapplication.utils.PREFER_GENDER_KEY

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = Destination.SIGNUP.name) {
            val signupViewmodel: SignupViewmodel = hiltViewModel()
            val uiState by signupViewmodel.uiState.collectAsState()
            SignupScreen(uiState = uiState, signupViewmodel::onEvent)
        }
        composable(
            route = Destination.HOME.name + "/{$NAME_KEY}/{$GENDER_KEY}",
            arguments = listOf(
                navArgument(NAME_KEY) { type = NavType.StringType },
                navArgument(GENDER_KEY) { type = NavType.StringType }
            ),
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(NAME_KEY).toString()
            val gender = backStackEntry.arguments?.getString(GENDER_KEY).toString()
            val homeViewmodel: HomeViewmodel = hiltViewModel()
            val uiState by homeViewmodel.uiState.collectAsState()
            LaunchedEffect(key1 = true) {
                homeViewmodel.onEvent(HomeEvent.GetUserDetails(name = name, gender = gender))
            }
            HomeScreen(uiState = uiState, onEvent = homeViewmodel::onEvent)
        }
        composable(
            route = Destination.SEARCH.name + "/{$NAME_KEY}/{$GENDER_KEY}/{$PREFER_GENDER_KEY}",
            arguments = listOf(
                navArgument(NAME_KEY) { type = NavType.StringType },
                navArgument(GENDER_KEY) { type = NavType.StringType },
                navArgument(PREFER_GENDER_KEY) { type = NavType.StringType }
            ),
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(NAME_KEY).toString()
            val gender = backStackEntry.arguments?.getString(GENDER_KEY).toString()
            val preferGender = backStackEntry.arguments?.getString(PREFER_GENDER_KEY).toString()
            val searchViewModel: SearchViewModel = hiltViewModel()
            val uiState by searchViewModel.uiState.collectAsState()
            LaunchedEffect(key1 = true) {
                searchViewModel.onEvent(
                    SearchEvent.GetUserDetails(
                        name = name,
                        gender = gender,
                        preferGender = preferGender
                    )
                )
            }
            SearchScreen(uiState = uiState, onEvent = searchViewModel::onEvent)
        }
    }
}