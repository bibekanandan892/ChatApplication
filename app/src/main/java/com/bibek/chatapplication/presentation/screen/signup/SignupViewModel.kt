package com.bibek.chatapplication.presentation.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.data.model.authenticate.req.AuthRequest
import com.bibek.chatapplication.data.model.error.ErrorResponse
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.presentation.navigation.Destination
import com.bibek.chatapplication.utils.GENERIC_ERROR_MESSAGE
import com.bibek.chatapplication.utils.USER_AGENT_VALUE
import com.bibek.chatapplication.utils.dispatcher.DispatcherProvider
import com.bibek.chatapplication.utils.generateBasicAuthHeader
import com.bibek.chatapplication.utils.generateToken
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel responsible for handling the signup process in the application.
 *
 * This ViewModel manages the UI state for the signup screen, listens to user events, and processes actions like name change,
 * gender selection, and submitting the signup form. It interacts with the repository for data persistence and network operations,
 * and uses `Navigator` to handle navigation actions. It also uses `Toaster` for displaying error or success messages to the user.
 *
 * The ViewModel employs a `DispatcherProvider` to control coroutine dispatchers for various operations, ensuring a clean separation
 * between the main thread and background tasks. This enhances testability and flexibility, allowing for custom dispatcher configurations.
 *
 * @param navigator A `Navigator` that handles navigation between screens.
 * @param toaster A `Toaster` used for displaying short messages (e.g., errors, success messages) to the user.
 * @param repository A `Repository` used for performing network operations and data persistence (e.g., authentication, saving user data).
 * @param dispatcherProvider A `DispatcherProvider` used for defining coroutine dispatchers (`main`, `default`, `io`) to ensure efficient
 *                           and testable coroutine execution.
 */
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val navigator: Navigator,         // Handles navigation
    private val toaster: Toaster,             // Displays messages to the user
    private val repository: Repository,       // Performs data operations
    private val dispatcherProvider: DispatcherProvider // Provides coroutine dispatchers
) : ViewModel() {

    // MutableStateFlow to manage the UI state, including loading state, gender, and user name (udid).
    private val _uiState = MutableStateFlow(SignupState())

    // Public read-only version of _uiState
    val uiState get() = _uiState.asStateFlow()

    // MutableSharedFlow to handle user events (e.g., name change, signup button click, gender selection).
    private val _eventFlow = MutableSharedFlow<SignupEvent>(extraBufferCapacity = 10)

    // Public read-only version of _eventFlow
    val eventFlow get() = _eventFlow.asSharedFlow()

    init {
        collectEvents() // Initialize event collection
    }

    /**
     * Dispatches an event to the ViewModel to trigger the appropriate action.
     *
     * @param event The event to be processed (e.g., name change, signup button click).
     */
    fun onEvent(event: SignupEvent) {
        _eventFlow.tryEmit(event)
    }

    /**
     * Collects events emitted from the UI and delegates the handling to corresponding functions.
     *
     * This function subscribes to events such as name change, signup click, and gender selection and calls the appropriate handler.
     */
    private fun collectEvents() {
        eventFlow.onEach { event ->
            when (event) {
                is SignupEvent.OnNameChange -> handleNameChange(event)     // Handles name change
                is SignupEvent.OnSignupClick -> handleSignupClick(event)   // Handles signup button click
                is SignupEvent.OnGenderSelect -> handleGenderClick(event)  // Handles gender selection
            }
        }.launchIn(viewModelScope) // Launch the event flow collection in the ViewModel's scope
    }

    /**
     * Handles the event when the gender is selected.
     *
     * Updates the UI state with the selected gender.
     *
     * @param event The gender selection event containing the selected gender.
     */
    private fun handleGenderClick(event: SignupEvent.OnGenderSelect) {
        _uiState.update { uiState ->
            uiState.copy(gender = event.gender)
        }
    }

    /**
     * Handles the event when the signup button is clicked.
     *
     * Validates the name and gender before proceeding to the signup process. If validation fails, an error message is displayed.
     *
     * @param event The signup click event containing the device ID.
     */
    private suspend fun handleSignupClick(event: SignupEvent.OnSignupClick) {
        uiState.value.apply {
            // Check if the name length is valid
            if (udid.length < 8) {
                toaster.error(" يجب ألا يقل اسم المستخدم عن ثمانية")  // Show error message if the name is too short
            }
            // Check if the gender is selected
            else if (gender == null) {
                toaster.error("يرجى اختيار الجنس") // Show error message if gender is not selected
            } else {
                signup(event.deviceId)  // Proceed with signup if validation passes
            }
        }
    }

    /**
     * Handles the event when the name is changed.
     *
     * Updates the UI state with the new name, filtering out non-alphanumeric characters.
     *
     * @param event The name change event containing the new name.
     */
    private fun handleNameChange(event: SignupEvent.OnNameChange) {
        _uiState.update { uiState ->
            uiState.copy(udid = event.name.filter { it.isLetterOrDigit() }) // Filter out non-letter and non-digit characters
        }
    }

    /**
     * Performs the signup process.
     *
     * Generates a session ID and token, then calls the repository to authenticate the user.
     * Saves the user data (UDID, auth token, device ID) in the repository after successful authentication.
     *
     * @param deviceId The device ID used for authentication.
     */
    private fun signup(deviceId: String = "") {
        val sessionUUID = UUID.randomUUID().toString() // Generate a unique session ID
        val token = generateToken(sessionId = sessionUUID, deviceId = deviceId) // Generate a token
        val authorization = generateBasicAuthHeader(
            deviceId = deviceId,
            token = token
        ) // Generate authorization header

        // Call the repository to authenticate the user and perform necessary actions on success
        repository.authenticate(
            udid = uiState.value.udid,
            deviceId = deviceId,
            token = token,
            authorization = authorization,
            sessionId = sessionUUID,
            userAgent = USER_AGENT_VALUE,
            request = AuthRequest(username = uiState.value.udid, password = uiState.value.udid)
        )
            .flowOn(dispatcherProvider.io) // Perform authentication on the IO dispatcher
            .onStart {
                _uiState.update { uiState -> uiState.copy(isLoading = true) } // Show loading state
            }
            .onEach { authResponse -> // Handle the authentication response
                withContext(context = dispatcherProvider.io) {
                    repository.saveUdidName(authResponse.udid)  // Save the UDID
                    repository.saveAuth(authResponse.auth)     // Save the authentication token
                    repository.saveDeviceId(deviceId)         // Save the device ID
                    repository.saveToken(token)               // Save the token
                }
                navigator.back()  // Navigate back
                navigator.navigate(destination = Destination.HOME.name) // Navigate to the home screen
            }
            .onCompletion {
                _uiState.update { uiState -> uiState.copy(isLoading = false) } // Hide loading state
            }
            .catch {
                // Handle errors during the authentication process
                val responseException = it as? ResponseException
                val errorResponse = responseException?.response?.body<ErrorResponse>()
                toaster.error(
                    errorResponse?.error?.message ?: GENERIC_ERROR_MESSAGE
                ) // Show error message
                _uiState.update { uiState -> uiState.copy(isLoading = false) } // Hide loading state
            }
            .launchIn(viewModelScope) // Launch the flow in the ViewModel scope
    }
}
