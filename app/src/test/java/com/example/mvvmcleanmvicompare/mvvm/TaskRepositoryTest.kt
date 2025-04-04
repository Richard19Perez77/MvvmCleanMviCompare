package com.example.mvvmcleanmvicompare.mvvm

import app.cash.turbine.test
import com.example.mvvmcleanmvicompare.mvvm.data.TaskDao
import com.example.mvvmcleanmvicompare.mvvm.data.TaskMVVM
import com.example.mvvmcleanmvicompare.mvvm.data.TaskRepository
import com.example.mvvmcleanmvicompare.mvvm.network.FakeApiService
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class TaskRepositoryTest {

    private val dao = mockk<TaskDao>(relaxed = true)
    private val api = mockk<FakeApiService>(relaxed = true)
    private lateinit var repo: TaskRepository

    @Before
    fun setup() {
        repo = TaskRepository(dao, api)
    }

    @Test
    fun `allTasks emits list from DAO using Turbine`() = runTest {
        val mockList = listOf(TaskMVVM(id = 1, title = "From DB"))

        every { dao.getAllTasks() } returns flow {
            emit(mockList)
            awaitCancellation()
        }

        // Make sure you're recreating repo after mocking dao
        repo = TaskRepository(dao, api)

        repo.allTasks.test {
            val item = awaitItem()
            assertEquals(mockList, item)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `addTask calls DAO insert`() = runTest {
        val task = TaskMVVM(title = "Add Me")
        repo.addTask(task)
        coVerify { dao.insert(task) }
    }

    @Test
    fun `deleteTask calls DAO delete`() = runTest {
        val task = TaskMVVM(id = 2, title = "Delete Me")
        repo.deleteTask(task)
        coVerify { dao.delete(task) }
    }

    @Test
    fun `toggleTaskDone inverts task isDone and updates DAO`() = runTest {
        val task = TaskMVVM(id = 3, title = "Toggle", isDone = false)
        val expected = task.copy(isDone = true)

        repo.toggleTaskDone(task)
        coVerify { dao.update(expected) }
    }

    @Test
    fun `syncWithServer calls API with one shot DAO tasks`() = runTest {
        val tasks: Flow<List<TaskMVVM>> = flow {
            emit(listOf(TaskMVVM(id = 1, title = "Sync Me"))) // ✅ emit!
        }

        every { dao.getAllTasks() } returns tasks

        repo.syncWithServer()
        val result = tasks.first()
        coVerify { api.syncTasks(result) }
    }

    @Test
    fun `syncWithServer calls API with room like DAO tasks`() = runTest {
        val mockTasks = listOf(TaskMVVM(id = 1, title = "Sync Me"))
        every { dao.getAllTasks() } returns flow {
            emit(mockTasks) // ✅ emits one item
            awaitCancellation() // optional: simulate Room-style long-lived flow
        }

        repo.syncWithServer()

        coVerify { api.syncTasks(mockTasks) }
    }
}
