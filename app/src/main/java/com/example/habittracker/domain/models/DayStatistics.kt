package com.example.habittracker.domain.models

import java.time.DayOfWeek


data class DayStatistics(
    val date: String,           // "2024-01-15"
    val dayOfWeek: DayOfWeek,      // "Понедельник"
    var completedTasks: Int,    // Количество выполненных задач
    var totalTasks: Int,        // Общее количество задач
    var incompleteTasks: List<String> // Список невыполненных задач
)