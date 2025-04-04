package com.example.mvvmcleanmvicompare.mvi

import com.example.mvvmcleanmvicompare.mvi.data.TaskMVI
import com.example.mvvmcleanmvicompare.mvi.data.TaskRepository
import com.example.mvvmcleanmvicompare.mvi.ui.TaskListEffect
import com.example.mvvmcleanmvicompare.mvi.ui.TaskListEvent
import com.example.mvvmcleanmvicompare.mvi.ui.TaskMVIViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskMVIViewModelTest {

    private val repo = mockk<TaskRepository>(relaxed = true)
    private lateinit var viewModel: TaskMVIViewModel

    @Before
    fun setup() {
        every { repo.allTasks } returns flowOf(
            listOf(TaskMVI(id = 1, title = "Test"))
        )

        viewModel = TaskMVIViewModel(repo)
    }

    @Test
    fun `LoadTasks updates state with tasks`() = runTest {
        viewModel.onEvent(TaskListEvent.LoadTasks)
        val state = viewModel.state.first()
        assertEquals(1, state.tasks.size)
    }

    @Test
    fun `AddTask calls repository`() = runTest {
        viewModel.onEvent(TaskListEvent.AddTask("Hello"))
        coVerify { repo.addTask(match { it.title == "Hello" }) }
    }

    @Test
    fun `SyncTasks emits effect`() = runTest {
        viewModel.onEvent(TaskListEvent.SyncTasks)
        assertEquals(
            TaskListEffect.ShowMessage("Synced to server"),
            viewModel.effect.first()
        )
    }

    @Test
    fun `DeleteTask calls repository`() = runTest {
        val task = TaskMVI(id = 99, title = "To Delete")
        viewModel.onEvent(TaskListEvent.DeleteTask(task))
        coVerify { repo.deleteTask(task) }
    }

    @Test
    fun `ToggleTask calls repository`() = runTest {
        val task = TaskMVI(id = 42, title = "Toggle Me", isDone = false)
        viewModel.onEvent(TaskListEvent.ToggleTask(task))
        coVerify { repo.toggleTaskDone(task) }
    }

    @Test
    fun `FetchRemoteTasks emits effect`() = runTest {
        viewModel.onEvent(TaskListEvent.FetchRemoteTasks)
        assertEquals(
            TaskListEffect.ShowMessage("Fetched from remote"),
            viewModel.effect.first()
        )
    }
}
