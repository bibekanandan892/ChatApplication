package com.bibek.chatapplication.data.repository

import com.bibek.chatapplication.data.local.appdatastore.AppDataStore
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageDao
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageEntity
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageDao
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageEntity
import com.bibek.chatapplication.data.model.authenticate.req.AuthRequest
import com.bibek.chatapplication.data.model.authenticate.res.AuthResponse
import com.bibek.chatapplication.data.model.chat.ChatMessage
import com.bibek.chatapplication.data.model.chat.toChatMessage
import com.bibek.chatapplication.data.remote.WebsocketClient
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.utils.AUTHORIZATION
import com.bibek.chatapplication.utils.AUTH_ENDPOINT
import com.bibek.chatapplication.utils.BASE_URL
import com.bibek.chatapplication.utils.CONTENT_TYPE
import com.bibek.chatapplication.utils.DEVICE_ID_PARAM
import com.bibek.chatapplication.utils.SocketEvent
import com.bibek.chatapplication.utils.TOKEN_PARAM
import com.bibek.chatapplication.utils.UDID_PARAM
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

/**
 * Implementation of the Repository interface that acts as a single source of truth
 * for managing data from various sources such as WebSocket, Room Database, and DataStore.
 *
 * @property websocketClient Instance of WebsocketClient for WebSocket communication.
 * @property chatMessageDao DAO for managing chat message data in Room Database.
 * @property failedMessageDao DAO for managing failed messages in Room Database.
 * @property httpClient Ktor HttpClient for network operations.
 * @property appDataStore DataStore for persisting app-specific data.
 */
