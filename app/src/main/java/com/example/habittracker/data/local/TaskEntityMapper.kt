package com.example.habittracker.data.local

import com.example.habittracker.domain.models.Task
import com.example.habittracker.domain.models.TaskPriority
import javax.inject.Inject

class TaskEntityMapper @Inject constructor(
    private val priorityMapper: PriorityMapper
){
    fun toDomain(taskEntity: TaskEntity, complete: Boolean = false) = Task(
        id = taskEntity.idTask,
        title = taskEntity.title,
        description = taskEntity.description,
        isCompleted = complete,
        priority = priorityMapper.toTaskPriority(taskEntity.priority),
        createdAt = taskEntity.createdAt
    )
    fun toDataBase(task: Task) = TaskEntity(
        idTask = task.id,
        title = task.title,
        description = task.description,
        priority = priorityMapper.toDataBase(task.priority ?: TaskPriority.LOW),
        createdAt = task.createdAt
    )
}