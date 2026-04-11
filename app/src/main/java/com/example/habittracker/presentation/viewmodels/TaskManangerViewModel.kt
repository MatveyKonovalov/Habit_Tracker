
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
    private val _tasksTodayComplete= MutableStateFlow(emptyList<Task>())
    val tasksTodayComplete= _tasksTodayComplete.asStateFlow()


    private val _maxStrick = MutableStateFlow(0)
    val maxStrick: StateFlow<Int> = _maxStrick.asStateFlow()

    private val _currentStrick = MutableStateFlow(0)
    val currentStrick: StateFlow<Int> = _currentStrick.asStateFlow()
    private val _tasksAll = MutableStateFlow<List<Task>>(emptyList())
    val tasksAll: StateFlow<List<Task>> = _tasksAll.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private var _editingTask: Task? = null
    private fun makeChange(){
        _change.value ++
    }
    fun loadTodayTasks(){
        viewModelScope.launch {
            appRepository.loadData()
            _tasksTodayComplete.value = appRepository.getTasks()
            loadStreaks()
            makeChange()
        }

    }
    fun loadSampleTasks() {
        viewModelScope.launch{
            _tasksAll.value = appRepository.getAllTasks()
            makeChange()
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
        _tasksAll.value += task
        viewModelScope.launch{
            appRepository.addTask(task)
            makeChange()
        }
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
            makeChange()
        }


        closeDialog()
    }

    fun deleteTask(id: String) {
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                _tasksAll.value = _tasksAll.value.filter{it.id != id}
                appRepository.deleteTaskById(id)
            }
            makeChange()
            loadStreaks()
        }

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

    fun loadStreaks() {
        viewModelScope.launch {
            _currentStrick.value = appRepository.getCurrentStreak()
        }
        viewModelScope.launch {
            _maxStrick.value = appRepository.getMaxStreak()
        }

    }
    fun completeTask(id: String){
        viewModelScope.launch {
            _tasksTodayComplete.value = _tasksTodayComplete.value.map {task ->
                if (task.id == id){
                    task.copy(isCompleted = !task.isCompleted)
                } else task
            }

            withContext(Dispatchers.IO){
                appRepository.completeTask(id)
            }
            loadStreaks()
            makeChange()
        }

    }
}