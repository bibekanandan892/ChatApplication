package com.bibek.chatapplication.data.local.appdatastore
import kotlinx.coroutines.flow.Flow
/**
 * Interface representing a data store for application preferences.
 * Provides methods for saving and retrieving user-specific data such as credentials and tokens.
 */
interface AppDataStore {

    /**
     * Saves the UDID name to the data store.
     *
     * @param udidName The UDID name to be saved.
     */
    suspend fun saveUdidName(udidName: String)

    /**
     * Saves the password to the data store.
     *
     * @param password The password to be saved.
     */
    suspend fun savePassword(password: String)

    /**
     * Saves the authentication token to the data store.
     *
     * @param auth The authentication token to be saved.
     */
    suspend fun saveAuth(auth: String)

    /**
     * Saves the device ID to the data store.
     *
     * @param deviceId The device ID to be saved.
     */
    suspend fun saveDeviceId(deviceId: String)

    /**
     * Saves the session token to the data store.
     *
     * @param token The session token to be saved.
     */
    suspend fun saveToken(token: String)

    /**
     * Retrieves the authentication token from the data store.
     *
     * @return A [Flow] emitting the authentication token, or `null` if not found.
     */
    fun getAuth(): Flow<String?>

    /**
     * Retrieves the UDID name from the data store.
     *
     * @return A [Flow] emitting the UDID name, or `null` if not found.
     */
    fun getUdidName(): Flow<String?>

    /**
     * Retrieves the password from the data store.
     *
     * @return A [Flow] emitting the password, or `null` if not found.
     */
    fun getPassword(): Flow<String?>

    /**
     * Retrieves the device ID from the data store.
     *
     * @return A [Flow] emitting the device ID, or `null` if not found.
     */
    fun getDeviceId(): Flow<String?>

    /**
     * Retrieves the session token from the data store.
     *
     * @return A [Flow] emitting the session token, or `null` if not found.
     */
    fun getToken(): Flow<String?>
}