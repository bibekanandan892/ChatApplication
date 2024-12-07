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

/**
 * Interface defining the contract for the repository, which acts as the single source of truth for application data.
 * It includes methods for WebSocket operations, database interactions, and persistent data storage.
 */
interface Repository {

    /**
     * A flow of WebSocket events emitted as strings.
     */
    val websocketEventFlow: SharedFlow<String>

    /**
     * Connects to the WebSocket server.
     */
    suspend fun connect()

    /**
     * Sends an event to the WebSocket server.
     *
     * @param event The type of the event being sent.
     * @param text The content of the event.
     */
    fun sendEvent(event: SocketEvent, text: String)

    /**
     * Authenticates the user with the provided credentials.
     *
     * @param udid The unique device identifier.
     * @param deviceId The unique device ID.
     * @param token The authentication token.
     * @param contentType The type of content being sent (default is JSON).
     * @param sessionId The session ID for the authentication request.
     * @param userAgent The user agent string for the request.
     * @param authorization The authorization header value.
     * @param request The authentication request body.
     * @return A [Flow] emitting the authentication response.
     */
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

    /**
     * Retrieves all chat messages from the database.
     *
     * @return A [Flow] emitting a list of [ChatMessage].
     */
    fun getAllChatMessage(): Flow<List<ChatMessage>>

    /**
     * Inserts a failed chat message into the database.
     *
     * @param chatMessageEntity The [ChatMessageEntity] to be inserted.
     */
    suspend fun insertFailedChat(chatMessageEntity: ChatMessageEntity)

    /**
     * Deletes all chat messages from the database.
     */
    suspend fun deleteAllChats()

    /**
     * Updates the status of a chat message by its ID.
     *
     * @param messageId The ID of the message to be updated.
     * @param newStatus The new status value.
     */
    suspend fun updateStatusById(messageId: String, newStatus: String)

    /**
     * Updates the status of a chat message by its timestamp.
     *
     * @param messageTs The timestamp of the message to be updated.
     * @param newStatus The new status value.
     */
    suspend fun updateStatusByTs(messageTs: Long, newStatus: String)

    /**
     * Saves the user's UDID name to persistent storage.
     *
     * @param userName The UDID name to be saved.
     */
    suspend fun saveUdidName(userName: String)

    /**
     * Saves the user's password to persistent storage.
     *
     * @param password The password to be saved.
     */
    suspend fun savePassword(password: String)

    /**
     * Saves the user's authentication token to persistent storage.
     *
     * @param auth The authentication token to be saved.
     */
    suspend fun saveAuth(auth: String)

    /**
     * Saves the device ID to persistent storage.
     *
     * @param deviceId The device ID to be saved.
     */
    suspend fun saveDeviceId(deviceId: String)

    /**
     * Saves the user's token to persistent storage.
     *
     * @param token The token to be saved.
     */
    suspend fun saveToken(token: String)

    /**
     * Retrieves the user's authentication token from persistent storage.
     *
     * @return A [Flow] emitting the authentication token, or `null` if not found.
     */
    fun getAuth(): Flow<String?>

    /**
     * Retrieves the user's UDID name from persistent storage.
     *
     * @return A [Flow] emitting the UDID name, or `null` if not found.
     */
    fun getUdidName(): Flow<String?>

    /**
     * Retrieves the user's password from persistent storage.
     *
     * @return A [Flow] emitting the password, or `null` if not found.
     */
    fun getPassword(): Flow<String?>

    /**
     * Retrieves the device ID from persistent storage.
     *
     * @return A [Flow] emitting the device ID, or `null` if not found.
     */
    fun getDeviceId(): Flow<String?>

    /**
     * Retrieves the user's token from persistent storage.
     *
     * @return A [Flow] emitting the token, or `null` if not found.
     */
    fun getToken(): Flow<String?>

    /**
     * Disconnects from the WebSocket and cleans up resources.
     */
    fun dispose()

    /**
     * Inserts a failed message into the failed messages database.
     *
     * @param failedMessageEntity The [FailedMessageEntity] to be inserted.
     */
    suspend fun insertFailedChat(failedMessageEntity: FailedMessageEntity)

    /**
     * Retrieves all failed messages from the database.
     *
     * @return A list of [FailedMessageEntity].
     */
    suspend fun getAllFailedMessage(): List<FailedMessageEntity>

    /**
     * Deletes all failed messages from the database.
     */
    suspend fun deleteAllFailedMessage()
}
