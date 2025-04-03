package com.example.mvvmcleanmvicompare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvvmcleanmvicompare.clean.presentation.screen.TaskCleanListScreen
import com.example.mvvmcleanmvicompare.clean.presentation.viewmodel.TaskCleanViewModel
import com.example.mvvmcleanmvicompare.mvi.ui.TaskMVIListScreen
import com.example.mvvmcleanmvicompare.mvi.ui.TaskMVIViewModel
import com.example.mvvmcleanmvicompare.mvvm.ui.TaskMVVMListScreen
import com.example.mvvmcleanmvicompare.mvvm.ui.TaskMVVMViewModel
import com.example.mvvmcleanmvicompare.ui.theme.MvvmCleanMviCompareTheme
import dagger.hilt.android.AndroidEntryPoint

enum class Architecture {
    MVVM, MVI, CLEAN
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MvvmCleanMviCompareTheme {

                var selectedArch by remember { mutableStateOf<Architecture?>(Architecture.MVVM) }

                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { selectedArch = Architecture.MVVM },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedArch == Architecture.MVVM) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.secondary
                                }
                            )
                        ) {
                            Text("MVVM")
                        }

                        Button(
                            onClick = { selectedArch = Architecture.MVI },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedArch == Architecture.MVI) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.secondary
                                }
                            )
                        ) {
                            Text("MVI")
                        }

                        Button(
                            onClick = { selectedArch = Architecture.CLEAN },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedArch == Architecture.CLEAN) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.secondary
                                }
                            )
                        ) {
                            Text("Clean")
                        }
                    }

                    Crossfade(
                        targetState = selectedArch,
                        animationSpec = tween(500)
                    ) { arch ->
                        when (arch) {
                            Architecture.MVVM -> {
                                val viewModel: TaskMVVMViewModel = hiltViewModel()
                                TaskMVVMListScreen(viewModel)
                            }

                            Architecture.MVI -> {
                                val viewModel: TaskMVIViewModel = hiltViewModel()
                                TaskMVIListScreen(viewModel)
                            }

                            Architecture.CLEAN -> {
                                val viewModel: TaskCleanViewModel = hiltViewModel()
                                TaskCleanListScreen(
                                    viewModel
                                )
                            }

                            null -> {}
                        }
                    }
                }
            }
        }
    }
}