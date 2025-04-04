package com.example.mvvmcleanmvicompare.clean

import com.example.mvvmcleanmvicompare.clean.domain.model.Task
import com.example.mvvmcleanmvicompare.clean.domain.usecase.AddTaskUseCase
import com.example.mvvmcleanmvicompare.clean.domain.usecase.DeleteTaskUseCase
import com.example.mvvmcleanmvicompare.clean.domain.usecase.FetchRemoteTasksUseCase
import com.example.mvvmcleanmvicompare.clean.domain.usecase.GetAllTasksUseCase
import com.example.mvvmcleanmvicompare.clean.domain.usecase.SyncTasksUseCase
import com.example.mvvmcleanmvicompare.clean.domain.usecase.ToggleTaskCompletionUseCase
import com.example.mvvmcleanmvicompare.clean.presentation.viewmodel.TaskCleanViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Clean architecture allows view model to load initial tasks, uiState reflects repo data.
 * Calling onAddTask(...) invokes the corresponding use case with correct input.
 *
 *  1. ui state verification without touching repo's
 *      in mvvm you can only have a Flow<List<Task>> no full uiState object to assert.
 *      in mvi you can assert state, but view model still handles logic
 *
 *  2. verify one responsibility at a time
 *      you test only the view model's orchestration, not logic
 *      logic is verified separately inside AddTaskUseCaseTest, etc.
 *
 *  3. mocking use cases instead of repo's
 *      mocking TaskRepository in mvvm, mvi covers too much
 *      in clean you mock only the small targeted use case being exercised
 *
 *  summary
 *      clean view model tests are stronger because it separates orchestration from logic
 *      you can test all 6 view model functions easily with use case mocks
 *      harder or impossible in MVVM w/o verifying internals or dealing with raw flows
 *      clean gives you narrower, more precise tests
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TaskCleanViewModelTest {

    private val getAllTasks = mockk<GetAllTasksUseCase>()
    private val addTask = mockk<AddTaskUseCase>(relaxed = true)
    private val deleteTask = mockk<DeleteTaskUseCase>(relaxed = true)
    private val toggleTask = mockk<ToggleTaskCompletionUseCase>(relaxed = true)
    private val sync = mockk<SyncTasksUseCase>(relaxed = true)
    private val fetch = mockk<FetchRemoteTasksUseCase>(relaxed = true)

    private lateinit var viewModel: TaskCleanViewModel

    @Before
    fun setup() {
        every { getAllTasks() } returns flowOf(
            listOf(Task(id = 1, title = "Test", isDone = false))
        )

        viewModel = TaskCleanViewModel(
            getAllTasks, addTask, deleteTask, toggleTask, sync, fetch
        )
    }

    @Test
    fun `uiState should load tasks`() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(1, state.tasks.size)
        assertEquals("Test", state.tasks.first().title)
    }

    @Test
    fun `onAddTask should call use case`() = runTest {
        viewModel.onAddTask("My Task")
        coVerify { addTask(match { it.title == "My Task" }) }
    }

    @Test
    fun `onDeleteTask should call use case`() = runTest {
        val task = Task(id = 5, title = "Delete Me")
        viewModel.onDeleteTask(task)
        coVerify { deleteTask(task) }
    }

    @Test
    fun `onToggleTask should call use case`() = runTest {
        val task = Task(id = 7, title = "Toggle", isDone = false)
        viewModel.onToggleTask(task)
        coVerify { toggleTask(task) }
    }

    @Test
    fun `onSyncTasks should call use case`() = runTest {
        viewModel.onSyncTasks()
        coVerify { sync() }
    }

    @Test
    fun `onFetchRemoteTasks should call use case`() = runTest {
        viewModel.onFetchRemoteTasks()
        coVerify { fetch() }
    }
}
