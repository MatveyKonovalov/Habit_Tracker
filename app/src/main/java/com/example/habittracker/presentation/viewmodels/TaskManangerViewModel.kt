
package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.TaskRepository
import com.example.habittracker.domain.models.Task
import com.example.habittracker.domain.models.TaskPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid.Companion.random

@HiltViewModel
class TaskManagerViewModel @Inject constructor(
    private val appRepository: TaskRepository
) : ViewModel() {
    private val _change = MutableStateFlow(0)
    val change = _change.asStateFlow()
    private val _tasksView = MutableStateFlow(emptyList<Task>())
    val tasksView = _tasksView.asStateFlow()


    private val _maxStrick = MutableStateFlow(0)
    val maxStrick: StateFlow<Int> = _maxStrick.asStateFlow()

    private val _currentStrick = MutableStateFlow(0)
    val currentStrick: StateFlow<Int> = _currentStrick.asStateFlow()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private var _editingTask: Task? = null

    fun loadTodayTasks(){
        viewModelScope.launch {
            appRepository.loadData()
            _tasks.value = appRepository.getTasks()
        }

    }
    fun loadSampleTasks() {
        viewModelScope.launch{
            _tasksView.value = appRepository.getAllTasks()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addTask(title: String, description: String, priority: TaskPriority) {
        val task = Task(
            id = random().toString(),
            title = title,
            description = description,
            isCompleted = false,
            priority = priority)
        _tasks.value += task
        viewModelScope.launch{
            appRepository.addTask(task)
        }
        _change.value += 1
        closeDialog()

    }

    fun updateTask(id: String, title: String, description: String, priority: TaskPriority) {
        val task = Task(
            id = id,
            title = title,
            description = description,
            isCompleted = false,
            priority = priority,
        )
        viewModelScope.launch {
            appRepository.updateTask(task)
        }

        _change.value += 1
        closeDialog()
    }

    fun deleteTask(id: String) {
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                _tasks.value = _tasks.value.filter{it.id != id}
                appRepository.deleteTaskById(id)
            }
        }
        _change.value += 1
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

    private fun loadStreaks() {
        viewModelScope.launch {
            _maxStrick.value = appRepository.getMaxStreak()
            _currentStrick.value = appRepository.getCurrentStreak()
        }
    }

    private fun updateStreaks() {
        viewModelScope.launch {
            _currentStrick.value = appRepository.getCurrentStreak()
            val max = appRepository.getMaxStreak()
            if (max > _maxStrick.value) {
                _maxStrick.value = max
            }
        }
    }
    fun completeTask(id: String){
        viewModelScope.launch {
            _tasks.value = _tasks.value.map {task ->
                if (task.id == id){
                    task.copy(isCompleted = !task.isCompleted)
                } else task
            }
            _change.value += 1
            withContext(Dispatchers.IO){
                appRepository.completeTask(id)
            }


        }
    }
}