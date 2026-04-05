package com.example.habittracker.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.habittracker.R

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : Screen(
        route = "home",
        title = "Главная",
        icon = Icons.Default.Home,
        selectedIcon = Icons.Default.Home
    )

    object Tasks : Screen(
        route = "tasks",
        title = "Задачи",
        icon = Icons.Default.DateRange,
        selectedIcon = Icons.Default.DateRange
    )

    object Statistics : Screen(
        route = "statistics",
        title = "Статистика",
        icon = Icons.Default.Edit,
        selectedIcon = Icons.Default.Edit
    )

    object Settings : Screen(
        route = "settings",
        title = "Настройки",
        icon = Icons.Default.Settings,
        selectedIcon = Icons.Default.Settings
    )

    companion object {
        val screens = listOf(Home, Tasks, Statistics, Settings)
    }
}