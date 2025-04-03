package com.example.mvvmcleanmvicompare.clean.data.repository

import com.example.mvvmcleanmvicompare.clean.data.local.TaskDao
import com.example.mvvmcleanmvicompare.clean.data.local.entity.toDomain
import com.example.mvvmcleanmvicompare.clean.data.local.entity.toEntity
import com.example.mvvmcleanmvicompare.clean.data.remote.FakeApiService
import com.example.mvvmcleanmvicompare.clean.domain.model.Task
import com.example.mvvmcleanmvicompare.clean.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val dao: TaskDao,
    private val api: FakeApiService
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addTask(task: Task) {
        dao.insert(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.delete(task.toEntity())
    }

    override suspend fun toggleTaskDone(task: Task) {
        dao.update(task.copy(isDone = !task.isDone).toEntity())
    }

    override suspend fun syncWithServer() {
        val tasks = dao.getAllTasks().map { it.map { e -> e.toDomain() } }.first()
        api.syncTasks(tasks)
    }

    override suspend fun fetchRemoteTasks() {
        val remoteTasks = api.fetchRemoteTasks()
        remoteTasks.forEach { dao.insert(it.toEntity()) }
    }
}