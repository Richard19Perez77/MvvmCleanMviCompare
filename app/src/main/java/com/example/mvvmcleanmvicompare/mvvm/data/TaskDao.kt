package com.example.mvvmcleanmvicompare.mvvm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks_mvvm")
    fun getAllTasks(): Flow<List<TaskMVVM>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskMVVM)

    @Delete
    suspend fun delete(task: TaskMVVM)

    @Update
    suspend fun update(task: TaskMVVM)
}