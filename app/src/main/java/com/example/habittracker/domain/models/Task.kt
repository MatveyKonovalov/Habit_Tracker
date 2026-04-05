package com.example.habittracker.domain.models

import kotlin.uuid.Uuid


data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: TaskPriority? = null,
    val createdAt: Long = System.currentTimeMillis()
)

