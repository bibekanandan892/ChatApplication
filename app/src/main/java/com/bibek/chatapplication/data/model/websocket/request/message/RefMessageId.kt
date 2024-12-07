package com.bibek.chatapplication.data.model.websocket.request.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefMessageId(
    @SerialName("ref")
    val ref: String? = null
)