package com.bibek.chatapplication.presentation.screen.home

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

/**
 * The ViewModel for managing the UI state and events of the Home screen.
 * This ViewModel handles the business logic, such as updating the UI state, processing events,
 * and interacting with the navigation and toaster utilities.
 *
 * @param navigator A utility class responsible for handling navigation between screens.
 * @param toaster A utility class for displaying toast messages to the user.
 */
@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val navigator: Navigator,
    private val toaster: Toaster
) : ViewModel() {

    /**
     * A private MutableStateFlow that holds the current state of the Home screen.
     * This flow is exposed as a read-only StateFlow via [uiState].
     */
    private val _uiState = MutableStateFlow(HomeState())

    /**
     * A public read-only StateFlow that represents the current state of the Home screen.
     */
    val uiState get() = _uiState.asStateFlow()

    /**
     * A private MutableSharedFlow used to manage and collect HomeEvents.
     * This flow is exposed as a read-only SharedFlow via [eventFlow].
     */
    private val _eventFlow = MutableSharedFlow<HomeEvent>(extraBufferCapacity = 10)

    /**
     * A public read-only SharedFlow used to observe HomeEvents.
     */
    val eventFlow get() = _eventFlow.asSharedFlow()

    /**
     * Initializes the ViewModel and starts collecting events from the eventFlow.
     * Each event triggers specific actions, such as updating UI state or performing navigation.
     */
    init {
        collectEvents()
    }

    /**
     * Triggers an event, which is emitted to the eventFlow for processing.
     *
     * @param event The event to be processed.
     */
    fun onEvent(event: HomeEvent) {
        _eventFlow.tryEmit(event)
    }

    /**
     * Collects events from the eventFlow and performs corresponding actions.
     * Events can update the UI state, show toast messages, or navigate to other screens.
     */
    fun collectEvents() {
        eventFlow.onEach { event ->
            when (event) {
                is HomeEvent.GetUserDetails -> {
                    // Update UI state with user details
                    _uiState.update { uiState ->
                        uiState.copy(
                            name = event.name,
                            gender = event.gender
                        )
                    }
                }

                is HomeEvent.OnGenderSelect -> {
                    // Update preferred gender in UI state
                    _uiState.update { uiState ->
                        uiState.copy(
                            preferGender = event.gender
                        )
                    }
                }

                HomeEvent.NavigateToSearch -> {
                    // Navigate to the search screen if gender is selected, otherwise show an error toast
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
