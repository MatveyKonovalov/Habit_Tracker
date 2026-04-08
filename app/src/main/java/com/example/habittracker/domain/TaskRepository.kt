package com.example.habittracker.domain

import com.example.habittracker.domain.models.DayStatistics
import com.example.habittracker.domain.models.IncompleteTask
import com.example.habittracker.domain.models.Task

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun completeTask(id: String)
    suspend fun getMaxStreak(): Int
    suspend fun getCurrentStreak(): Int
    suspend fun loadData()

    suspend fun getTaskById(id: String): IncompleteTask
    suspend fun getAllDays(): List<DayStatistics>

    suspend fun deleteTaskById(id: String)
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)

    suspend fun getAllTasks(): List<Task>
}