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
import com.bibek.chatapplication.utils.SocketEvent
import com.bibek.chatapplication.utils.connectivity.ConnectionState
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserver
import com.bibek.chatapplication.utils.logger.Logger
import com.bibek.chatapplication.utils.mapper.toFailedMessage
import com.bibek.chatapplication.utils.mapper.toSendMessage
import com.bibek.chatapplication.utils.message.extractJsonContent
import com.bibek.chatapplication.utils.navigation.Navigator
import com.bibek.chatapplication.utils.toaster.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val navigator: Navigator,
    private val toaster: Toaster,
    private val repository: Repository,
    private val json: Json,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState(allChats = repository.getAllChatMessage()))
    val uiState get() = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<SearchEvent>(extraBufferCapacity = 10)
    val eventFlow get() = _eventFlow.asSharedFlow()
    private val websocketState = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)

    init {
        viewModelScope.launch {
            delay(1000)
            repository.connect()
            delay(1000)
            findMatch()
        }
        observeConnectivity()
        collectEvents()
        collectWebSocketEvent()
    }

    private fun observeConnectivity() {
        connectivityObserver.connectionState
            .distinctUntilChanged()
            .onEach {
                when (it) {
                    ConnectionState.Available -> repository.connect()
                    ConnectionState.Unavailable -> repository.dispose()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun findMatch() {
        clearDb()
        repository.sendEvent(
            event = SocketEvent.Match, text = MatchRequest
        )
    }

    private fun collectWebSocketEvent() {
        repository.websocketEventFlow.onEach { response ->
            when {
                response.startsWith(SocketEvent.OnOpen.route) -> handleOnOpenEvent()
                response.startsWith(SocketEvent.OnClosing.route) -> {}
                response.startsWith(SocketEvent.OnClosed.route) -> {
                    websocketState.update { WebSocketState.Disconnected }
                }
                response.startsWith(SocketEvent.OnFailure.route) -> {
                    websocketState.update { WebSocketState.Disconnected }
                }
                response.startsWith(SocketEvent.Session.route, true) -> {}
                response.startsWith(SocketEvent.Status.route, true) -> {}
                response.startsWith(SocketEvent.Matched.route, true) -> handleMatchedEvent(response)
                response.startsWith(SocketEvent.Sync.route, true) -> {}
                response.startsWith(SocketEvent.Leave.route, true) -> reMatch()
                response.startsWith(SocketEvent.Message.route, true) -> handleMessageEvent(response)
                response.startsWith(SocketEvent.Ack.route, true) -> {
                    val ack =
                        extractJsonContent(socketEvent = SocketEvent.Ack, text = response)
                    try {
                        val ackResponse = json.decodeFromString<AckResponseString>(ack)
                        ackResponse.apply {
                            if (!ref.isNullOrEmpty() && !status.isNullOrEmpty()) {
                                repository.updateStatusById(
                                    messageId = ref,
                                    newStatus = status
                                )
                            } else if (!ref.isNullOrEmpty()) {
                                repository.updateStatusById(
                                    messageId = ref,
                                    newStatus = Message.Sent.status
                                )
                            }
                        }
                    } catch (_: Exception) {
                    }

                }

                response.startsWith(SocketEvent.Type.route, true) -> {
                }

                response.startsWith(SocketEvent.Seen.route, true) -> {
                    val sent = extractJsonContent(socketEvent = SocketEvent.Seen, text = response)
                    var ackResponseLong: AckResponseLong? = null
                    var ackResponseString: AckResponseString? = null
                    try {
                        ackResponseString = json.decodeFromString<AckResponseString>(sent)
                        ackResponseString.apply {
                            if (!ref.isNullOrEmpty()) {
                                repository.updateStatusById(
                                    messageId = ref,
                                    newStatus = Message.Sent.status
                                )
                            }
                        }
                    } catch (_: Exception) {
                        ackResponseLong = json.decodeFromString<AckResponseLong>(sent)
                        ackResponseLong.apply {
                            if (ref != null) {
                                repository.updateStatusByTs(
                                    messageTs = ref,
                                    newStatus = Message.Sent.status
                                )
                            }
                        }
                    }
                    delay(1000)
                }

                else -> {
                    Logger.log(message = "Received unknown command: $response")
                }
            }
        }
            .launchIn(viewModelScope)
    }

    private suspend fun handleMessageEvent(response: String) {
        val message =
            extractJsonContent(socketEvent = SocketEvent.Message, text = response)
        val messageResponse = json.decodeFromString<ReceiveMessage>(message)
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
        val ackRequest = AckRequest(
            id = System.currentTimeMillis(),
            ref = messageResponse.id,
            status = "read"
        )
        repository.sendEvent(SocketEvent.Ack, text = json.encodeToString(ackRequest))
    }

    private suspend fun handleMatchedEvent(text: String) {
        val response =
            extractJsonContent(socketEvent = SocketEvent.Matched, text = text)
        val matchedResponse = json.decodeFromString<MatchedResponse>(response)
        matchedResponse.apply {
            if (matchedResponse.accepted == true) {
                if (uiState.value.chatState != ChatState.Matching) {
                    toaster.success("Request accepted")
                }
                if (uiState.value.isRequestedForAccepted) {
                    _uiState.update { uiState -> uiState.copy(chatState = ChatState.Accepted) }
                } else {
                    _uiState.update { uiState -> uiState.copy(isRequestAccepted = true) }
                }

            } else {
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

    private suspend fun handleOnOpenEvent() {
        websocketState.update { WebSocketState.Connected }
        withContext(Dispatchers.IO) {
            repository.getAllFailedMessage().forEach { failedMessage ->
                val sendMessageRequest = failedMessage.toSendMessage()
                val requestString = json.encodeToString(sendMessageRequest)
                delay(200)
                repository.sendEvent(event = SocketEvent.Send, text = requestString)
            }
            repository.deleteAllFailedMessage()
        }
    }

    fun onEvent(event: SearchEvent) {
        _eventFlow.tryEmit(event)
    }

    fun collectEvents() {
        eventFlow.onEach { event ->
            when (event) {
                SearchEvent.OnRematchClick -> reMatch()
                SearchEvent.OnAcceptClick -> {
                    if (!uiState.value.isRequestAccepted) {
                        _uiState.update { uiState -> uiState.copy(isRequestedForAccepted = true) }
                    } else {
                        _uiState.update { uiState -> uiState.copy(chatState = ChatState.Accepted) }
                    }
                    clearDb()
                    val chatIdRequest = ChatIdModel(chatId = uiState.value.chatId)
                    val chatIdRequestString = json.encodeToString(chatIdRequest)
                    repository.sendEvent(SocketEvent.Accept, chatIdRequestString)
                }

                SearchEvent.SendMessage -> {
                    if (uiState.value.currentMessage.isNotEmpty()) {
                        val sendMessageRequest = SendMessage(
                            content = uiState.value.currentMessage,
                            to = uiState.value.matchUsername,
                            id = UUID.randomUUID().toString(),
                            ts = System.currentTimeMillis()
                        )
                        val requestString = json.encodeToString(sendMessageRequest)
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
                        when (websocketState.value) {
                            WebSocketState.Connected -> {
                                repository.sendEvent(event = SocketEvent.Send, text = requestString)
                            }

                            WebSocketState.Disconnected -> {
                                repository.insertFailedChat(sendMessageRequest.toFailedMessage())
                            }
                        }
                        _uiState.update { uiState -> uiState.copy(currentMessage = "") }
                    }
                }

                is SearchEvent.OnCurrentMessageChange -> {
                    _uiState.update { uiState -> uiState.copy(currentMessage = event.value) }
                }

                SearchEvent.OnLeaveChatClick -> {
                    reMatch()
                    _uiState.update { uiState ->
                        uiState.copy(
                            isShowDialog = false
                        )
                    }
                }

                SearchEvent.OnBackClick -> {
                    if (uiState.value.chatState == ChatState.Accepted) {
                        _uiState.update { uiState -> uiState.copy(isShowDialog = true) }
                    } else {
                        navigator.back()
                        repository.dispose()
                    }
                }

                SearchEvent.OnDialogDismissClick -> _uiState.update { uiState ->
                    uiState.copy(
                        isShowDialog = false
                    )
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun clearDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllChats()
        }
    }

    private fun reMatch() {
        _uiState.update { uiState ->
            uiState.copy(
                chatState = ChatState.Matching,
                isRequestAccepted = false,
                isRequestedForAccepted = false
            )
        }
        findMatch()
    }
}