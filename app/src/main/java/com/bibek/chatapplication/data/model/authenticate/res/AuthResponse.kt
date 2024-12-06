package com.bibek.chatapplication.data.model.authenticate.res

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("udid")
    val udid: String,
    @SerialName("token")
    val auth: String
)

