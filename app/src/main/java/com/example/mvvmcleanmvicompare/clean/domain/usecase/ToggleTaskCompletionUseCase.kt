package com.example.mvvmcleanmvicompare.clean.domain.usecase

import com.example.mvvmcleanmvicompare.clean.domain.model.Task
import com.example.mvvmcleanmvicompare.clean.domain.repository.TaskRepository

class ToggleTaskCompletionUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.toggleTaskDone(task)
}
