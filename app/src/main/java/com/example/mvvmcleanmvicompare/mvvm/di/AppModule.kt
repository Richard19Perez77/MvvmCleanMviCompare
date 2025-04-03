package com.example.mvvmcleanmvicompare.mvvm.di

import android.content.Context
import com.example.mvvmcleanmvicompare.mvvm.data.TaskDao
import com.example.mvvmcleanmvicompare.mvvm.data.TaskDatabase
import com.example.mvvmcleanmvicompare.mvvm.data.TaskRepository
import com.example.mvvmcleanmvicompare.mvvm.network.FakeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TaskDatabase {
        return TaskDatabase.getDatabase(context)
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao, api: FakeApiService): TaskRepository {
        return TaskRepository(dao, api)
    }

    @Provides
    @Singleton
    fun provideFakeApiService(): FakeApiService {
        return FakeApiService()
    }
}
