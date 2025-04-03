package com.example.mvvmcleanmvicompare.clean.data.remote

import com.example.mvvmcleanmvicompare.clean.domain.model.Task
import kotlinx.coroutines.delay

class FakeApiService {

    suspend fun syncTasks(tasks: List<Task>) {
        delay(1000)
        println("Synced ${tasks.size} tasks to server.")
    }

    suspend fun fetchRemoteTasks(): List<Task> {
        delay(1000)
        return listOf(
            Task(id = 1000, title = "Remote Task 1", isDone = false),
            Task(id = 1001, title = "Remote Task 2", isDone = true)
        )
    }
}
