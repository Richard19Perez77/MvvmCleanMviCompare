package com.example.mvvmcleanmvicompare.mvi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmcleanmvicompare.mvi.data.TaskMVI
import com.example.mvvmcleanmvicompare.mvi.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  Architecture
 *      Follows the Unidirectional Data Flow (UDF) pattern. Events trigger actions, effects capture side-effects, and states are updated after processing.
 *
 *  Strengths
 *      Better suited for complex UI interactions. The Channel ensures proper handling of side effects or one-time effects.
 *
 *  Weaknesses
 *      Slightly more verbose and complex due to event and effect management.
 *
 *  Suitability
 *      Ideal for applications where predictable state transitions and one-time events are crucial.
 */
@HiltViewModel
class TaskMVIViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state

    private val _effect = Channel<TaskListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(TaskListEvent.LoadTasks)
    }

    fun onEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.LoadTasks -> {
                viewModelScope.launch {
                    repository.allTasks.collect {
                        _state.value = _state.value.copy(tasks = it)
                    }
                }
            }
            is TaskListEvent.AddTask -> {
                viewModelScope.launch {
                    repository.addTask(TaskMVI(title = event.title))
                }
            }
            is TaskListEvent.DeleteTask -> {
                viewModelScope.launch {
                    repository.deleteTask(event.task)
                }
            }
            is TaskListEvent.ToggleTask -> {
                viewModelScope.launch {
                    repository.toggleTaskDone(event.task)
                }
            }
            is TaskListEvent.SyncTasks -> {
                viewModelScope.launch {
                    repository.syncWithServer()
                    _effect.send(TaskListEffect.ShowMessage("Synced to server"))
                }
            }
            is TaskListEvent.FetchRemoteTasks -> {
                viewModelScope.launch {
                    repository.fetchAndCacheRemoteTasks()
                    _effect.send(TaskListEffect.ShowMessage("Fetched from remote"))
                }
            }
        }
    }
}
