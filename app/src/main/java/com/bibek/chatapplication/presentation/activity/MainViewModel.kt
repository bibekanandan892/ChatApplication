package com.bibek.chatapplication.presentation.activity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.presentation.navigation.Destination
import com.bibek.chatapplication.utils.connectivity.ConnectionState
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the main logic of the application, including navigation and connectivity state.
 *
 * @property connectivityObserver Observes the current network connection state.
 * @property repository Provides access to application data and services such as authentication details.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val repository: Repository
) : ViewModel() {

    /**
     * Holds the current connectivity state, updated in real-time.
     */
    var isConnectivityAvailable: MutableState<ConnectionState?> = mutableStateOf(null)
        private set

    /**
     * Determines the starting destination of the application based on authentication and user data availability.
     */
    val startDestination = mutableStateOf(Destination.SLASH)

    /**
     * Initializes the ViewModel by setting up the start destination and observing connectivity changes.
     */
    init {
        setupDestination()
        observeConnectivity()
    }

    /**
     * Determines the starting destination for navigation based on stored authentication and user data.
     *
     * If the user is authenticated and their username is available, navigate to the home screen.
     * Otherwise, navigate to the signup screen.
     */
    private fun setupDestination() {
        viewModelScope.launch {
            val auth = repository.getAuth().first()
            val udidName = repository.getUdidName().first()
            startDestination.value = if (!auth.isNullOrEmpty() && !udidName.isNullOrEmpty()) {
                Destination.HOME
            } else {
                Destination.SIGNUP
            }
        }
    }

    /**
     * Observes the connectivity state of the device and updates [isConnectivityAvailable] whenever the state changes.
     *
     * The connection state changes are distinct to avoid redundant updates.
     */
    private fun observeConnectivity() {
        connectivityObserver.connectionState
            .distinctUntilChanged()
            .onEach { connectionState ->
                isConnectivityAvailable.value = connectionState
            }
            .launchIn(viewModelScope)
    }
}
