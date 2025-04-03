package com.example.mvvmcleanmvicompare.clean.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.mvvmcleanmvicompare.clean.presentation.viewmodel.TaskCleanViewModel

@Composable
fun TaskCleanListScreen(viewModel: TaskCleanViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                viewModel.onSyncTasks()
                Toast.makeText(context, "Synced to Server", Toast.LENGTH_SHORT).show()
            }) {
                Text("Sync")
            }

            Button(onClick = {
                viewModel.onFetchRemoteTasks()
                Toast.makeText(context, "Loaded Remote Tasks", Toast.LENGTH_SHORT).show()
            }) {
                Text("Load Remote")
            }

            Button(onClick = {
                val id = System.currentTimeMillis()
                viewModel.onAddTask("Task id: $id")
            }) {
                Text("Add Task")
            }
        }

        LazyColumn {
            items(state.tasks) { task ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onToggleTask(task) }
                        .padding(8.dp)
                ) {
                    Text(
                        text = task.title,
                        style = if (task.isDone)
                            TextStyle(textDecoration = TextDecoration.LineThrough)
                        else
                            LocalTextStyle.current
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.onDeleteTask(task) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
