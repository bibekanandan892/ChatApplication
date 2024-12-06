package com.bibek.chatapplication.utils

import android.content.Context
import android.provider.Settings
import com.bibek.chatapplication.utils.logger.Logger
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.text.SimpleDateFormat
import java.util.Locale

fun convertTimeMillisToTimeFormat(timeMillis: Long): String {
    // Create a SimpleDateFormat object for "hh:mm a" format
    val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    // Convert the time in milliseconds to the desired format
    return dateFormat.format(timeMillis)
}
fun generateDeviceId(androidId: String): String {
    val hash = MessageDigest.getInstance("SHA-256").digest(androidId.toByteArray())
    return UUID.nameUUIDFromBytes(hash).toString().replace("-", "").uppercase()
}

fun getAndroidId(context: Context): String {
    return generateDeviceId(
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    )
}

fun generateToken(sessionId: String, deviceId: String): String {
    val mac = Mac.getInstance("HmacMD5")
    val secretKeySpec = SecretKeySpec(sessionId.toByteArray(), "HmacMD5")
    mac.init(secretKeySpec)
    val hashBytes = mac.doFinal(deviceId.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}

fun generateBasicAuthHeader(deviceId: String, token: String): String {
    val credentials = "$deviceId:$token"
    val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray())
    return "Basic $encodedCredentials"
}

fun getErrorDes(
    errorString: String?,
    errorKeys: List<String> = listOf(ERROR_DESCRIPTION_KEY, MESSAGE_KEY, STATUS_DESC_KEY),
): String? {
    try {
        if (errorString == null) {
            return UNKNOWN_ERROR_MESSAGE
        } else {
            val errorObj = JSONObject(errorString)
            errorKeys.forEach { errorKey ->
                if (errorObj.has(errorKey)) {
                    return errorObj.getString(errorKey).toString()
                }
            }
            Logger.log(errorString)

            return UNKNOWN_ERROR_MESSAGE
        }
    } catch (e: Exception) {
        return e.message
    }
}
