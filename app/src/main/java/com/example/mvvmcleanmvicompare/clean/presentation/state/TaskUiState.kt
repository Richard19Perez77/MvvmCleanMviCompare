package com.example.mvvmcleanmvicompare.clean.presentation.state

import com.example.mvvmcleanmvicompare.clean.domain.model.Task

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)
