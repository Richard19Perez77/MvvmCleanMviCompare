package com.example.mvvmcleanmvicompare.mvvm.data

import com.example.mvvmcleanmvicompare.mvvm.network.FakeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class TaskRepository(
    private val dao: TaskDao,
    private val api: FakeApiService // Injected
) {
    val allTasks: Flow<List<Task>> = dao.getAllTasks()

    suspend fun addTask(task: Task) = dao.insert(task)

    suspend fun deleteTask(task: Task) = dao.delete(task)

    suspend fun toggleTaskDone(task: Task) =
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
