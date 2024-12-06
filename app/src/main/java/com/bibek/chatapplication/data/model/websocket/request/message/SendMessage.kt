package com.bibek.chatapplication.data.model.websocket.request.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessage(
    @SerialName("content")
    val content: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("to")
    val to: String? = null,
    @SerialName("ts")
    val ts: Long? = null
)