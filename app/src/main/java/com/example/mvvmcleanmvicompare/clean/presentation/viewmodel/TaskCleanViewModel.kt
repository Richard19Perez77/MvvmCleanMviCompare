package com.example.mvvmcleanmvicompare.clean.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmcleanmvicompare.clean.domain.model.Task
import com.example.mvvmcleanmvicompare.clean.domain.usecase.*
import com.example.mvvmcleanmvicompare.clean.presentation.state.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clean architecture allows view model to load initial tasks, uiState reflects repo data.
 *
 *  Architecture
 *      Explicitly segregates use cases and domain logic. State updates are handled through MutableStateFlow and onEach.
 *
 *  Strengths
 *      Promotes testable, modular code by emphasizing the separation of concerns. Encourages the use of use cases for feature-specific logic.
 *
 *  Weaknesses
 *      More verbose than MVVM or MVI, which can increase development overhead.
 *
 *  Suitability
 *      Best for large-scale apps with intricate business logic and requirements for maintainability.
 *
 */
@HiltViewModel
class TaskCleanViewModel @Inject constructor(
    private val getAllTasks: GetAllTasksUseCase,
    private val addTask: AddTaskUseCase,
    private val deleteTask: DeleteTaskUseCase,
    private val toggleTaskCompletion: ToggleTaskCompletionUseCase,
    private val syncTasks: SyncTasksUseCase,
    private val fetchRemoteTasks: FetchRemoteTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState

    init {
        viewModelScope.launch {
            getAllTasks()
                .onEach { tasks -> _uiState.update { it.copy(tasks = tasks) } }
                .launchIn(this)
        }
    }

    fun onAddTask(title: String) = viewModelScope.launch {
        addTask(Task(title = title))
    }

    fun onDeleteTask(task: Task) = viewModelScope.launch {
        deleteTask(task)
    }

    fun onToggleTask(task: Task) = viewModelScope.launch {
        toggleTaskCompletion(task)
    }

    fun onSyncTasks() = viewModelScope.launch {
        syncTasks()
    }

    fun onFetchRemoteTasks() = viewModelScope.launch {
        fetchRemoteTasks()
    }
}
