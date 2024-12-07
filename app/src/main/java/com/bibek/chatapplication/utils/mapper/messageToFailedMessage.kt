package com.bibek.chatapplication.utils.mapper

import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageEntity
import com.bibek.chatapplication.data.model.websocket.request.message.SendMessage

/**
 * Extension function to convert a [SendMessage] object to a [FailedMessageEntity].
 *
 * This function takes a [SendMessage] and maps its properties (content, id, to, ts) to a
 * corresponding [FailedMessageEntity]. This is useful when you need to store a failed message
 * in the local database for later retry or logging purposes.
 *
 * @return A [FailedMessageEntity] object with the same data as the [SendMessage].
 */
fun SendMessage.toFailedMessage(): FailedMessageEntity {
    return FailedMessageEntity(content = content, id = id, to = to, ts = ts)
}

/**
 * Extension function to convert a [FailedMessageEntity] object to a [SendMessage].
 *
 * This function takes a [FailedMessageEntity] and maps its properties (content, id, to, ts) to a
 * corresponding [SendMessage]. This is useful when you want to resend a failed message by converting
 * it back to the format needed for the websocket request.
 *
 * @return A [SendMessage] object with the same data as the [FailedMessageEntity].
 */
fun FailedMessageEntity.toSendMessage(): SendMessage {
    return SendMessage(content = content, id = id, to = to, ts = ts)
}
