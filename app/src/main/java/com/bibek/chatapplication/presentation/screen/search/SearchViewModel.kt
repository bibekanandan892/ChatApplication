package com.bibek.chatapplication.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageEntity
import com.bibek.chatapplication.data.model.websocket.request.ack.AckRequest
import com.bibek.chatapplication.data.model.websocket.request.chat.ChatIdModel
import com.bibek.chatapplication.data.model.websocket.request.match.MatchRequest
import com.bibek.chatapplication.data.model.websocket.request.message.ReceiveMessage
import com.bibek.chatapplication.data.model.websocket.request.message.SendMessage
import com.bibek.chatapplication.data.model.websocket.response.ack.AckResponseLong
import com.bibek.chatapplication.data.model.websocket.response.ack.AckResponseString
import com.bibek.chatapplication.data.model.websocket.response.matched.MatchedResponse
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.utils.READ
import com.bibek.chatapplication.utils.SocketEvent
import com.bibek.chatapplication.utils.connectivity.ConnectionState
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserver
import com.bibek.chatapplication.utils.dispatcher.DispatcherProvider
import com.bibek.chatapplication.utils.logger.Logger
import com.bibek.chatapplication.utils.mapper.toFailedMessage
import com.bibek.chatapplication.utils.mapper.toSendMessage
import com.bibek.chatapplication.utils.message.extractJsonContent
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel responsible for managing the search-related functionality in the Chat Application.
 * It handles websocket events, manages UI state, and interacts with the repository to fetch data
 * and perform actions such as sending/receiving messages and matching users.
 *
 * @property navigator Navigation handler to control the app's flow.
 * @property toaster To display success/failure messages.
 * @property repository Repository for interacting with the data layer.
 * @property json JSON serializer/deserializer.
 * @property connectivityObserver Observer for the device's network state.
 * @property dispatcherProvider Provides coroutine dispatchers for managing threads in a structured way
 *                              (e.g., main, IO, default) to improve testability and flexibility.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val navigator: Navigator,
    private val toaster: Toaster,
    private val repository: Repository,
    private val json: Json,
    private val connectivityObserver: ConnectivityObserver,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {


    /**
     * UI State Flow that represents the current state of the search screen.
     * It holds the list of all chat messages from the repository.
     */
    private val _uiState = MutableStateFlow(SearchState(allChats = repository.getAllChatMessage()))
    val uiState get() = _uiState.asStateFlow()

    /**
     * Event Flow used to trigger UI events such as button clicks and state changes.
     * It allows one-way communication from ViewModel to UI components.
     */
    private val _eventFlow = MutableSharedFlow<SearchEvent>(extraBufferCapacity = 10)
    val eventFlow get() = _eventFlow.asSharedFlow()

    /**
     * WebSocket connection state that tracks whether the WebSocket is connected or disconnected.
     * Used to handle connection lifecycle events.
     */
    private val websocketState = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)

    init {
        /**
         * Initializes the ViewModel and performs the following actions in a coroutine:
         * 1. Delay for 1 second before establishing a WebSocket connection.
         * 2. Delay for another second and then attempt to find a match.
         */
        viewModelScope.launch {
            delay(1000)
            repository.connect() // Connect to the WebSocket.
            delay(1000)
            findMatch() // Start the match process after initial delay.
        }
        observeConnectivity() // Observe the network connectivity changes.
        collectEvents() // Collect and handle UI events.
        collectWebSocketEvent() // Collect and handle WebSocket events.
    }

    /**
     * Observes the network connectivity state and reacts accordingly by reconnecting or disposing of
     * the WebSocket connection based on whether the network is available or unavailable.
     */
    private fun observeConnectivity() {
        connectivityObserver.connectionState
            .distinctUntilChanged() // Avoid emitting the same state repeatedly.
            .onEach {
                when (it) {
                    ConnectionState.Available -> repository.connect() // Reconnect when the network is available.
                    ConnectionState.Unavailable -> repository.dispose() // Disconnect when the network is unavailable.
                }
            }
            .launchIn(viewModelScope) // Launch the flow in the ViewModel's scope.
    }

    /**
     * Initiates the matching process by clearing the database and sending a match request
     * to the server through the repository.
     */
    private fun findMatch() {
        clearDb() // Clears any previous data in the database.
        // Sends the match event to the server to start the matching process.
        repository.sendEvent(event = SocketEvent.Match, text = MatchRequest)
    }

    /**
     * Collects WebSocket events and processes them based on the event type received.
     * Depending on the event, it will trigger specific actions such as handling matches,
     * messages, or connection state changes.
     */
    private fun collectWebSocketEvent() {
        repository.websocketEventFlow.onEach { response ->
            when {
                // Handles WebSocket open event.
                response.startsWith(SocketEvent.OnOpen.route) -> handleOnOpenEvent()
                // Handles WebSocket closing event (no action taken here).
                response.startsWith(SocketEvent.OnClosing.route) -> {}
                // Handles WebSocket closed or failure event, updating the connection state to disconnected.
                response.startsWith(SocketEvent.OnClosed.route) || response.startsWith(SocketEvent.OnFailure.route) -> {
                    websocketState.update { WebSocketState.Disconnected }
                }
                // Handles session-related events (no action taken here).
                response.startsWith(SocketEvent.Session.route, true) -> {}
                // Handles status-related events (no action taken here).
                response.startsWith(SocketEvent.Status.route, true) -> {}
                // Handles the matched event, triggering the handling of a match response.
                response.startsWith(SocketEvent.Matched.route, true) -> handleMatchedEvent(response)
                // Handles sync-related events (no action taken here).
                response.startsWith(SocketEvent.Sync.route, true) -> {}
                // Handles the leave event and triggers a rematch.
                response.startsWith(SocketEvent.Leave.route, true) -> reMatch()
                // Handles the message event and processes the incoming message.
                response.startsWith(SocketEvent.Message.route, true) -> handleMessageEvent(response)
                // Handles the acknowledgment event.
                response.startsWith(SocketEvent.Ack.route, true) -> handleAckEvent(response)
                // Handles type-related events (no action taken here).
                response.startsWith(SocketEvent.Type.route, true) -> {}
                // Handles seen event, triggering related actions.
                response.startsWith(SocketEvent.Seen.route, true) -> handleSeenEvent(response)
                // Logs unknown commands that don't match any predefined events.
                else -> Logger.log(message = "Received unknown command: $response")
            }
        }
            // Launches the collection in the ViewModel's scope to start listening for WebSocket events.
            .launchIn(viewModelScope)
    }

    /**
     * Emits an event to the event flow, which is used to trigger UI-related actions based on user interaction.
     *
     * @param event The event triggered by the user or system.
     */
    fun onEvent(event: SearchEvent) {
        _eventFlow.tryEmit(event) // Emits the event to the eventFlow.
    }

    /**
     * Collects and handles user events from the UI, performing actions based on the event type.
     * This includes actions such as rematching, accepting a match, sending a message, and navigating back.
     */
    fun collectEvents() {
        eventFlow.onEach { event ->
            when (event) {
                // Handles the rematch click event by calling the rematch function.
                SearchEvent.OnRematchClick -> reMatch()
                // Handles the accept match click event.
                SearchEvent.OnAcceptClick -> handleAcceptClick()
                // Handles the send message event.
                SearchEvent.SendMessage -> handleSendMessageClick()
                // Handles the current message change event.
                is SearchEvent.OnCurrentMessageChange -> handleCurrentMessageChange(event)
                // Handles the leave chat click event.
                SearchEvent.OnLeaveChatClick -> handleLeaveChatClick()
                // Handles the back click event.
                SearchEvent.OnBackClick -> handleBackClick()
                // Handles the dialog dismiss click event.
                SearchEvent.OnDialogDismissClick -> dismissDialog()
                else -> {} // No action for unknown events.
            }
        }
            // Launches the event handling flow in the ViewModel's scope.
            .launchIn(viewModelScope)
    }
    /**
     * Handles the "Seen" event received from the WebSocket, updating the status of the message
     * in the repository based on the event response.
     *
     * @param response The raw WebSocket response for the Seen event.
     */
    private suspend fun handleSeenEvent(response: String) {
        // Extract the content from the response specific to the Seen event.
        val sent = extractJsonContent(socketEvent = SocketEvent.Seen, text = response)
        var ackResponseLong: AckResponseLong? = null
        var ackResponseString: AckResponseString? = null
        try {
            // Try to decode the response into AckResponseString.
            ackResponseString = json.decodeFromString<AckResponseString>(sent)
            ackResponseString.apply {
                // If reference (ref) is not null or empty, update the message status to Sent.
                if (!ref.isNullOrEmpty()) {
                    repository.updateStatusById(
                        messageId = ref,
                        newStatus = Message.Sent.status
                    )
                }
            }
        } catch (_: Exception) {
            // If decoding to AckResponseString fails, try decoding to AckResponseLong.
            ackResponseLong = json.decodeFromString<AckResponseLong>(sent)
            ackResponseLong.apply {
                // If reference (ref) is not null, update the message status based on timestamp.
                if (ref != null) {
                    repository.updateStatusByTs(
                        messageTs = ref,
                        newStatus = Message.Sent.status
                    )
                }
            }
        }
        // Delay to see every state of the message. we need to remove this in production.
        delay(1000)
    }

    /**
     * Handles the "Ack" event received from the WebSocket, updating the status of the message
     * in the repository based on the acknowledgment response.
     *
     * @param response The raw WebSocket response for the Ack event.
     */
    private suspend fun handleAckEvent(response: String) {
        // Extract the content from the response specific to the Ack event.
        val ack = extractJsonContent(socketEvent = SocketEvent.Ack, text = response)
        try {
            // Decode the response into AckResponseString.
            val ackResponse = json.decodeFromString<AckResponseString>(ack)
            ackResponse.apply {
                // If reference and status are available, update the message status by ID.
                if (!ref.isNullOrEmpty() && !status.isNullOrEmpty()) {
                    repository.updateStatusById(
                        messageId = ref,
                        newStatus = status
                    )
                } else if (!ref.isNullOrEmpty()) {
                    // If no status, update the message status to Sent by default.
                    repository.updateStatusById(
                        messageId = ref,
                        newStatus = Message.Sent.status
                    )
                }
            }
        } catch (_: Exception) {
            // If decoding fails, do nothing (silent catch).
        }
    }

    /**
     * Handles the "Message" event received from the WebSocket by inserting the failed chat
     * message into the repository and sending an acknowledgment for the received message.
     *
     * @param response The raw WebSocket response for the Message event.
     */
    private suspend fun handleMessageEvent(response: String) {
        // Extract the content from the response specific to the Message event.
        val message = extractJsonContent(socketEvent = SocketEvent.Message, text = response)
        // Decode the response into a ReceiveMessage object.
        val messageResponse = json.decodeFromString<ReceiveMessage>(message)
        // Insert the failed chat message into the repository.
        repository.insertFailedChat(
            chatMessageEntity = ChatMessageEntity(
                timeMillis = messageResponse.ts ?: 0,
                message = messageResponse.content,
                userName = uiState.value.matchUsername,
                chatId = messageResponse.chatId,
                status = "",
                id = messageResponse.id
            )
        )
        // Create an acknowledgment request for the received message.
        val ackRequest = AckRequest(
            id = System.currentTimeMillis(),
            ref = messageResponse.id,
            status = READ
        )
        // Send the acknowledgment event through the repository.
        repository.sendEvent(SocketEvent.Ack, text = json.encodeToString(ackRequest))
    }

    /**
     * Handles the "Matched" event received from the WebSocket by updating the UI state
     * based on whether the match was accepted or not.
     *
     * @param text The raw WebSocket response for the Matched event.
     */
    private suspend fun handleMatchedEvent(text: String) {
        // Extract the content from the response specific to the Matched event.
        val response = extractJsonContent(socketEvent = SocketEvent.Matched, text = text)
        // Decode the response into a MatchedResponse object.
        val matchedResponse = json.decodeFromString<MatchedResponse>(response)
        matchedResponse.apply {
            // If the match was accepted, update the UI state to reflect this.
            if (matchedResponse.accepted == true) {
                if (uiState.value.chatState != ChatState.Matching) {
                    toaster.success("Request accepted") // Show success message.
                }
                // Update UI state based on whether the user was requesting or accepting.
                if (uiState.value.isRequestedForAccepted) {
                    _uiState.update { uiState -> uiState.copy(chatState = ChatState.Accepted) }
                } else {
                    _uiState.update { uiState -> uiState.copy(isRequestAccepted = true) }
                }
            } else {
                // If the match was not accepted, update the UI state to "Matched" state.
                _uiState.update { uiState ->
                    uiState.copy(
                        chatState = ChatState.Matched,
                        matchUsername = udid ?: "",
                        chatId = chatId ?: "",
                        isRequestAccepted = false
                    )
                }
            }
        }
    }


    /**
     * Handles the "OnOpen" WebSocket event. This method updates the websocket state to connected,
     * sends all failed messages from the repository, and then deletes the failed messages.
     */
    private suspend fun handleOnOpenEvent() {
        // Update WebSocket state to Connected
        websocketState.update { WebSocketState.Connected }
        // Perform the message sending in the IO dispatcher to avoid blocking the main thread.
        withContext(dispatcherProvider.io) {
            // Iterate over all failed messages in the repository and resend them.
            repository.getAllFailedMessage().forEach { failedMessage ->
                val sendMessageRequest = failedMessage.toSendMessage()
                val requestString = json.encodeToString(sendMessageRequest)
                delay(200) // Add a small delay before sending each message to prevent flooding.
                // Send each failed message to the WebSocket.
                repository.sendEvent(event = SocketEvent.Send, text = requestString)
            }
            // Once all failed messages are sent, clear the failed messages from the repository.
            repository.deleteAllFailedMessage()
        }
    }

    /**
     * Handles the "Back" button click event. If the chat state is "Accepted", a dialog is shown,
     * otherwise, the user is navigated back and the repository is disposed.
     */
    private fun handleBackClick() {
        if (uiState.value.chatState == ChatState.Accepted) {
            // Show a dialog when back is clicked in the "Accepted" chat state.
            _uiState.update { uiState -> uiState.copy(isShowDialog = true) }
        } else {
            // Navigate back and dispose the repository if chat is not accepted.
            navigator.back()
            repository.dispose()
        }
    }

    /**
     * Handles the "Leave Chat" button click event. It triggers the rematch process and dismisses the dialog.
     */
    private fun handleLeaveChatClick() {
        // Call rematch logic and dismiss the dialog.
        reMatch()
        dismissDialog()
    }

    /**
     * Dismisses the dialog by updating the UI state to hide the dialog.
     */
    private fun dismissDialog() {
        _uiState.update { uiState ->
            uiState.copy(
                isShowDialog = false
            )
        }
    }

    /**
     * Handles changes in the current message input by the user.
     * This updates the UI state with the new message value.
     *
     * @param event The event containing the new message value.
     */
    private fun handleCurrentMessageChange(event: SearchEvent.OnCurrentMessageChange) {
        _uiState.update { uiState -> uiState.copy(currentMessage = event.value) }
    }

    /**
     * Handles the "Send Message" button click event. It sends the current message to the WebSocket
     * if connected, or saves the message as a failed message if disconnected.
     */
    private suspend fun handleSendMessageClick() {
        // Proceed only if the current message is not empty.
        if (uiState.value.currentMessage.isNotEmpty()) {
            val sendMessageRequest = SendMessage(
                content = uiState.value.currentMessage,
                to = uiState.value.matchUsername,
                id = UUID.randomUUID().toString(),
                ts = System.currentTimeMillis()
            )
            // Encode the message to a string for transmission.
            val requestString = json.encodeToString(sendMessageRequest)
            // Insert the message into the repository as a failed chat initially.
            repository.insertFailedChat(
                chatMessageEntity = ChatMessageEntity(
                    timeMillis = sendMessageRequest.ts ?: 0,
                    message = sendMessageRequest.content,
                    userName = uiState.value.username,
                    chatId = uiState.value.chatId,
                    status = Message.Sending.status,
                    id = sendMessageRequest.id
                )
            )
            // Handle message sending based on the WebSocket connection state.
            when (websocketState.value) {
                WebSocketState.Connected -> {
                    // Send the message if connected.
                    repository.sendEvent(event = SocketEvent.Send, text = requestString)
                }
                WebSocketState.Disconnected -> {
                    // Save the message as a failed message if disconnected.
                    repository.insertFailedChat(sendMessageRequest.toFailedMessage())
                }
            }
            // Clear the input field after sending the message.
            _uiState.update { uiState -> uiState.copy(currentMessage = "") }
        }
    }

    /**
     * Handles the "Accept" button click event. It either updates the UI state to reflect that
     * the request was accepted or sets up the UI for requesting acceptance, then sends an "Accept" event.
     */
    private fun handleAcceptClick() {
        if (!uiState.value.isRequestAccepted) {
            // If the request is not accepted, update the UI to show that the request is being made.
            _uiState.update { uiState -> uiState.copy(isRequestedForAccepted = true) }
        } else {
            // If already accepted, update the chat state to "Accepted".
            _uiState.update { uiState -> uiState.copy(chatState = ChatState.Accepted) }
        }
        // Clear the database to start fresh for the chat.
        clearDb()
        // Send an "Accept" event with the current chat ID.
        val chatIdRequest = ChatIdModel(chatId = uiState.value.chatId)
        val chatIdRequestString = json.encodeToString(chatIdRequest)
        repository.sendEvent(SocketEvent.Accept, chatIdRequestString)
    }

    /**
     * Clears all chat data from the repository in a background thread.
     */
    private fun clearDb() {
        viewModelScope.launch(dispatcherProvider.io) {
            // Delete all chats from the repository.
            repository.deleteAllChats()
        }
    }

    /**
     * Triggers the rematch process by resetting the UI state and calling the `findMatch` method.
     */
    private fun reMatch() {
        _uiState.update { uiState ->
            uiState.copy(
                chatState = ChatState.Matching,
                isRequestAccepted = false,
                isRequestedForAccepted = false
            )
        }
        // Find a new match.
        findMatch()
    }

}