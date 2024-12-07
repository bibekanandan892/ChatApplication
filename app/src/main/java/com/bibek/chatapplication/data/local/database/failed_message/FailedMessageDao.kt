package com.bibek.chatapplication.data.local.database.failed_message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FailedMessageDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertChat(failedMessageEntity: FailedMessageEntity)

    @Query("SELECT * FROM Failed_Message_Entity")
    fun getAllFailedMessageFlow(): Flow<List<FailedMessageEntity>>

    @Query("SELECT * FROM Failed_Message_Entity")
    suspend fun getAllFailedMessage(): List<FailedMessageEntity>

    @Query("DELETE FROM Failed_Message_Entity")
    suspend fun deleteAllFailedMessage()
}