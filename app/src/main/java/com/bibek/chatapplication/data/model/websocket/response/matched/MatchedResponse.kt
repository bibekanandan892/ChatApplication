package com.bibek.chatapplication.data.model.websocket.response.matched


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchedResponse(
    @SerialName("accepted")
    val accepted: Boolean? = null,
    @SerialName("chatId")
    val chatId: String? = null,
    @SerialName("initiate")
    val initiate: Boolean? = null,
    @SerialName("lang")
    val lang: List<String?>? = null,
    @SerialName("premium")
    val premium: Boolean? = null,
    @SerialName("udid")
    val udid: String? = null
)