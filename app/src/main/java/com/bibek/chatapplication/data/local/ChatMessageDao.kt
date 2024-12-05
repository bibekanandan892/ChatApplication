package com.bibek.chatapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface ChatMessageDao {

    @Upsert
    suspend fun upsertAll(chatMessageEntities: List<ChatMessageEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insertChat(chatMessageEntity : ChatMessageEntity)

    @Insert
    suspend fun insertChatMessageList(chatMessageEntities: List<ChatMessageEntity>)

    @Query("SELECT * FROM Chat_Message_Entity")
    fun getAllChatMessage(): Flow<List<ChatMessageEntity>>

    @Query("DELETE FROM Chat_Message_Entity")
    suspend fun deleteAll()
}