package com.example.mvvmcleanmvicompare.clean.domain.usecase

import com.example.mvvmcleanmvicompare.clean.domain.model.Task
import com.example.mvvmcleanmvicompare.clean.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.deleteTask(task)
}
