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
}
