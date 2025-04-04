package com.example.mvvmcleanmvicompare.mvi.network


import com.example.mvvmcleanmvicompare.mvi.data.TaskMVI
import kotlinx.coroutines.delay

class FakeApiService {

    // Simulate uploading tasks to a remote server
    suspend fun syncTasks(tasks: List<TaskMVI>) {
        delay(1000) // Simulate network latency
        println("Synced ${tasks.size} tasks to the server.")
    }

    // Simulate fetching tasks from a remote server
    suspend fun fetchRemoteTasks(): List<TaskMVI> {
        delay(1000) // Simulate network delay
        return listOf(
            TaskMVI(id = 100, title = "Remote Task A", isDone = false),
            TaskMVI(id = 101, title = "Remote Task B", isDone = true)
        )
    }
}