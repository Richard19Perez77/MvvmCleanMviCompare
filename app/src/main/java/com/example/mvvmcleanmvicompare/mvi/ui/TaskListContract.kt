package com.example.mvvmcleanmvicompare.mvi.ui

import com.example.mvvmcleanmvicompare.mvi.data.Task

/**
 * Represents the different events that can occur on the TaskListScreen.
 *
 *  3 part contract
 *      Event
 *      State
 *      Effect
 *
 *  MVI treats a screen like a finite state machine.
 *      The user does something -> that's an event
 *      The app decides what to do -> updates state
 *      Sometimes, it also does a one-time thing -> that's an effect
 */

/**
 * Represents an event on the TaskListScreen.
 *
 *  Define interactions or intents that could happen
 *      Clicks
 *      Button Presses
 *      Init Events
 *
 *  Event - what the user or system does.
 *
 */
sealed class TaskListEvent {
    data object LoadTasks : TaskListEvent()
    data object SyncTasks : TaskListEvent()
    data object FetchRemoteTasks : TaskListEvent()
    data class AddTask(val title: String) : TaskListEvent()
    data class ToggleTask(val task: Task) : TaskListEvent()
    data class DeleteTask(val task: Task) : TaskListEvent()
}

/**
 * Represents the current state of the TaskListScreen.
 *
 *  Immutable snapshot of the screen's data, updated in response to Events.
 *
 *  State - snapshot of the UI at any point.
 *
 */
data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Represents side effects that occur on the TaskListScreen.
 *
 *  One-off side effects.
 *
 *  Effect - something that happens once.
 *
 */
sealed class TaskListEffect {
    data class ShowMessage(val message: String) : TaskListEffect()
}
