package com.bibek.chatapplication.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.chatapplication.data.local.ChatMessageEntity
import com.bibek.chatapplication.data.model.request.chat.ChatIdModel
import com.bibek.chatapplication.data.model.request.message.ReceiveMessage
import com.bibek.chatapplication.data.model.request.message.SendMessage
import com.bibek.chatapplication.data.model.response.matched.MatchedResponse
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.presentation.navigation.Destination
import com.bibek.chatapplication.utils.Logger
import com.bibek.chatapplication.utils.SocketEvent
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private val json: Json
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState(allChats = repository.getAllChatMessage()))
    val uiState get() = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<SearchEvent>(extraBufferCapacity = 10)
    val eventFlow get() = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            repository.connect("")
            delay(1000)
            findMatch()
        }
        collectEvents()
        collectWebSocketEvent()
    }

    private fun findMatch() {
        clearDb()
        repository.sendEvent(
            event = SocketEvent.Match, text = """{"algo": "R","segment": "modern"}"""
        )
    }

    private fun collectWebSocketEvent() {
        repository.websocketEventFlow.onEach { text ->
            when {
                text.startsWith(SocketEvent.Session.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Session.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Session,
                            text = text
                        )
                    )
                }

                text.startsWith(SocketEvent.Status.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Status.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Status,
                            text = text
                        )
                    )
                }

                text.startsWith(SocketEvent.Matched.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Matched.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Matched,
                            text = text
                        )
                    )
                    val response =
                        extractJsonContent(socketEvent = SocketEvent.Matched, text = text)
                    val matchedResponse = json.decodeFromString<MatchedResponse>(response)
                    matchedResponse.apply {
                        if (matchedResponse.accepted == true) {
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

                text.startsWith(SocketEvent.Sync.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Sync.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Sync,
                            text = text
                        )
                    )
                }

                text.startsWith(SocketEvent.Accept.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Accept.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Accept,
                            text = text
                        )
                    )
                }

                text.startsWith(SocketEvent.Leave.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Leave.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Leave,
                            text = text
                        )
                    )
                   reMatch()
                }

                text.startsWith(SocketEvent.Message.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Message.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Message,
                            text = text
                        )
                    )
                    val message =
                        extractJsonContent(socketEvent = SocketEvent.Message, text = text)
                    val messageResponse = json.decodeFromString<ReceiveMessage>(message)
                    repository.insertChat(
                        chatMessageEntity = ChatMessageEntity(
                            timeMillis = messageResponse.ts ?: 0,
                            message = messageResponse.content,
                            userName = uiState.value.matchUsername,
                            chatId = messageResponse.chatId,
                            status = "",
                            id = messageResponse.id
                        )
                    )

                }

                text.startsWith(SocketEvent.Ack.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Ack.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Ack,
                            text = text
                        )
                    )
                }

                text.startsWith(SocketEvent.Type.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Type.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Type,
                            text = text
                        )
                    )
                }

                text.startsWith(SocketEvent.Send.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Send.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Send,
                            text = text
                        )
                    )
                }

                text.startsWith(SocketEvent.Seen.route, true) -> {
                    Logger.log(
                        message = SocketEvent.Seen.route + "->" + extractJsonContent(
                            socketEvent = SocketEvent.Seen,
                            text = text
                        )
                    )
                }

                else -> {
                    Logger.log(message = "Received unknown command: $text")
                }
            }
        }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SearchEvent) {
        _eventFlow.tryEmit(event)
    }

    fun collectEvents() {
        eventFlow.onEach { event ->

            when (event) {
//                is SignupEvent.OnNameChange -> {
//                    _uiState.update { uiState -> uiState.copy(name = event.name) }
//                }
//
                is SearchEvent.NavigateToChat -> {
                    uiState.value.apply {
                        navigator.navigate(destination = Destination.CHAT.name)
                    }
                }

                SearchEvent.OnRematchClick -> {
                    reMatch()
                }

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
                        val request = SendMessage(
                            content = uiState.value.currentMessage,
                            to = uiState.value.matchUsername,
                            id = UUID.randomUUID().toString(),
                            ts = System.currentTimeMillis()
                        )
                        val requestString = json.encodeToString(request)
                        repository.insertChat(
                            chatMessageEntity = ChatMessageEntity(
                                timeMillis = request.ts ?: 0,
                                message = request.content,
                                userName = uiState.value.username,
                                chatId = uiState.value.chatId,
                                status = "",
                                id = request.id
                            )
                        )
                        repository.sendEvent(event = SocketEvent.Send, text = requestString)
                        _uiState.update { uiState -> uiState.copy(currentMessage = "") }

                    }
                }

                is SearchEvent.OnCurrentMessageChange -> {
                    _uiState.update { uiState -> uiState.copy(currentMessage = event.value) }
                }

                SearchEvent.OnLeaveChatClick ->  reMatch()
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
    private fun clearDb(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllChats()
        }
    }
    private fun reMatch(){
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