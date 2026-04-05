package com.example.habittracker.domain.models

data class Day(
    val tasksComplete: List<Task>,
    val data: String,
)