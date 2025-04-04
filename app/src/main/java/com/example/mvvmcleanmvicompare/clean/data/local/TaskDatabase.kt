package com.example.mvvmcleanmvicompare.clean.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvmcleanmvicompare.clean.data.local.entity.TaskClean

@Database(entities = [TaskClean::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "clean_task_db"
                ).build().also { INSTANCE = it }
            }
    }
}
