// presentation/screens/HomeScreen.kt
package com.example.habittracker.presentation.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.presentation.TaskList
import com.example.habittracker.presentation.viewmodels.MainAppViewModel

@Composable
fun HomeScreen(
    viewModel: MainAppViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.padding(horizontal = 16.dp)
        ) {
            ShowStaticStrick(viewModel = viewModel)
            Spacer(modifier = Modifier.height(8.dp))
            TaskList(viewModel = viewModel)
        }
    }
}