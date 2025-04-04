package com.example.mvvmcleanmvicompare.clean.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmcleanmvicompare.clean.domain.model.Task

@Entity(tableName = "tasks_clean")
data class TaskClean(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isDone: Boolean = false
)

fun TaskClean.toDomain(): Task = Task(id = id, title = title, isDone = isDone)

fun Task.toEntity(): TaskClean = TaskClean(id = id, title = title, isDone = isDone)