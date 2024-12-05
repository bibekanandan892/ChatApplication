package com.bibek.chatapplication.data.model.response.sync


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncResponse(
    @SerialName("data")
    val `data`: Data? = null
) {
    @Serializable
    data class Data(
        @SerialName("data")
        val `data`: Data? = null,
        @SerialName("dataset")
        val dataset: String? = null,
        @SerialName("item")
        val item: String? = null,
        @SerialName("version")
        val version: Int? = null
    ) {
        @Serializable
        data class Data(
            @SerialName("configuration")
            val configuration: String? = null,
            @SerialName("gender")
            val gender: String? = null,
            @SerialName("image")
            val image: String? = null,
            @SerialName("name")
            val name: String? = null,
            @SerialName("premium")
            val premium: Boolean? = null,
            @SerialName("udid")
            val udid: String? = null,
            @SerialName("username")
            val username: String? = null
        )
    }
}