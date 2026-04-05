// presentation/viewmodels/TaskManagerViewModel.kt
package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.models.Task
import com.example.habittracker.domain.models.TaskPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class TaskManagerViewModel @Inject constructor() : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private var _editingTask: Task? = null

    init {
        loadSampleTasks()
    }

    private fun loadSampleTasks() {
        _tasks.value = listOf(
            Task(
                id = "1",
                title = "Сделать зарядку",
                description = "Утренняя зарядка 15 минут",
                priority = TaskPriority.HIGH
            ),
            Task(
                id = "2",
                title = "Прочитать книгу",
                description = "30 минут чтения",
                priority = TaskPriority.MEDIUM
            ),
            Task(
                id = "3",
                title = "Выучить Kotlin",
                description = "Изучить Flow и Coroutines",
                priority = TaskPriority.HIGH
            ),
            Task(
                id = "4",
                title = "Прогулка",
                description = "30 минут на свежем воздухе",
                priority = TaskPriority.LOW
            )
        )
    }

    fun addTask(title: String, description: String, priority: TaskPriority) {
        val newTask = Task(
            title = title,
            description = description,
            priority = priority,
            id = "1"
        )
        _tasks.value = _tasks.value + newTask
        closeDialog()
    }

    fun updateTask(id: String, title: String, description: String, priority: TaskPriority) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == id) {
                task.copy(
                    title = title,
                    description = description,
                    priority = priority
                )
            } else task
        }
        closeDialog()
    }

    fun deleteTask(id: String) {
        _tasks.value = _tasks.value.filter { it.id != id }
    }

    fun openAddDialog() {
        _editingTask = null
        _showDialog.value = true
    }

    fun openEditDialog(task: Task) {
        _editingTask = task
        _showDialog.value = true
    }

    fun closeDialog() {
        _editingTask = null
        _showDialog.value = false
    }

    fun getEditingTask(): Task? = _editingTask
}