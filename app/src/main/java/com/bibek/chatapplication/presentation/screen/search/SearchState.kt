package com.bibek.chatapplication.presentation.screen.search

import com.bibek.chatapplication.data.model.chat.ChatMessage
import com.bibek.chatapplication.presentation.screen.signup.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchState(
    val chatState: ChatState = ChatState.Matching,
    val gender: Gender? = null,
    val username: String = "",
    val matchUsername: String = "",
    val chatId: String = "",
    val isAccepted: Boolean = false,
    val allChats: Flow<List<ChatMessage>> = flowOf(),
    val currentMessage: String = "",
    val isRequestedForAccepted: Boolean = false,
    val isRequestAccepted: Boolean = false,
    val isShowDialog : Boolean = false
)

sealed interface ChatState {
    data object Matching : ChatState
    data object Matched : ChatState
    data object Accepted : ChatState
}

sealed class Message(val status: String) {
    data object Sending : Message("sending")
    data object Sent : Message("sent")
    data object Read : Message("read")
    data object None : Message("")
}