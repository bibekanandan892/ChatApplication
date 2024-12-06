package com.bibek.chatapplication.data.model.websocket.request.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReceiveMessage(
    @SerialName("by")
    val `by`: String? = null,
    @SerialName("chatId")
    val chatId: String? = null,
    @SerialName("content")
    val content: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("ts")
    val ts: Long? = null
)