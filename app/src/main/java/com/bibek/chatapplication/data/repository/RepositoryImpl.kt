package com.bibek.chatapplication.data.repository

import com.bibek.chatapplication.data.local.ChatMessageDao
import com.bibek.chatapplication.data.local.ChatMessageEntity
import com.bibek.chatapplication.data.remote.WebsocketClient
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.utils.SocketEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

class RepositoryImpl(
    private val websocketClient: WebsocketClient,
    private val chatMessageDao: ChatMessageDao
) : Repository {

    override val websocketEventFlow: SharedFlow<String>
        get() = websocketClient.websocketEventFlow

    override fun connect(username: String) {
        websocketClient.connect(username)
    }

    override fun sendEvent(
        event: SocketEvent,
        text: String
    ) {
        websocketClient.sendEvent(event, text)
    }

    override fun getAllChatMessage(): Flow<List<ChatMessageEntity>> {
        return chatMessageDao.getAllChatMessage()
    }

    override suspend fun insertChat(chatMessageEntity: ChatMessageEntity) {
        chatMessageDao.insertChat(chatMessageEntity)
    }

    override suspend fun deleteAllChats() {
        chatMessageDao.deleteAll()
    }
}