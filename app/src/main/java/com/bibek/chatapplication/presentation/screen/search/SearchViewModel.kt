package com.bibek.chatapplication.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val navigator: Navigator,
    private val toaster: Toaster
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchState())
    val uiState get() = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<SearchEvent>(extraBufferCapacity = 10)
    val eventFlow get() = _eventFlow.asSharedFlow()

    init {
        collectEvents()
    }

    fun onEvent(event: SearchEvent) {
        _eventFlow.tryEmit(event)
    }

    fun collectEvents() {
        eventFlow.onEach { event ->
            if(event is SearchEvent.GetUserDetails){
//                toaster.error("${event.name}  ${event.gender}  ${event.preferGender}")
            }
//            when (event) {
//                is SignupEvent.OnNameChange -> {
//                    _uiState.update { uiState -> uiState.copy(name = event.name) }
//                }
//
//                is SignupEvent.NavigateToHome -> {
//                    uiState.value.apply {
//                        if (name.isEmpty() || name.length < 3) {
//                            toaster.error("يرجى إدخال اسم صالح")
//                        } else if (gender == null) {
//                            toaster.error("يرجى اختيار الجنس")
//                        } else {
//                            navigator.navigate(destination = Destination.HOME.name + "/${uiState.value.name}/${uiState.value.gender?.name}")
//                        }
//                    }
//                }
//
//                is SignupEvent.OnGenderSelect -> _uiState.update { uiState ->
//                    uiState.copy(
//                        gender = event.gender
//                    )
//                }
//            }
        }.launchIn(viewModelScope)
    }
}