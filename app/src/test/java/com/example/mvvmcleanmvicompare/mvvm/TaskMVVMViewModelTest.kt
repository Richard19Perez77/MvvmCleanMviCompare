package com.example.mvvmcleanmvicompare.mvvm

import com.example.mvvmcleanmvicompare.mvvm.data.TaskMVVM
import com.example.mvvmcleanmvicompare.mvvm.data.TaskRepository
import com.example.mvvmcleanmvicompare.mvvm.ui.TaskMVVMViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `tasks emits values from repository`() = runTest {
        val collected = mutableListOf<List<TaskMVVM>>()
        val job = launch {
            viewModel.tasks.collect {
                collected.add(it)
            }
        }

        advanceUntilIdle() // Wait for emissions

        assertEquals(1, collected.first().size)
        assertEquals("Hello", collected.first().first().title)

        job.cancel()
    }


    @Test
    fun `addTask calls repository`() = runTest {
        viewModel.addTask("Test Title")
        coVerify { repo.addTask(match { it.title == "Test Title" }) }
    }
}
