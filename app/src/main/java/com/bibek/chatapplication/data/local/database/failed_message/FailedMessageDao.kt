package com.bibek.chatapplication.data.local.database.failed_message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing failed messages in the database.
 * Provides methods to perform CRUD operations on the FailedMessageEntity table.
 */
@Dao
interface FailedMessageDao {

    /**
     * Inserts a single failed message into the database.
     * If a conflict occurs, the existing record will be replaced.
     *
     * @param failedMessageEntity The FailedMessageEntity object to insert.
     */
    @Insert(onConflict = REPLACE)
    suspend fun insertChat(failedMessageEntity: FailedMessageEntity)

    /**
     * Retrieves all failed messages from the database as a Flow.
     *
     * @return A Flow emitting a list of all FailedMessageEntity objects.
     */
    @Query("SELECT * FROM Failed_Message_Entity")
    fun getAllFailedMessageFlow(): Flow<List<FailedMessageEntity>>

    /**
     * Retrieves all failed messages from the database as a list.
     *
     * @return A list of all FailedMessageEntity objects.
     */
    @Query("SELECT * FROM Failed_Message_Entity")
    suspend fun getAllFailedMessage(): List<FailedMessageEntity>

    /**
     * Deletes all failed messages from the database.
     */
    @Query("DELETE FROM Failed_Message_Entity")
    suspend fun deleteAllFailedMessage()
}
