package com.bibek.chatapplication.data.remote

import com.bibek.chatapplication.utils.SocketEvent
import com.bibek.chatapplication.utils.logger.Logger
import com.bibek.chatapplication.utils.message.createSocketMessageString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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
class WebsocketClient @Inject constructor(private val client: OkHttpClient) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private var ws: WebSocket? = null
    private val _websocketEventFlow = MutableSharedFlow<String>()
    val websocketEventFlow: SharedFlow<String> = _websocketEventFlow

    fun connect(
        sessionId: String?,
        deviceId: String?,
        token: String?,
        auth: String?,
        udid: String?
    ) {
        val request = Request
            .Builder()
            .url("wss://dev.wefaaq.net/@fadfedx?session-id=$sessionId&devid=$deviceId&token=$token&auth=$auth&udid=$udid")
            .addHeader("User-Agent", "FadFed/2.4.3(iOS/15.4)")
            .addHeader("Sec-Websocket-Protocol", "v2.fadfedly.com")
            .build()
        ws = client.newWebSocket(request, ChatWebSocketListener())
    }

    fun sendEvent(event: SocketEvent, text: String) {
        Logger.log("send event ${event.route} -> $text")
        ws?.send(createSocketMessageString(event = event, text = text))
    }

    private inner class ChatWebSocketListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            launch {
                _websocketEventFlow.emit(text)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            launch {
                delay(3000)

            }
            Logger.log(
                message = t.message.toString()
            )
        }
    }


    fun dispose() {
        cancel()
        ws?.cancel()
    }

}