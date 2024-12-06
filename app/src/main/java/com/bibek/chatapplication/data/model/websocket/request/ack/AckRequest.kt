package com.bibek.chatapplication.data.model.websocket.request.ack


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AckRequest(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("ref")
    val ref: String? = null,
    @SerialName("status")
    val status: String? = null
)