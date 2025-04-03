package com.example.mvvmcleanmvicompare.clean.domain.repository

import com.example.mvvmcleanmvicompare.clean.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun toggleTaskDone(task: Task)
    suspend fun syncWithServer()
    suspend fun fetchRemoteTasks()
}
