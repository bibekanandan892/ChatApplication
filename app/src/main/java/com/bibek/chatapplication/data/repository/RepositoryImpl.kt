package com.bibek.chatapplication.data.repository

import com.bibek.chatapplication.data.local.appdatastore.AppDataStore
import com.bibek.chatapplication.data.local.database.ChatMessageDao
import com.bibek.chatapplication.data.local.database.ChatMessageEntity
import com.bibek.chatapplication.data.model.authenticate.req.AuthRequest
import com.bibek.chatapplication.data.model.authenticate.res.AuthResponse
import com.bibek.chatapplication.data.model.chat.ChatMessage
import com.bibek.chatapplication.data.model.chat.toChatMessage
import com.bibek.chatapplication.data.remote.WebsocketClient
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.utils.AUTHORIZATION
import com.bibek.chatapplication.utils.CONTENT_TYPE
import com.bibek.chatapplication.utils.SocketEvent
import com.bibek.chatapplication.utils.USER_AGENT
import com.bibek.chatapplication.utils.X_SESSION_ID
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class RepositoryImpl(
    private val websocketClient: WebsocketClient,
    private val chatMessageDao: ChatMessageDao,
    private val httpClient: HttpClient,
    private val appDataStore: AppDataStore
) : Repository {

    override val websocketEventFlow: SharedFlow<String>
        get() = websocketClient.websocketEventFlow

    override suspend fun connect() {
        websocketClient.connect(
            udid = appDataStore.getUdidName().first(),
            sessionId = UUID.randomUUID().toString(),
            deviceId = appDataStore.getDeviceId().first(),
            token = appDataStore.getToken().first(),
            auth = appDataStore.getAuth().first()
        )
    }

    override fun sendEvent(
        event: SocketEvent,
        text: String
    ) {
        websocketClient.sendEvent(event, text)
    }

    override fun authenticate(
        udid: String,
        deviceId: String,
        token: String,
        contentType: String,
        sessionId: String,
        userAgent: String,
        authorization: String,
        request: AuthRequest
    ): Flow<AuthResponse> {
        return flow {
            val response =
                httpClient.put(urlString = "https://dev.wefaaq.net/api/@fadfedx/auth?udid=$udid&devid=$deviceId&token=$token") {
                    headers {
                        header(USER_AGENT, userAgent)
                        header(CONTENT_TYPE, contentType)
                        header(X_SESSION_ID, sessionId)
                        header(AUTHORIZATION, authorization)
                    }
                    setBody(request)
                }.body<AuthResponse>()
            emit(response)
        }
    }

    override fun getAllChatMessage(): Flow<List<ChatMessage>> {
        return chatMessageDao.getAllChatMessage().map {
            it.map { it.toChatMessage() }
        }
    }

    override suspend fun insertChat(chatMessageEntity: ChatMessageEntity) {
        chatMessageDao.insertChat(chatMessageEntity)
    }

    override suspend fun deleteAllChats() {
        chatMessageDao.deleteAll()
    }

    override suspend fun updateStatusById(messageId: String, newStatus: String) {
        chatMessageDao.updateStatusById(messageId = messageId, newStatus = newStatus)
    }

    override suspend fun updateStatusByTs(messageTs: Long, newStatus: String) {
        chatMessageDao.updateStatusByTs(messageTs, newStatus)
    }

    override suspend fun saveUdidName(userName: String) {
        appDataStore.saveUdidName(userName)
    }

    override suspend fun savePassword(password: String) {
        appDataStore.savePassword(password)
    }

    override suspend fun saveAuth(auth: String) {
        appDataStore.saveAuth(auth)
    }

    override fun getAuth(): Flow<String?> {
        return appDataStore.getAuth()
    }

    override fun getUdidName(): Flow<String?> {
        return appDataStore.getUdidName()
    }

    override fun getPassword(): Flow<String?> {
        return appDataStore.getPassword()
    }

    override suspend fun saveDeviceId(deviceId: String) {
        appDataStore.saveDeviceId(deviceId)
    }

    override suspend fun saveToken(token: String) {
        appDataStore.saveToken(token)
    }

    override fun getDeviceId(): Flow<String?> {
        return appDataStore.getDeviceId()
    }

    override fun getToken(): Flow<String?> {
        return appDataStore.getToken()
    }

}