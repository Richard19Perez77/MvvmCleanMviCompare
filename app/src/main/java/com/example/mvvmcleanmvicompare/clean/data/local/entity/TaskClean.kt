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

/**
 * TaskClean will have conversion methods to call for converting from Task to TaskClean and vice versa.
 *
 *  Why?
 *      Clean separates architecture layers
 *      Data  - TaskClean (@Entity) for Room and DB only
 *      Domain - Task for core business logic
 *      Presentation TaskUiState for UI only view state
 *
 *      Because each layer is independent, we need mappers to convert between them.
 *      // Data -> Domain
 *      fun TaskClean.toDomain(): Task = Task(id = id, title = title, isDone = isDone)
 *
 *      // Domain -> Data
 *      fun Task.toEntity(): TaskClean = TaskClean(id = id, title = title, isDone = isDone)
 *
 *      This keeps each layer clean, reusable, and testable - no db imports in domain layer, and domain is database agnostic.
 */

fun TaskClean.toDomain(): Task = Task(id = id, title = title, isDone = isDone)

fun Task.toEntity(): TaskClean = TaskClean(id = id, title = title, isDone = isDone)