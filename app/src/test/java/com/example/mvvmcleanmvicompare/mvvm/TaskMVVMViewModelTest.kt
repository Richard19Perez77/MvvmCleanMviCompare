package com.example.mvvmcleanmvicompare.mvvm

import app.cash.turbine.test
import com.example.mvvmcleanmvicompare.mvvm.data.TaskMVVM
import com.example.mvvmcleanmvicompare.mvvm.data.TaskRepository
import com.example.mvvmcleanmvicompare.mvvm.ui.TaskMVVMViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Testing MVVM View Model involves dependencies with TaskRepository, has business logic inside the view model, has a UI State that exposes as raw list a StateFlow<List<Task>>, has view model and repo level granularity and all tests mock the task repo, it's logic reusability is hard.
 *
 * The view model responsibility is high and over long-term there is risk of bloat.
 *
 * In mvvm the view model does everything, holds state, calls the repo, understands what a task is, and contains all logic.
 *
 * If you improve this in clean teh view model just calls use cases, if toggle logic changes, it's in ToggleTaskCompletionUseCase and the view model tests don't care how things work, only that they're triggered correctly.
 *
 * Needs:
 *  mvvm needs to be able to test logic inside of sync, clean can mock a use case
 *  use case reused in cli, not what mvvm is designed for, and clean can cleanly decouple it.
 *
 * mvvm can be fine if its a small app, with no need for complex reuse or scaling and you want fewer classes and quick delivery.
 *
 *
 *
 */
class TaskMVVMViewModelTest {

    private val repo = mockk<TaskRepository>(relaxed = true)
    private lateinit var viewModel: TaskMVVMViewModel

    @Before
    fun setup() {
        every { repo.allTasks } returns flowOf(
            listOf(TaskMVVM(id = 1, title = "Hello"))
        )
        viewModel = TaskMVVMViewModel(repo)
    }

    @Test
    fun `tasks turbine 2 emits values from repository`() = runTest {
        viewModel.tasks.test {
            // First emission (initial empty list)
            assertEquals(emptyList<TaskMVVM>(), awaitItem())

            // Second emission (your mock data)
            val tasks = awaitItem()
            assertEquals(1, tasks.size)
            assertEquals("Hello", tasks.first().title)

            // Cancel to avoid infinite waiting
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTask calls repository`() = runTest {
        viewModel.addTask("Test Title")
        coVerify { repo.addTask(match { it.title == "Test Title" }) }
    }

    @Test
    fun `deleteTask calls repository`() = runTest {
        val task = TaskMVVM(id = 2, title = "Delete")
        viewModel.deleteTask(task)
        coVerify { repo.deleteTask(task) }
    }

    @Test
    fun `toggleTask calls repository`() = runTest {
        val task = TaskMVVM(id = 3, title = "Toggle", isDone = false)
        viewModel.toggleTask(task)
        coVerify { repo.toggleTaskDone(task) }
    }

    @Test
    fun `syncToServer calls repository`() = runTest {
        viewModel.syncToServer()
        coVerify { repo.syncWithServer() }
    }

    @Test
    fun `loadRemoteTasks calls repository`() = runTest {
        viewModel.loadRemoteTasks()
        coVerify { repo.fetchAndCacheRemoteTasks() }
    }
}
