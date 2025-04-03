package com.example.mvvmcleanmvicompare.clean.di

import android.content.Context
import com.example.mvvmcleanmvicompare.clean.data.local.TaskDao
import com.example.mvvmcleanmvicompare.clean.data.local.TaskDatabase
import com.example.mvvmcleanmvicompare.clean.data.remote.FakeApiService
import com.example.mvvmcleanmvicompare.clean.data.repository.TaskRepositoryImpl
import com.example.mvvmcleanmvicompare.clean.domain.repository.TaskRepository
import com.example.mvvmcleanmvicompare.clean.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Database + DAO
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase {
        return TaskDatabase.getDatabase(context)
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()

    // Fake API
    @Provides
    @Singleton
    fun provideFakeApiService(): FakeApiService = FakeApiService()

    // Repository
    @Provides
    @Singleton
    fun provideTaskRepository(
        dao: TaskDao,
        api: FakeApiService
    ): TaskRepository = TaskRepositoryImpl(dao, api)

    // Use Cases
    @Provides
    fun provideGetAllTasksUseCase(repo: TaskRepository) = GetAllTasksUseCase(repo)

    @Provides
    fun provideAddTaskUseCase(repo: TaskRepository) = AddTaskUseCase(repo)

    @Provides
    fun provideDeleteTaskUseCase(repo: TaskRepository) = DeleteTaskUseCase(repo)

    @Provides
    fun provideToggleTaskCompletionUseCase(repo: TaskRepository) = ToggleTaskCompletionUseCase(repo)

    @Provides
    fun provideSyncTasksUseCase(repo: TaskRepository) = SyncTasksUseCase(repo)

    @Provides
    fun provideFetchRemoteTasksUseCase(repo: TaskRepository) = FetchRemoteTasksUseCase(repo)
}
