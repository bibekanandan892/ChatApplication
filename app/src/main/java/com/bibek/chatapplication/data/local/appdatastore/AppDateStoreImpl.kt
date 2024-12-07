package com.bibek.chatapplication.data.local.appdatastore
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bibek.chatapplication.utils.ACCESS_TOKEN
import com.bibek.chatapplication.utils.DEVICE_ID
import com.bibek.chatapplication.utils.PASSWORD_KEY
import com.bibek.chatapplication.utils.PREFERENCES_NAME
import com.bibek.chatapplication.utils.TOKEN
import com.bibek.chatapplication.utils.UDID_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Extension property to initialize DataStore with the specified name for Preferences storage
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME
)

/**
 * Implementation of the AppDataStore interface to manage application-specific preferences
 * using DataStore. Provides methods to save and retrieve user-specific preferences such as
 * authentication token, device ID, and more.
 *
 * @param context The context used to access the DataStore instance.
 */
class AppDateStoreImpl(context: Context) : AppDataStore {

    private val dataStore = context.dataStore

    // Object to define keys used in Preferences for storing and retrieving data.
    private object PreferencesKey {
        val authKey = stringPreferencesKey(name = ACCESS_TOKEN)  // Key for authentication token
        val udidNameKey = stringPreferencesKey(name = UDID_NAME) // Key for UDID name
        val passwordKey = stringPreferencesKey(name = PASSWORD_KEY) // Key for password
        val deviceIdKey = stringPreferencesKey(name = DEVICE_ID) // Key for device ID
        val tokenKey = stringPreferencesKey(name = TOKEN) // Key for token
    }

    // Save the UDID to DataStore
    override suspend fun saveUdidName(udid: String) {
        savePreference(PreferencesKey.udidNameKey, udid)
    }

    // Save the password to DataStore
    override suspend fun savePassword(password: String) {
        savePreference(PreferencesKey.passwordKey, password)
    }

    // Save the authentication token to DataStore
    override suspend fun saveAuth(auth: String) {
        savePreference(PreferencesKey.authKey, auth)
    }

    // Save the device ID to DataStore
    override suspend fun saveDeviceId(deviceId: String) {
        savePreference(PreferencesKey.deviceIdKey, deviceId)
    }

    // Save the token to DataStore
    override suspend fun saveToken(token: String) {
        savePreference(PreferencesKey.tokenKey, token)
    }

    // Retrieve the authentication token as a Flow
    override fun getAuth(): Flow<String?> {
        return getPreference(PreferencesKey.authKey)
    }

    // Retrieve the UDID name as a Flow
    override fun getUdidName(): Flow<String?> {
        return getPreference(PreferencesKey.udidNameKey)
    }

    // Retrieve the password as a Flow
    override fun getPassword(): Flow<String?> {
        return getPreference(PreferencesKey.passwordKey)
    }

    // Retrieve the device ID as a Flow
    override fun getDeviceId(): Flow<String?> {
        return getPreference(PreferencesKey.deviceIdKey)
    }

    // Retrieve the token as a Flow
    override fun getToken(): Flow<String?> {
        return getPreference(PreferencesKey.tokenKey)
    }

    /**
     * Generic method to retrieve a preference value as a Flow.
     *
     * @param key The Preferences key used to identify the stored value.
     * @return A Flow emitting the value associated with the given key or null if not present.
     */
    private fun <T> getPreference(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data
            .catch { exception ->
                // Handle IOException by emitting an empty Preferences object
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] // Extract the value for the given key
            }
    }

    /**
     * Generic method to save a preference value to DataStore.
     *
     * @param key The Preferences key used to identify the value to store.
     * @param value The value to store in the Preferences.
     */
    private suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value // Save the value to the given key
        }
    }
}
