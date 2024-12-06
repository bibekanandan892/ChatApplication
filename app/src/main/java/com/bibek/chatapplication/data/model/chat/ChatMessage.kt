package com.bibek.chatapplication.data.model.chat

import com.bibek.chatapplication.data.local.database.ChatMessageEntity
import com.bibek.chatapplication.presentation.screen.search.Message
import com.bibek.chatapplication.utils.convertTimeMillisToTimeFormat

data class ChatMessage(
    val time: String = "",
    val id: String? = "",
    val message: String? = null,
    val chatId: String? = null,
    val userName: String? = null,
    val status: Message? = null
)

fun ChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        time = convertTimeMillisToTimeFormat(timeMillis),
        id = id,
        message = message,
        userName = userName,
        status = if (status == Message.Sent.status) {
            Message.Sent
        } else if (status == Message.Sending.status) {
            Message.Sending
        } else if (status == Message.Read.status) {
            Message.Read
        } else {
            Message.None
        }, chatId = chatId
    )
}