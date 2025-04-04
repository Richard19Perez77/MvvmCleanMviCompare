package com.example.mvvmcleanmvicompare.clean.data.local

import androidx.room.*
import com.example.mvvmcleanmvicompare.clean.data.local.entity.TaskClean
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks_clean")
    fun getAllTasks(): Flow<List<TaskClean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskClean)

    @Delete
    suspend fun delete(task: TaskClean)

    @Update
    suspend fun update(task: TaskClean)
}
