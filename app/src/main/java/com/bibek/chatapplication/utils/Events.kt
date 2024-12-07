package com.bibek.chatapplication.utils

sealed class SocketEvent(val route: String) {
    data object Session : SocketEvent("""["session",""")
    data object Status : SocketEvent("""["status",""")
    data object Match : SocketEvent("""["match",""")
    data object Matched : SocketEvent("""["matched",""")
    data object Sync : SocketEvent("""["sync",""")
    data object Accept : SocketEvent("""["accept",""")
    data object Leave : SocketEvent("""["leave",""")
    data object Message : SocketEvent("""["message",""")
    data object Ack : SocketEvent("""["ack",""")
    data object Type : SocketEvent("""["type",""")
    data object Send : SocketEvent("""["send",""")
    data object Seen : SocketEvent("""["seen",""")
    data object OnOpen : SocketEvent("OnOpen")
    data object OnClosing : SocketEvent("OnClose")
    data object OnClosed : SocketEvent("OnClosed")
    data object OnFailure : SocketEvent("OnFailure")

}