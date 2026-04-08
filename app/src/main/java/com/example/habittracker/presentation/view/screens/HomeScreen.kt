
package com.example.habittracker.presentation.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.presentation.view.TaskList
import com.example.habittracker.presentation.view.ShowStaticStrick
import com.example.habittracker.presentation.viewmodels.TaskManagerViewModel

@Composable
fun HomeScreen(
    viewModel: TaskManagerViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            ShowStaticStrick(viewModel = viewModel)
            Spacer(modifier = Modifier.height(8.dp))
            TaskList(taskManagerViewModel = viewModel)
        }
    }
}