package com.bibek.chatapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageDao
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageEntity
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageDao
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageEntity


@Database(entities = [ChatMessageEntity::class, FailedMessageEntity::class], version = 1, exportSchema = false)
abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun failedMessageDao() : FailedMessageDao
}