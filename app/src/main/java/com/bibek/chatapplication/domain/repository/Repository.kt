package com.bibek.chatapplication.domain.repository

import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageEntity
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageEntity
import com.bibek.chatapplication.data.model.authenticate.req.AuthRequest
import com.bibek.chatapplication.data.model.authenticate.res.AuthResponse
import com.bibek.chatapplication.data.model.chat.ChatMessage
import com.bibek.chatapplication.utils.APPLICATION_JSON
import com.bibek.chatapplication.utils.SocketEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface Repository {
    val websocketEventFlow: SharedFlow<String>
    suspend fun connect()
    fun sendEvent(event: SocketEvent, text: String)
    fun authenticate(
        udid: String,
        deviceId: String,
        token: String,
        contentType: String = APPLICATION_JSON,
        sessionId: String,
        userAgent: String,
        authorization: String,
        request: AuthRequest
    ): Flow<AuthResponse>

    fun getAllChatMessage(): Flow<List<ChatMessage>>
    suspend fun insertFailedChat(chatMessageEntity: ChatMessageEntity)
    suspend fun deleteAllChats()
    suspend fun updateStatusById(messageId: String, newStatus: String)
    suspend fun updateStatusByTs(messageTs: Long, newStatus: String)
    suspend fun saveUdidName(userName: String)
    suspend fun savePassword(password: String)
    suspend fun saveAuth(auth: String)
    suspend fun saveDeviceId(deviceId: String)
    suspend fun saveToken(token: String)
    fun getAuth(): Flow<String?>
    fun getUdidName(): Flow<String?>
    fun getPassword(): Flow<String?>
    fun getDeviceId(): Flow<String?>
    fun getToken(): Flow<String?>
    fun dispose()
    suspend fun insertFailedChat(failedMessageEntity: FailedMessageEntity)
    suspend fun getAllFailedMessage(): List<FailedMessageEntity>
    suspend fun deleteAllFailedMessage()


}