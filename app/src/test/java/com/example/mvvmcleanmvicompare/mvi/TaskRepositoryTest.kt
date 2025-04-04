package com.example.mvvmcleanmvicompare.mvi

import app.cash.turbine.test
import com.example.mvvmcleanmvicompare.mvi.data.TaskDao
import com.example.mvvmcleanmvicompare.mvi.data.TaskMVI
import com.example.mvvmcleanmvicompare.mvi.data.TaskRepository
import com.example.mvvmcleanmvicompare.mvi.network.FakeApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
    fun `allTasks emits tasks from DAO using Turbine`() = runTest {
        val mockList = listOf(TaskMVI(id = 1, title = "MVI Task"))
        every { dao.getAllTasks() } returns flow {
            emit(mockList)
            awaitCancellation() // simulate Room-style long-lived flow
        }

        repo = TaskRepository(dao, api) // ⚠️ Make sure this happens after mocking!

        repo.allTasks.test {
            val item = awaitItem()
            assertEquals(mockList, item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTask delegates to DAO insert`() = runTest {
        val task = TaskMVI(id = 2, title = "Add Me")
        repo.addTask(task)
        coVerify { dao.insert(task) }
    }

    @Test
    fun `deleteTask delegates to DAO delete`() = runTest {
        val task = TaskMVI(id = 3, title = "Delete Me")
        repo.deleteTask(task)
        coVerify { dao.delete(task) }
    }

    @Test
    fun `toggleTaskDone inverts isDone and calls update`() = runTest {
        val original = TaskMVI(id = 4, title = "Toggle Me", isDone = false)
        val expected = original.copy(isDone = true)
        repo.toggleTaskDone(original)
        coVerify { dao.update(expected) }
    }

    @Test
    fun `syncWithServer fetches from DAO and calls API`() = runTest {
        val localTasks = listOf(TaskMVI(id = 5, title = "Local Task"))
        every { dao.getAllTasks() } returns flowOf(localTasks)

        repo.syncWithServer()

        coVerify { api.syncTasks(localTasks) }
    }

    @Test
    fun `fetchAndCacheRemoteTasks fetches from API and inserts to DAO`() = runTest {
        val remoteTasks = listOf(
            TaskMVI(id = 6, title = "Remote Task 1"),
            TaskMVI(id = 7, title = "Remote Task 2")
        )

        coEvery { api.fetchRemoteTasks() } returns remoteTasks

        repo.fetchAndCacheRemoteTasks()

        remoteTasks.forEach {
            coVerify { dao.insert(it) }
        }
    }
}
