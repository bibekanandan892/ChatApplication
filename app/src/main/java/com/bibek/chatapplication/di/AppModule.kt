package com.bibek.chatapplication.di

import android.content.Context
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserver
import com.bibek.chatapplication.utils.connectivity.ConnectivityObserverImpl
import com.bibek.chatapplication.utils.connectivity.connectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return ConnectivityObserverImpl(context.connectivityManager)
    }
}