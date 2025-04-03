package com.example.mvvmcleanmvicompare.mvi.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun TaskMVIListScreen(viewModel: TaskMVIViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TaskListEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                viewModel.onEvent(TaskListEvent.SyncTasks)
            }) {
                Text("Sync")
            }

            Button(onClick = {
                viewModel.onEvent(TaskListEvent.FetchRemoteTasks)
            }) {
                Text("Load Remote")
            }

            Button(onClick = {
                val id = System.currentTimeMillis()
                viewModel.onEvent(TaskListEvent.AddTask("Task id: $id"))
            }) {
                Text("Add Task")
            }
        }

        LazyColumn {
            items(state.tasks) { task ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(TaskListEvent.ToggleTask(task))
                        }
                        .padding(8.dp)
                ) {
                    Text(
                        text = task.title,
                        style = if (task.isDone) {
                            TextStyle(textDecoration = TextDecoration.LineThrough)
                        } else {
                            LocalTextStyle.current
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        viewModel.onEvent(TaskListEvent.DeleteTask(task))
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
