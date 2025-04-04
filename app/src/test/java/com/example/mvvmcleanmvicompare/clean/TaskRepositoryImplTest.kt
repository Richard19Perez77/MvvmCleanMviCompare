package com.example.mvvmcleanmvicompare.clean

import app.cash.turbine.test
import com.example.mvvmcleanmvicompare.clean.data.local.TaskDao
import com.example.mvvmcleanmvicompare.clean.data.local.entity.TaskClean
import com.example.mvvmcleanmvicompare.clean.data.remote.FakeApiService
import com.example.mvvmcleanmvicompare.clean.data.repository.TaskRepositoryImpl
import com.example.mvvmcleanmvicompare.clean.domain.model.Task
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

class TaskRepositoryImplTest {

    private val dao = mockk<TaskDao>(relaxed = true)
    private val api = mockk<FakeApiService>(relaxed = true)
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setup() {
        repository = TaskRepositoryImpl(dao, api)
    }

    @Test
    fun `getAllTasks emits mapped domain tasks from DAO using Turbine`() = runTest {
        val mockEntityList = listOf(TaskClean(id = 1, title = "Clean DB"))
        every { dao.getAllTasks() } returns flow {
            emit(mockEntityList)
            awaitCancellation()
        }

        repository.getAllTasks().test {
            val item = awaitItem()
            assertEquals("Clean DB", item.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTask inserts entity to DAO`() = runTest {
        val domainTask = Task(id = 1, title = "Add Me", isDone = false)
        repository.addTask(domainTask)
        coVerify { dao.insert(TaskClean(id = 1, title = "Add Me", isDone = false)) }
    }

    @Test
    fun `deleteTask calls DAO delete`() = runTest {
        val domainTask = Task(id = 2, title = "Remove Me", isDone = true)
        repository.deleteTask(domainTask)
        coVerify { dao.delete(TaskClean(id = 2, title = "Remove Me", isDone = true)) }
    }

    @Test
    fun `toggleTask updates isDone field and calls DAO update`() = runTest {
        val domainTask = Task(id = 3, title = "Toggle Me", isDone = false)
        val expected = TaskClean(id = 3, title = "Toggle Me", isDone = true)
        repository.toggleTaskDone(domainTask)
        coVerify { dao.update(expected) }
    }

    @Test
    fun `syncTasks sends mapped domain tasks to API`() = runTest {
        val localEntities = listOf(TaskClean(id = 4, title = "Local", isDone = false))
        every { dao.getAllTasks() } returns flowOf(localEntities)

        repository.syncWithServer()

        val expected = listOf(Task(id = 4, title = "Local", isDone = false))
        coVerify { api.syncTasks(expected) }
    }

    @Test
    fun `fetchRemoteTasks inserts converted tasks from API into DAO`() = runTest {
        val remoteTasks = listOf(Task(id = 5, title = "Remote", isDone = true))
        coEvery { api.fetchRemoteTasks() } returns remoteTasks

        repository.fetchRemoteTasks()

        coVerify { dao.insert(TaskClean(id = 5, title = "Remote", isDone = true)) }
    }
}
