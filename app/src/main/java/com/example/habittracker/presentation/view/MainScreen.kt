// presentation/MainScreen.kt
package com.example.habittracker.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittracker.presentation.navigation.BottomNavigationBar
import com.example.habittracker.presentation.view.TasksScreen
import com.example.habittracker.presentation.view.screens.HomeScreen
import com.example.habittracker.presentation.view.screens.SettingsScreen
import com.example.habittracker.presentation.view.screens.StatisticsScreen
import com.example.habittracker.presentation.viewmodels.MainAppViewModel
import com.example.habittracker.presentation.viewmodels.NavigationViewModel

@Composable
fun MainScreen(
    mainViewModel: MainAppViewModel = hiltViewModel(),
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val selectedScreen by navigationViewModel.selectedScreen.collectAsStateWithLifecycle()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarHeight)
        ) {
            // Контент (занимает всё доступное место)
            Box(
                modifier = Modifier.weight(1f)
            ) {
                when (selectedScreen) {
                    "home" -> HomeScreen(viewModel = mainViewModel)
                    "tasks" -> TasksScreen(viewModel = mainViewModel)
                    "statistics" -> StatisticsScreen()
                    "settings" -> SettingsScreen()
                }
            }

            // Нижняя навигационная панель
            BottomNavigationBar(
                currentRoute = selectedScreen,
                onItemSelected = { route ->
                    navigationViewModel.selectScreen(route)
                }
            )
        }
    }
}