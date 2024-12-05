package com.bibek.chatapplication.domain.repository

import com.bibek.chatapplication.data.local.ChatMessageEntity
import com.bibek.chatapplication.utils.SocketEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface Repository {
    val websocketEventFlow: SharedFlow<String>
    fun connect(username: String)
    fun sendEvent(event: SocketEvent, text: String)

    fun getAllChatMessage(): Flow<List<ChatMessageEntity>>
    suspend fun insertChat(chatMessageEntity: ChatMessageEntity)
    suspend fun deleteAllChats()

}