package com.bibek.chatapplication.data.remote

import com.bibek.chatapplication.utils.Logger
import com.bibek.chatapplication.utils.SocketEvent
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
    fun connect(username: String = "9337037960") {
        val request = Request
            .Builder()
            .url("wss://dev.wefaaq.net/@fadfedx?session-id=133e4567-e89b-12d3-a456-426614174000&devid=A2B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6&token=3b3ac2ee38fc62bf4fbf0af741f070ae&auth=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvYmoiOiJ1cm46bG9naW4iLCJpYXQiOjE3MzMzMTQwOTgsImF1ZCI6ImRldi53ZWZhYXEubmV0L2ZhZGZlZHgiLCJpc3MiOiJkZXYud2VmYWFxLm5ldCIsInN1YiI6InVybjp1ZDo5MzM3MDM3OTYwL0EyQjJDM0Q0RTVGNkc3SDhJOUowSzFMMk0zTjRPNVA2In0.yUdoT-FC11DzWKenTZFdpW056bHcJT_ZamW9aIzRK5A&udid=9337037960")
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
                connect()
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