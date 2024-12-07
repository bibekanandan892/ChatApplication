package com.bibek.chatapplication.data.remote

import com.bibek.chatapplication.utils.AUTH_PARAM
import com.bibek.chatapplication.utils.DEVICE_ID_PARAM
import com.bibek.chatapplication.utils.ENDPOINT
import com.bibek.chatapplication.utils.SEC_WEBSOCKET_PROTOCOL_HEADER
import com.bibek.chatapplication.utils.SEC_WEBSOCKET_PROTOCOL_VALUE
import com.bibek.chatapplication.utils.SESSION_ID_PARAM
import com.bibek.chatapplication.utils.SocketEvent
import com.bibek.chatapplication.utils.TOKEN_PARAM
import com.bibek.chatapplication.utils.UDID_PARAM
import com.bibek.chatapplication.utils.USER_AGENT_HEADER
import com.bibek.chatapplication.utils.USER_AGENT_VALUE
import com.bibek.chatapplication.utils.WEBSOCKET_BASE_URL
import com.bibek.chatapplication.utils.dispatcher.DispatcherProvider
import com.bibek.chatapplication.utils.message.createSocketMessageString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * WebsocketClient is a singleton class responsible for managing WebSocket connections.
 * It uses OkHttp for WebSocket communication and Kotlin Coroutines for asynchronous processing.
 *
 * @property client OkHttpClient instance used to create WebSocket connections.
 */
@Singleton
class WebsocketClient @Inject constructor(
    private val client: OkHttpClient,
    private val dispatcherProvider: DispatcherProvider
) : CoroutineScope {

    // Define the coroutine context as IO dispatcher with a supervisor job.
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatcherProvider.io

    // WebSocket instance for managing connection.
    private var ws: WebSocket? = null

    // SharedFlow to emit WebSocket events to observers.
    private val _websocketEventFlow = MutableSharedFlow<String>()
    val websocketEventFlow: SharedFlow<String> = _websocketEventFlow

    // Job for managing the lifecycle of the WebSocket connection coroutine.
    private var websocketConnectJob: Job? = null

    /**
     * Establishes a WebSocket connection with the provided parameters.
     *
     * @param sessionId The session ID to be included in the request.
     * @param deviceId The device ID to be included in the request.
     * @param token The token for authentication.
     * @param auth Additional authentication parameter.
     * @param udid The unique device identifier.
     */
    fun connect(
        sessionId: String?,
        deviceId: String?,
        token: String?,
        auth: String?,
        udid: String?
    ) {
        websocketConnectJob = launch {
            // Construct the WebSocket URL with query parameters.
            val url = "${WEBSOCKET_BASE_URL}/${ENDPOINT}?" +
                    "${SESSION_ID_PARAM}=$sessionId&" +
                    "${DEVICE_ID_PARAM}=$deviceId&" +
                    "${TOKEN_PARAM}=$token&" +
                    "${AUTH_PARAM}=$auth&" +
                    "${UDID_PARAM}=$udid"

            // Create the WebSocket request.
            val request = Request
                .Builder()
                .url(url)
                .addHeader(USER_AGENT_HEADER, USER_AGENT_VALUE)
                .addHeader(SEC_WEBSOCKET_PROTOCOL_HEADER, SEC_WEBSOCKET_PROTOCOL_VALUE)
                .build()

            // Establish the WebSocket connection.
            ws = client.newWebSocket(request, ChatWebSocketListener())
        }
    }

    /**
     * Sends a WebSocket message with the specified event and text.
     *
     * @param event The event type.
     * @param text The message content.
     */
    fun sendEvent(event: SocketEvent, text: String) {
        ws?.send(createSocketMessageString(event = event, text = text))
    }

    /**
     * WebSocketListener implementation to handle WebSocket events.
     */
    private inner class ChatWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            // Emit an "onOpen" event when the WebSocket connection is established.
            launch {
                _websocketEventFlow.emit(SocketEvent.OnOpen.route)
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            // Emit received messages to observers.
            launch {
                _websocketEventFlow.emit(text)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            // Emit an "onClosing" event when the WebSocket is about to close.
            launch {
                _websocketEventFlow.emit(SocketEvent.OnClosing.route)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            // Emit an "onClosed" event when the WebSocket connection is closed.
            launch {
                _websocketEventFlow.emit(SocketEvent.OnClosed.route)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            // Emit an "onFailure" event in case of an error.
            launch {
                _websocketEventFlow.emit(SocketEvent.OnFailure.route + t.message + "]")
            }
        }
    }

    /**
     * Disposes of the WebSocket connection and cancels the associated coroutine job.
     */
    fun dispose() {
        ws?.cancel()
        websocketConnectJob?.cancel()
    }
}
