// presentation/screens/TasksScreen.kt
package com.example.habittracker.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.presentation.TaskList
import com.example.habittracker.presentation.viewmodels.MainAppViewModel

@Composable
fun TasksScreen(
    viewModel: MainAppViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Управление задачами",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            TaskList(viewModel = viewModel)
        }
    }
}