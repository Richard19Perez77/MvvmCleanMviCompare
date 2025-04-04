package com.example.mvvmcleanmvicompare.mvvm.network

import com.example.mvvmcleanmvicompare.mvvm.data.TaskMVVM
import kotlinx.coroutines.delay

class FakeApiService {

    // Simulate uploading tasks to a remote server
    suspend fun syncTasks(tasks: List<TaskMVVM>) {
        delay(1000) // Simulate network latency
        println("Synced ${tasks.size} tasks to the server.")
    }

    // Simulate fetching tasks from a remote server
    suspend fun fetchRemoteTasks(): List<TaskMVVM> {
        delay(1000) // Simulate network delay
        return listOf(
            TaskMVVM(id = 100, title = "Remote Task A", isDone = false),
            TaskMVVM(id = 101, title = "Remote Task B", isDone = true)
        )
    }
}
