package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.TaskRepository
import com.example.habittracker.domain.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    private val _maxStrick = MutableStateFlow(0)
    val maxStrick: StateFlow<Int> = _maxStrick.asStateFlow()

    private val _currentStrick = MutableStateFlow(0)
    val currentStrick: StateFlow<Int> = _currentStrick.asStateFlow()

    init {
        loadTasks()
        loadStreaks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getTasks().collect { tasks ->
                _taskList.value = tasks
            }
        }
    }

    fun completeTask(id: String) {
        viewModelScope.launch {
            taskRepository.completeTask(id)
            updateStreaks() // Обновляем серии после выполнения задачи
        }
    }

    private fun loadStreaks() {
        viewModelScope.launch {
            _maxStrick.value = taskRepository.getMaxStreak()
            _currentStrick.value = taskRepository.getCurrentStreak()
        }
    }

    private fun updateStreaks() {
        viewModelScope.launch {
            _currentStrick.value = taskRepository.getCurrentStreak()
            val max = taskRepository.getMaxStreak()
            if (max > _maxStrick.value) {
                _maxStrick.value = max
            }
        }
    }
}