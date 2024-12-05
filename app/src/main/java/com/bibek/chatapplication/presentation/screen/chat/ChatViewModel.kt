package com.bibek.chatapplication.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.presentation.navigation.Destination
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
class ChatViewModel @Inject constructor(
    private val toaster: Toaster,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatState())
    val uiState get() = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<ChatEvent>(extraBufferCapacity = 10)
    val eventFlow get() = _eventFlow.asSharedFlow()

    init {
        collectEvents()
    }

    fun onEvent(event: ChatEvent) {
        _eventFlow.tryEmit(event)
    }

    fun collectEvents() {
        eventFlow.onEach { event ->
            when (event) {
                is ChatEvent.GetUserDetails -> {
                    _uiState.update { uiState ->
                        uiState.copy(
                            name = event.name,
                            gender = event.gender
                        )
                    }
                }

                is ChatEvent.OnGenderSelect -> _uiState.update { uiState ->
                    uiState.copy(
                        preferGender = event.gender
                    )
                }

                ChatEvent.NavigateToSearch -> {
                    if (uiState.value.preferGender == null) {
                        toaster.error("يرجى اختيار الجنس")
                    } else {
                        uiState.value.apply {
                            navigator.navigate(destination = Destination.SEARCH.name + "/${name}/$gender/${preferGender?.name}")

                        }
                    }
                }

                ChatEvent.OnConversationsClick -> toaster.error("الميزة غير متوفرة")
                ChatEvent.OnMyAccountClick -> toaster.error("الميزة غير متوفرة")
            }
        }.launchIn(viewModelScope)
    }
}