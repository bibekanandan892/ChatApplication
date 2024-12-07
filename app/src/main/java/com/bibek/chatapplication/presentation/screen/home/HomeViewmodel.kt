package com.bibek.chatapplication.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.presentation.navigation.Destination
import com.bibek.chatapplication.utils.logger.Logger
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val navigator: Navigator,
    private val toaster: Toaster
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState get() = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<HomeEvent>(extraBufferCapacity = 10)
    val eventFlow get() = _eventFlow.asSharedFlow()

    init {
        collectEvents()
        Logger.log("Init HomeViewModel")
    }

    fun onEvent(event: HomeEvent) {
        _eventFlow.tryEmit(event)
    }

    fun collectEvents() {
        eventFlow.onEach { event ->
            when (event) {
                is HomeEvent.GetUserDetails -> {
                    _uiState.update { uiState ->
                        uiState.copy(
                            name = event.name,
                            gender = event.gender
                        )
                    }
                }
                is HomeEvent.OnGenderSelect -> _uiState.update { uiState -> uiState.copy(preferGender = event.gender) }
                HomeEvent.NavigateToSearch -> {
                    if (uiState.value.preferGender == null) {
                        toaster.error("يرجى اختيار الجنس")
                    } else {
                        uiState.value.apply {
                            navigator.navigate(destination = Destination.SEARCH.name)
                        }
                    }
                }
                HomeEvent.OnConversationsClick -> toaster.error("الميزة غير متوفرة")
                HomeEvent.OnMyAccountClick -> toaster.error("الميزة غير متوفرة")
            }
        }.launchIn(viewModelScope)
    }
}