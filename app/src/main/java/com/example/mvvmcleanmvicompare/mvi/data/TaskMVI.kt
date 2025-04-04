package com.example.mvvmcleanmvicompare.mvi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_mvi")
data class TaskMVI(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isDone: Boolean = false
)
