package com.bibek.chatapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Chat_Message_Entity")
data class ChatMessageEntity(
    @PrimaryKey
    val timeMillis : Long = 0,
    val id : String? = "",
    val message : String? = null,
    val chatId : String? = null,
    val userName : String? = null,
    val status : String? = null
)
