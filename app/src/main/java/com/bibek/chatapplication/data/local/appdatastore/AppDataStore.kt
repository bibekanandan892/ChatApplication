package com.bibek.chatapplication.data.local.appdatastore
import kotlinx.coroutines.flow.Flow


interface AppDataStore {
    suspend fun saveUdidName(udidName : String)
    suspend fun savePassword(password : String)
    suspend fun saveAuth(auth : String)
    suspend fun saveDeviceId(deviceId: String)
    suspend fun saveToken(token: String)
    fun getAuth() : Flow<String?>
    fun getUdidName() : Flow<String?>
    fun getPassword() : Flow<String?>
    fun getDeviceId(): Flow<String?>
    fun getToken(): Flow<String?>
}