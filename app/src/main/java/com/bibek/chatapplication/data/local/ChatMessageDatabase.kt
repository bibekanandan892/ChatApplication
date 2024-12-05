package com.bibek.chatapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ChatMessageEntity::class], version = 1, exportSchema = false)
abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}