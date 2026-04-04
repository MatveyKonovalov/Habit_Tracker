package com.example.habittracker.data


import com.example.habittracker.domain.models.Task
import com.example.habittracker.domain.models.TaskPriority
import com.example.habittracker.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : TaskRepository {
    private var tasks = listOf(
        Task(1, "Сделать зарядку", "Утренняя зарядка 15 минут", false, TaskPriority.HIGH),
        Task(2, "Прочитать книгу", "30 минут чтения", false, TaskPriority.MEDIUM),
        Task(3, "Выучить урок", "Kotlin Flow", false, TaskPriority.HIGH),
        Task(4, "Помыть посуду", "", true, TaskPriority.LOW),
        Task(5, "Прогулка", "30 минут на свежем воздухе", false, TaskPriority.MEDIUM)
    )

    override fun getTasks(): Flow<List<Task>> = flow {
        emit(tasks)
    }

    override suspend fun completeTask(id: Int) {
        tasks = tasks.map { task ->
            if (task.id == id) task.copy(isCompleted = true)
            else task
        }
    }

    override suspend fun getMaxStreak(): Int {
        // Логика подсчета максимальной серии
        return tasks.count { it.isCompleted }
    }

    override suspend fun getCurrentStreak(): Int {
        // Логика подсчета текущей серии
        return tasks.count { it.isCompleted }
    }
}