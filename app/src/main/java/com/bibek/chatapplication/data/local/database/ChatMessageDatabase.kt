package com.bibek.chatapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageDao
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageEntity
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageDao
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageEntity

/**
 * Database class for the Chat Application.
 * Defines the database configuration and serves as the main access point
 * for the underlying connection to the app's SQLite database.
 *
 * @property chatMessageDao Provides access to chat message operations.
 * @property failedMessageDao Provides access to failed message operations.
 */
@Database(entities = [ChatMessageEntity::class, FailedMessageEntity::class], version = 1, exportSchema = false)
abstract class ChatMessageDatabase : RoomDatabase() {

    /**
     * Provides access to the ChatMessageDao interface for performing
     * CRUD operations on the ChatMessageEntity table.
     *
     * @return ChatMessageDao instance.
     */
    abstract fun chatMessageDao(): ChatMessageDao

    /**
     * Provides access to the FailedMessageDao interface for performing
     * CRUD operations on the FailedMessageEntity table.
     *
     * @return FailedMessageDao instance.
     */
    abstract fun failedMessageDao(): FailedMessageDao
}
