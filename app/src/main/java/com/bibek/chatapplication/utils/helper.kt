package com.bibek.chatapplication.utils

import android.content.Context
import android.provider.Settings
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Converts a time in milliseconds to a formatted time string.
 *
 * @param timeMillis The time in milliseconds since epoch.
 * @return A formatted time string in the format "h:mm a" (e.g., "1:30 PM").
 */
fun convertTimeMillisToTimeFormat(timeMillis: Long): String {
    // Create a SimpleDateFormat object for "h:mm a" format
    val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    // Convert the time in milliseconds to the desired format
    return dateFormat.format(timeMillis)
}

/**
 * Generates a unique device ID based on the Android ID using SHA-256 hashing.
 *
 * @param androidId The Android ID of the device, typically retrieved from the system.
 * @return A 32-character uppercase string representing the hashed device ID.
 */
fun generateDeviceId(androidId: String): String {
    // Hash the Android ID using SHA-256
    val hash = MessageDigest.getInstance("SHA-256").digest(androidId.toByteArray())
    // Convert the hash into a UUID, remove dashes, and convert to uppercase
    return UUID.nameUUIDFromBytes(hash).toString().replace("-", "").uppercase()
}

/**
 * Retrieves the Android ID from the device's system settings and generates a unique device ID.
 *
 * @param context The application context used to access system settings.
 * @return A 32-character uppercase string representing the unique device ID.
 */
fun getAndroidId(context: Context): String {
    return generateDeviceId(
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    )
}

/**
 * Generates a secure token using HMAC-MD5 hashing with a session ID as the secret key.
 *
 * @param sessionId The session ID to use as the secret key for the HMAC hash.
 * @param deviceId The device ID to be hashed.
 * @return A hexadecimal string representing the generated token.
 */
fun generateToken(sessionId: String, deviceId: String): String {
    // Initialize HMAC-MD5 with the session ID as the secret key
    val mac = Mac.getInstance("HmacMD5")
    val secretKeySpec = SecretKeySpec(sessionId.toByteArray(), "HmacMD5")
    mac.init(secretKeySpec)

    // Compute the HMAC hash of the device ID
    val hashBytes = mac.doFinal(deviceId.toByteArray())

    // Convert the hash bytes to a hexadecimal string
    return hashBytes.joinToString("") { "%02x".format(it) }
}

/**
 * Generates a Basic Authentication header value.
 *
 * @param deviceId The device ID to be included in the credentials.
 * @param token The token to be included in the credentials.
 * @return A string formatted as "Basic <encoded_credentials>".
 */
fun generateBasicAuthHeader(deviceId: String, token: String): String {
    // Combine the device ID and token into a credentials string
    val credentials = "$deviceId:$token"

    // Encode the credentials using Base64
    val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray())

    // Return the Basic Authentication header value
    return "Basic $encodedCredentials"
}