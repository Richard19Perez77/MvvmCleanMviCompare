package com.example.mvvmcleanmvicompare.mvvm.network

import com.example.mvvmcleanmvicompare.mvvm.data.Task
import kotlinx.coroutines.delay

class FakeApiService {

    // Simulate uploading tasks to a remote server
    suspend fun syncTasks(tasks: List<Task>) {
        delay(1000) // Simulate network latency
        println("Synced ${tasks.size} tasks to the server.")
    }

    // Simulate fetching tasks from a remote server
    suspend fun fetchRemoteTasks(): List<Task> {
        delay(1000) // Simulate network delay
        return listOf(
            Task(id = 100, title = "Remote Task A", isDone = false),
            Task(id = 101, title = "Remote Task B", isDone = true)
        )
    }
}
