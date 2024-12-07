package com.bibek.chatapplication.data.model.websocket.response.ack


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AckResponseString(
    @SerialName("by")
    val `by`: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("ref")
    val ref: String? = null,
    @SerialName("status")
    val status: String? = null
)
@Serializable
data class AckResponseLong(
    @SerialName("by")
    val `by`: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("ref")
    val ref: Long? = null,
    @SerialName("status")
    val status: String? = null
)