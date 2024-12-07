package com.bibek.chatapplication.data.local.database.chat_message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing chat messages in the database.
 * Provides methods to perform CRUD operations on the ChatMessageEntity table.
 */
@Dao
interface ChatMessageDao {

    /**
     * Inserts or updates a list of chat messages in the database.
     * If a conflict occurs, the existing record will be replaced.
     *
     * @param chatMessageEntities List of ChatMessageEntity objects to upsert.
     */
    @Upsert
    suspend fun upsertAll(chatMessageEntities: List<ChatMessageEntity>)

    /**
     * Inserts a single chat message into the database.
     * If a conflict occurs, the existing record will be replaced.
     *
     * @param chatMessageEntity The ChatMessageEntity object to insert.
     */
    @Insert(onConflict = REPLACE)
    suspend fun insertChat(chatMessageEntity: ChatMessageEntity)

    /**
     * Inserts a list of chat messages into the database.
     * This method does not handle conflicts.
     *
     * @param chatMessageEntities List of ChatMessageEntity objects to insert.
     */
    @Insert
    suspend fun insertChatMessageList(chatMessageEntities: List<ChatMessageEntity>)

    /**
     * Retrieves all chat messages from the database as a Flow.
     *
     * @return A Flow emitting a list of all ChatMessageEntity objects.
     */
    @Query("SELECT * FROM Chat_Message_Entity")
    fun getAllChatMessage(): Flow<List<ChatMessageEntity>>

    /**
     * Updates the status of a chat message identified by its ID.
     *
     * @param messageId The ID of the message to update.
     * @param newStatus The new status to set for the message.
     */
    @Query("UPDATE Chat_Message_Entity SET status = :newStatus WHERE id = :messageId")
    suspend fun updateStatusById(messageId: String, newStatus: String)

    /**
     * Updates the status of a chat message identified by its timestamp.
     *
     * @param messageTs The timestamp of the message to update.
     * @param newStatus The new status to set for the message.
     */
    @Query("UPDATE Chat_Message_Entity SET status = :newStatus WHERE timeMillis = :messageTs")
    suspend fun updateStatusByTs(messageTs: Long, newStatus: String)

    /**
     * Deletes all chat messages from the database.
     */
    @Query("DELETE FROM Chat_Message_Entity")
    suspend fun deleteAll()
}
