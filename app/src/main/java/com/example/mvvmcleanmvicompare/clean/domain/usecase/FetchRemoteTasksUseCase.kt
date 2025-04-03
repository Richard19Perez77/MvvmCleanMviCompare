package com.example.mvvmcleanmvicompare.clean.domain.usecase

import com.example.mvvmcleanmvicompare.clean.domain.repository.TaskRepository

class FetchRemoteTasksUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.fetchRemoteTasks()
}
