package com.example.habittracker.domain.models

data class IncompleteTask(
    val id: String,
    val title: String,
    val priority: TaskPriority
)