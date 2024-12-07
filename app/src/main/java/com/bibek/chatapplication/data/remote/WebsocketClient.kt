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
import com.bibek.chatapplication.utils.message.createSocketMessageString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

@Singleton
class WebsocketClient @Inject constructor(
    private val client: OkHttpClient,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private var ws: WebSocket? = null
    private val _websocketEventFlow = MutableSharedFlow<String>()
    val websocketEventFlow: SharedFlow<String> = _websocketEventFlow

    private var websocketConnectJob: Job? = null
    fun connect(
        sessionId: String?,
        deviceId: String?,
        token: String?,
        auth: String?,
        udid: String?
    ) {
        websocketConnectJob = launch {
            val url = "${WEBSOCKET_BASE_URL}/${ENDPOINT}?" +
                    "${SESSION_ID_PARAM}=$sessionId&" +
                    "${DEVICE_ID_PARAM}=$deviceId&" +
                    "${TOKEN_PARAM}=$token&" +
                    "${AUTH_PARAM}=$auth&" +
                    "${UDID_PARAM}=$udid"

            val request = Request
                .Builder()
                .url(url)
                .addHeader(USER_AGENT_HEADER, USER_AGENT_VALUE)
                .addHeader(SEC_WEBSOCKET_PROTOCOL_HEADER, SEC_WEBSOCKET_PROTOCOL_VALUE)
                .build()
            ws = client.newWebSocket(request, ChatWebSocketListener())
        }
    }

    fun sendEvent(event: SocketEvent, text: String) {
        ws?.send(createSocketMessageString(event = event, text = text))
    }

    private inner class ChatWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            launch {
                _websocketEventFlow.emit(SocketEvent.OnOpen.route)
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            launch {
                _websocketEventFlow.emit(text)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            launch {
                _websocketEventFlow.emit(SocketEvent.OnClosing.route)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            launch {
                _websocketEventFlow.emit(SocketEvent.OnClosed.route)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            launch {
                _websocketEventFlow.emit(SocketEvent.OnFailure.route + t.message + "]")
            }
        }
    }


    fun dispose() {
        ws?.cancel()
        websocketConnectJob?.cancel()
    }

}