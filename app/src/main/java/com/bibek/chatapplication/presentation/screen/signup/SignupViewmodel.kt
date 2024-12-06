package com.bibek.chatapplication.presentation.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.data.model.authenticate.req.AuthRequest
import com.bibek.chatapplication.data.model.error.ErrorResponse
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.presentation.navigation.Destination
import com.bibek.chatapplication.utils.GENERIC_ERROR_MESSAGE
import com.bibek.chatapplication.utils.generateBasicAuthHeader
import com.bibek.chatapplication.utils.generateToken
import com.bibek.chatapplication.utils.logger.Logger
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignupViewmodel @Inject constructor(
    private val navigator: Navigator,
    private val toaster: Toaster,
    private val repository: Repository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignupState())
    val uiState get() = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<SignupEvent>(extraBufferCapacity = 10)
    val eventFlow get() = _eventFlow.asSharedFlow()

    init {
        collectEvents()
        viewModelScope.launch{
            Logger.log(repository.getAuth().first().toString())
        }
    }

    fun onEvent(event: SignupEvent) {
        _eventFlow.tryEmit(event)
    }

    fun collectEvents() {
        eventFlow.onEach { event ->
            when (event) {
                is SignupEvent.OnNameChange -> {
                    _uiState.update { uiState -> uiState.copy(udid = event.name) }
                }

                is SignupEvent.OnSignupClick -> {
                    uiState.value.apply {
                        if (udid.isEmpty() || udid.length < 8) {
                            toaster.error("يرجى إدخال اسم صالح")
                        } else if (gender == null) {
                            toaster.error("يرجى اختيار الجنس")
                        } else {
                            signup(event.deviceId)
                        }
                    }
                }

                is SignupEvent.OnGenderSelect -> _uiState.update { uiState ->
                    uiState.copy(
                        gender = event.gender
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun signup(deviceId: String = "") {
        val sessionUUID = UUID.randomUUID().toString()
        Logger.log("Generated sessionUUID: $sessionUUID")

        val token = generateToken(sessionId = sessionUUID, deviceId = deviceId)
        Logger.log("Generated token: $token")

        val authorization = generateBasicAuthHeader(deviceId = deviceId, token = token)
        Logger.log("Generated authorization: $authorization")

        repository.authenticate(
            udid = uiState.value.udid,
            deviceId = deviceId,
            token = token,
            authorization = authorization,
            sessionId = sessionUUID,
            userAgent = "FadFed/2.4.3(iOS/15.4)",
            request = AuthRequest(username = uiState.value.udid, password = uiState.value.udid)
        )
            .flowOn(Dispatchers.IO)
            .onStart {
                _uiState.update { uiState -> uiState.copy(isLoading = true) }
            }
            .onEach { authResponse ->
                withContext(context = Dispatchers.IO) {
                    repository.saveUdidName(authResponse.udid)
                    repository.saveAuth(authResponse.auth)
                    repository.saveDeviceId(deviceId)
                    repository.saveToken(token)
                }
                navigator.back()
                navigator.navigate(destination = Destination.HOME.name)
            }
            .onCompletion {
                _uiState.update { uiState -> uiState.copy(isLoading = false) }
            }
            .catch {
                val responseException = it as? ResponseException
                val errorResponse = responseException?.response?.body<ErrorResponse>()
                toaster.error(errorResponse?.error?.message ?: GENERIC_ERROR_MESSAGE)
                _uiState.update { uiState -> uiState.copy(isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}