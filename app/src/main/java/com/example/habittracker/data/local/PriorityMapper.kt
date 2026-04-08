package com.example.habittracker.data.local

import androidx.navigationevent.NavigationEventDispatcher
import com.example.habittracker.domain.models.TaskPriority

import javax.inject.Inject

class PriorityMapper @Inject constructor() {
    private companion object{
        const val LOW = 'l'
        const val MEDIUM = 'm'
        const val HIGH = 'h'
    }

    fun toDataBase(data: TaskPriority): Char {
        return when (data) {
            TaskPriority.LOW -> LOW
            TaskPriority.MEDIUM -> MEDIUM
            TaskPriority.HIGH -> HIGH
        }
    }
    fun toTaskPriority(data: Char) = when(data){
        LOW -> TaskPriority.LOW
        MEDIUM -> TaskPriority.MEDIUM
        HIGH -> TaskPriority.HIGH
        else -> throw Exception("Undefined priority")
    }
}