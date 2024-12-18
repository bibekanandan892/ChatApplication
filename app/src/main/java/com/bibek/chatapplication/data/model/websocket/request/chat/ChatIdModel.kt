package com.bibek.chatapplication.data.model.websocket.request.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatIdModel(
    @SerialName("chatId")
    val chatId: String? = null
)