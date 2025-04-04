package com.example.mvvmcleanmvicompare.mvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmcleanmvicompare.mvvm.data.TaskMVVM
import com.example.mvvmcleanmvicompare.mvvm.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MVVM ViewModel for managing tasks.
 *
 *  Architecture
 *      Uses stateIn to share state across observers. Focuses on reactive flows for observing allTasks.
 *
 *  Strengths
 *      It is simpler to implement and requires less boilerplate code than other approaches.
 *
 *  Weaknesses
 *      Managing side-effects or more complex UI interactions can become challenging without dedicated handling.
 *
 *  Suitability
 *      Best for projects requiring a clean, minimal implementation with limited UI complexity.
 *
 */
@HiltViewModel
class TaskMVVMViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks = repository.allTasks.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTask(title: String) {
        viewModelScope.launch {
            repository.addTask(TaskMVVM(title = title))
        }
    }

    fun deleteTask(task: TaskMVVM) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun toggleTask(task: TaskMVVM) {
        viewModelScope.launch {
            repository.toggleTaskDone(task)
        }
    }

    fun syncToServer() {
        viewModelScope.launch {
            repository.syncWithServer()
        }
    }

    fun loadRemoteTasks() {
        viewModelScope.launch {
            repository.fetchAndCacheRemoteTasks()
        }
    }
}
