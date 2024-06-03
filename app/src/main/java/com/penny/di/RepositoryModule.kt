package com.penny.di

import com.google.firebase.firestore.FirebaseFirestore
import com.penny.data.repository.AccountsRepository
import com.penny.data.repository.AccountsRepositoryImpl
import com.penny.data.repository.AnalysisRepository
import com.penny.data.repository.AnalysisRepositoryImpl
import com.penny.data.repository.FeaturedRepository
import com.penny.data.repository.FeaturedRepositoryImpl
import com.penny.data.repository.TransactionRepository
import com.penny.data.repository.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFeaturedRepository(db: FirebaseFirestore,@IODispatcher ioDispatcher: CoroutineDispatcher): FeaturedRepository {
        return FeaturedRepositoryImpl(
            db,
            ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideAccountsRepository(db: FirebaseFirestore,@IODispatcher ioDispatcher: CoroutineDispatcher): AccountsRepository {
        return AccountsRepositoryImpl(
            db,
            ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db: FirebaseFirestore,@IODispatcher ioDispatcher: CoroutineDispatcher): TransactionRepository {
        return TransactionRepositoryImpl(
            db,
            ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideAnalysisRepository(db: FirebaseFirestore,@IODispatcher ioDispatcher: CoroutineDispatcher): AnalysisRepository {
        return AnalysisRepositoryImpl(
            db,
            ioDispatcher
        )
    }
}