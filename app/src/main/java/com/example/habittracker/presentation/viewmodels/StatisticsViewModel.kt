package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.TaskRepository
import com.example.habittracker.domain.models.DayStatistics
import com.example.habittracker.domain.models.IncompleteTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val appRepository: TaskRepository
) : ViewModel() {

    private val _statistics = MutableStateFlow<List<DayStatistics>>(emptyList())
    val statistics: StateFlow<List<DayStatistics>> = _statistics.asStateFlow()

    private val _change = MutableStateFlow(0)
    val change = _change.asStateFlow()
    private val _selectedDay = MutableStateFlow<DayStatistics?>(null)
    val selectedDay: StateFlow<DayStatistics?> = _selectedDay.asStateFlow()

    private val _showDetailsDialog = MutableStateFlow(false)
    val showDetailsDialog: StateFlow<Boolean> = _showDetailsDialog.asStateFlow()

    // Кэш задач для быстрого доступа
    private val _tasksCache = MutableStateFlow<Map<String, IncompleteTask>>(emptyMap())
    val tasksCache: StateFlow<Map<String, IncompleteTask>> = _tasksCache.asStateFlow()

    // Состояние загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _statistics.value = appRepository.getAllDays()
                // Загружаем все невыполненные задачи для кэширования
                loadIncompleteTasks()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadIncompleteTasks() {
        val allIncompleteTaskIds = _statistics.value.flatMap { it.incompleteTasks }.distinct()
        val cache = mutableMapOf<String, IncompleteTask>()
        val allCurrentTasks = appRepository.getTasks().map{it.id}.toSet()
        for (taskId in allIncompleteTaskIds) {
            if (taskId in allCurrentTasks){
                cache[taskId] = appRepository.getTaskById(taskId)
            }
        }
        _tasksCache.value = cache
    }

    fun showDayDetails(day: DayStatistics) {
        _selectedDay.value = day
        _showDetailsDialog.value = true
    }

    fun closeDetailsDialog() {
        _selectedDay.value = null
        _showDetailsDialog.value = false
    }


    fun refreshStatistics() {
        loadStatistics()
    }
}