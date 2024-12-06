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

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val repository: Repository
) :
    ViewModel() {
    var isConnectivityAvailable: MutableState<ConnectionState?> = mutableStateOf(null)
        private set
    val startDestination = mutableStateOf(Destination.SLASH)

    init {
        setupDestination()
        observeConnectivity()
    }

    private fun setupDestination() {
        viewModelScope.launch {
            if (!repository.getAuth().first().isNullOrEmpty() && !repository.getUdidName().first()
                    .isNullOrEmpty()
            ) {
                startDestination.value = Destination.HOME
            } else {
                startDestination.value = Destination.SIGNUP
            }
        }
    }

    private fun observeConnectivity() {
        connectivityObserver.connectionState
            .distinctUntilChanged()
            .onEach {
                isConnectivityAvailable.value = it
            }
            .launchIn(viewModelScope)
    }
}