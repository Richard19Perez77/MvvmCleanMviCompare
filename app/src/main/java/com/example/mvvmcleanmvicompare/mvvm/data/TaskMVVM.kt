package com.example.mvvmcleanmvicompare.mvvm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_mvvm")
data class TaskMVVM(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isDone: Boolean = false
)
