package com.bibek.chatapplication.utils.mapper

import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageEntity
import com.bibek.chatapplication.data.model.websocket.request.message.SendMessage

fun SendMessage.toFailedMessage(): FailedMessageEntity {
    return FailedMessageEntity(content = content, id = id, to = to, ts = ts)
}
fun FailedMessageEntity.toSendMessage() : SendMessage{
    return SendMessage(content = content, id = id, to = to, ts = ts)
}