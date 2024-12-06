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


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME
)
class AppDateStoreImpl(context: Context) : AppDataStore {

    private val dataStore = context.dataStore

    private object PreferencesKey {
        val authKey = stringPreferencesKey(name = ACCESS_TOKEN)
        val udidNameKey = stringPreferencesKey(name = UDID_NAME)
        val passwordKey = stringPreferencesKey(name = PASSWORD_KEY)
        val deviceIdKey = stringPreferencesKey(name = DEVICE_ID)
        val tokenKey = stringPreferencesKey(name = TOKEN)
    }

    override suspend fun saveUdidName(udidName: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.udidNameKey] = udidName
        }
    }

    override suspend fun savePassword(password: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.passwordKey] = password
        }
    }

    override suspend fun saveAuth(auth: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.authKey] = auth
        }
    }

    override suspend fun saveDeviceId(deviceId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.deviceIdKey] = deviceId
        }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.tokenKey] = token
        }
    }

    override fun getAuth(): Flow<String?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKey.authKey]
        }
    }

    override fun getUdidName(): Flow<String?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKey.udidNameKey]
        }
    }

    override fun getPassword(): Flow<String?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKey.passwordKey]
        }
    }

    override fun getDeviceId(): Flow<String?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKey.deviceIdKey]
        }
    }

    override fun getToken(): Flow<String?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKey.tokenKey]
        }
    }
}
