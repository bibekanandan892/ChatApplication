package com.bibek.chatapplication.di

import android.content.Context
import androidx.room.Room
import com.bibek.chatapplication.data.local.appdatastore.AppDataStore
import com.bibek.chatapplication.data.local.appdatastore.AppDateStoreImpl
import com.bibek.chatapplication.data.local.database.ChatMessageDatabase
import com.bibek.chatapplication.data.local.database.chat_message.ChatMessageDao
import com.bibek.chatapplication.data.local.database.failed_message.FailedMessageDao
import com.bibek.chatapplication.data.remote.WebsocketClient
import com.bibek.chatapplication.data.repository.RepositoryImpl
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.utils.CHAT_MESSAGE_DATABASE
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserver
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserverImpl
import com.bibek.chatapplication.utils.connectivity.connectivityManager
import com.bibek.chatapplication.utils.logger.NetworkLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return ConnectivityObserverImpl(context.connectivityManager)
    }

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient()
    }

    @Singleton
    @Provides
    fun provideRepository(
        websocketClient: WebsocketClient,
        chatMessageDao: ChatMessageDao,
        failedMessageDao: FailedMessageDao,
        httpClient: HttpClient,
        appDataStore: AppDataStore
    ): Repository {
        return RepositoryImpl(
            websocketClient = websocketClient,
            chatMessageDao = chatMessageDao,
            failedMessageDao = failedMessageDao,
            httpClient = httpClient,
            appDataStore = appDataStore,

        )
    }

    @Singleton
    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideChatMessageDataBase(@ApplicationContext context: Context): ChatMessageDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            ChatMessageDatabase::class.java,
            CHAT_MESSAGE_DATABASE
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(contentType = ContentType.Application.Json, json = Json {
                    ignoreUnknownKeys = true
                })
                json(contentType = ContentType.Application.FormUrlEncoded)
                json(contentType = ContentType.Text.Plain)
            }
            expectSuccess = true
            install(HttpTimeout) {
                socketTimeoutMillis = 60000
                requestTimeoutMillis = 60000
                connectTimeoutMillis = 60000
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
            install(Logging) {
                logger = NetworkLogger()
                level = LogLevel.ALL
            }
        }
    }

    @Singleton
    @Provides
    fun provideChatMessageDao(chatMessageDatabase: ChatMessageDatabase): ChatMessageDao =
        chatMessageDatabase.chatMessageDao()

    @Singleton
    @Provides
    fun providesFailedMessageDao(chatMessageDatabase: ChatMessageDatabase): FailedMessageDao =
        chatMessageDatabase.failedMessageDao()

    // Provides a singleton instance of AppDataStore
    @Singleton
    @Provides
    fun provideAppDataStore(@ApplicationContext context: Context): AppDataStore {
        return AppDateStoreImpl(context = context)
    }

}