class RepositoryImpl(
    private val websocketClient: WebsocketClient,
    private val chatMessageDao: ChatMessageDao,
    private val failedMessageDao: FailedMessageDao,
    private val httpClient: HttpClient,
    private val appDataStore: AppDataStore
) : Repository {

    /**
     * Exposes WebSocket event flow to observe WebSocket state and messages.
     */
    override val websocketEventFlow: SharedFlow<String>
        get() = websocketClient.websocketEventFlow

    /**
     * Connects to the WebSocket server using credentials and parameters stored in DataStore.
     */
    override suspend fun connect() {
        websocketClient.connect(
            udid = appDataStore.getUdidName().first(),
            sessionId = UUID.randomUUID().toString(),
            deviceId = appDataStore.getDeviceId().first(),
            token = appDataStore.getToken().first(),
            auth = appDataStore.getAuth().first()
        )
    }

    /**
     * Sends a custom event with associated text over the WebSocket connection.
     *
     * @param event The type of event to send.
     * @param text The message to send with the event.
     */
    override fun sendEvent(event: SocketEvent, text: String) {
        websocketClient.sendEvent(event, text)
    }

    /**
     * Authenticates the user with the server using a PUT request.
     *
     * @param udid Unique Device Identifier.
     * @param deviceId Device ID.
     * @param token Authentication token.
     * @param contentType Content-Type header value.
     * @param sessionId Session ID.
     * @param userAgent User-Agent header value.
     * @param authorization Authorization header value.
     * @param request Authentication request body.
     * @return A flow emitting the authentication response.
     */
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
            val url = "${BASE_URL}${AUTH_ENDPOINT}?" +
                    "${UDID_PARAM}=$udid&" +
                    "${DEVICE_ID_PARAM}=$deviceId&" +
                    "${TOKEN_PARAM}=$token"
            val response = httpClient.put(urlString = url) {
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

    /**
     * Retrieves all chat messages from the local database.
     *
     * @return A flow emitting a list of ChatMessage objects.
     */
    override fun getAllChatMessage(): Flow<List<ChatMessage>> {
        return chatMessageDao.getAllChatMessage().map {
            it.map { entity -> entity.toChatMessage() }
        }
    }

    /**
     * Inserts a failed chat message into the database.
     *
     * @param chatMessageEntity The entity representing the chat message to insert.
     */
    override suspend fun insertFailedChat(chatMessageEntity: ChatMessageEntity) {
        chatMessageDao.insertChat(chatMessageEntity)
    }

    /**
     * Deletes all chat messages from the database.
     */
    override suspend fun deleteAllChats() {
        chatMessageDao.deleteAll()
    }

    /**
     * Updates the status of a chat message by its ID.
     *
     * @param messageId The ID of the message to update.
     * @param newStatus The new status value.
     */
    override suspend fun updateStatusById(messageId: String, newStatus: String) {
        chatMessageDao.updateStatusById(messageId = messageId, newStatus = newStatus)
    }

    /**
     * Updates the status of a chat message by its timestamp.
     *
     * @param messageTs The timestamp of the message to update.
     * @param newStatus The new status value.
     */
    override suspend fun updateStatusByTs(messageTs: Long, newStatus: String) {
        chatMessageDao.updateStatusByTs(messageTs, newStatus)
    }

    // DataStore operations for saving and retrieving user-related data.

    /**
     * Saves the user's unique device name (UDID) into the DataStore.
     *
     * @param userName The unique device name to be saved.
     */
    override suspend fun saveUdidName(userName: String) = appDataStore.saveUdidName(userName)

    /**
     * Saves the user's password into the DataStore.
     *
     * @param password The password to be saved.
     */
    override suspend fun savePassword(password: String) = appDataStore.savePassword(password)

    /**
     * Saves the user's authentication token into the DataStore.
     *
     * @param auth The authentication token to be saved.
     */
    override suspend fun saveAuth(auth: String) = appDataStore.saveAuth(auth)

    /**
     * Retrieves the saved authentication token from the DataStore.
     *
     * @return A Flow emitting the saved authentication token, or null if not found.
     */
    override fun getAuth(): Flow<String?> = appDataStore.getAuth()

    /**
     * Retrieves the saved unique device name (UDID) from the DataStore.
     *
     * @return A Flow emitting the saved UDID, or null if not found.
     */
    override fun getUdidName(): Flow<String?> = appDataStore.getUdidName()

    /**
     * Retrieves the saved password from the DataStore.
     *
     * @return A Flow emitting the saved password, or null if not found.
     */
    override fun getPassword(): Flow<String?> = appDataStore.getPassword()

    /**
     * Saves the device ID into the DataStore.
     *
     * @param deviceId The device ID to be saved.
     */
    override suspend fun saveDeviceId(deviceId: String) = appDataStore.saveDeviceId(deviceId)

    /**
     * Saves the token into the DataStore.
     *
     * @param token The token to be saved.
     */
    override suspend fun saveToken(token: String) = appDataStore.saveToken(token)

    /**
     * Retrieves the saved device ID from the DataStore.
     *
     * @return A Flow emitting the saved device ID, or null if not found.
     */
    override fun getDeviceId(): Flow<String?> = appDataStore.getDeviceId()

    /**
     * Retrieves the saved token from the DataStore.
     *
     * @return A Flow emitting the saved token, or null if not found.
     */
    override fun getToken(): Flow<String?> = appDataStore.getToken()

    /**
     * Disconnects from the WebSocket server and performs necessary cleanup operations.
     */
    override fun dispose() {
        websocketClient.dispose()
    }

    /**
     * Inserts a failed message into the local database for later processing or retry.
     *
     * @param failedMessageEntity The entity representing the failed message to be inserted.
     */
    override suspend fun insertFailedChat(failedMessageEntity: FailedMessageEntity) {
        failedMessageDao.insertChat(failedMessageEntity)
    }

    /**
     * Retrieves all failed messages from the local database.
     *
     * @return A list of FailedMessageEntity objects representing the failed messages.
     */
    override suspend fun getAllFailedMessage(): List<FailedMessageEntity> {
        return failedMessageDao.getAllFailedMessage()
    }

    /**
     * Deletes all failed messages from the local database.
     */
    override suspend fun deleteAllFailedMessage() {
        failedMessageDao.deleteAllFailedMessage()
    }
}