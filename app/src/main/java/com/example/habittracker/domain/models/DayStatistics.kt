package com.example.habittracker.domain.models

data class DayStatistics(
    val date: String,           // "2024-01-15"
    val dayOfWeek: String,      // "Понедельник"
    val completedTasks: Int,    // Количество выполненных задач
    val totalTasks: Int,        // Общее количество задач
    val incompleteTasks: List<IncompleteTask> // Список невыполненных задач
)