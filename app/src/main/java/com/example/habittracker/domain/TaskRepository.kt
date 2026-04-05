package com.example.habittracker.domain

import com.example.habittracker.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun completeTask(id: String)
    suspend fun getMaxStreak(): Int
    suspend fun getCurrentStreak(): Int
}