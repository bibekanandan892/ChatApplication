package com.bibek.chatapplication.di

import android.content.Context
import androidx.room.Room
import com.bibek.chatapplication.data.local.ChatMessageDao
import com.bibek.chatapplication.data.local.ChatMessageDatabase
import com.bibek.chatapplication.data.remote.WebsocketClient
import com.bibek.chatapplication.data.repository.RepositoryImpl
import com.bibek.chatapplication.domain.repository.Repository
import com.bibek.chatapplication.utils.CHAT_MESSAGE_DATABASE
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserver
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserverImpl
import com.bibek.chatapplication.utils.connectivity.connectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideRepository(websocketClient: WebsocketClient, chatMessageDao: ChatMessageDao): Repository {
        return RepositoryImpl(websocketClient,chatMessageDao)
    }

    @Singleton
    @Provides
    fun provideJson(websocketClient: WebsocketClient) = Json {
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
    fun provideChatMessageDao(chatMessageDatabase: ChatMessageDatabase): ChatMessageDao =
        chatMessageDatabase.chatMessageDao()

}