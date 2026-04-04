package com.example.habittracker.domain.models


data class Task(
    val id: Int,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: TaskPriority? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}