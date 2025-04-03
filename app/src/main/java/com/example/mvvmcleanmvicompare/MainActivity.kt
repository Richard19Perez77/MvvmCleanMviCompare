package com.example.mvvmcleanmvicompare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
        enableEdgeToEdge()
        setContent {
            MvvmCleanMviCompareTheme {
                val selectedArch by remember { mutableStateOf<Architecture?>(Architecture.MVVM) }

                Crossfade(targetState = selectedArch) { arch ->
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
