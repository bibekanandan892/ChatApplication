package com.bibek.chatapplication.data.model.error


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
    @SerialName("code")
    val code: String? = null,
    @SerialName("message")
    val message: String? = null
)