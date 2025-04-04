package com.example.mvvmcleanmvicompare.mvi.data

import com.example.mvvmcleanmvicompare.mvi.network.FakeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class TaskRepository(
    private val dao: TaskDao,
    private val api: FakeApiService // Injected
) {
    val allTasks: Flow<List<TaskMVI>> = dao.getAllTasks()

    suspend fun addTask(task: TaskMVI) = dao.insert(task)

    suspend fun deleteTask(task: TaskMVI) = dao.delete(task)

    suspend fun toggleTaskDone(task: TaskMVI) =
        dao.update(task.copy(isDone = !task.isDone))

    suspend fun syncWithServer() {
        val tasks = dao.getAllTasks().first()
        api.syncTasks(tasks)
    }

    suspend fun fetchAndCacheRemoteTasks() {
        val remoteTasks = api.fetchRemoteTasks()
        remoteTasks.forEach { dao.insert(it) }
    }
}