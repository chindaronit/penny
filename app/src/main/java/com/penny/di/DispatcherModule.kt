package com.penny.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @IODispatcher
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher=Dispatchers.IO
}

@Retention
@Qualifier
annotation class IODispatcher

