package com.bibek.chatapplication.data.local.database.failed_message

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Failed_Message_Entity")
data class FailedMessageEntity(
    @SerialName("content")
    val content: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("to")
    val to: String? = null,
    @SerialName("ts")
    @PrimaryKey
    val ts: Long? = null
)