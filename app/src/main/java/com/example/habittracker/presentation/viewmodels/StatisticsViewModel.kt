package com.example.habittracker.presentation.viewmodels


import androidx.lifecycle.ViewModel
import com.example.habittracker.domain.models.DayStatistics
import com.example.habittracker.domain.models.IncompleteTask
import com.example.habittracker.domain.models.TaskPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor() : ViewModel() {

    private val _statistics = MutableStateFlow<List<DayStatistics>>(emptyList())
    val statistics: StateFlow<List<DayStatistics>> = _statistics.asStateFlow()

    private val _selectedDay = MutableStateFlow<DayStatistics?>(null)
    val selectedDay: StateFlow<DayStatistics?> = _selectedDay.asStateFlow()

    private val _showDetailsDialog = MutableStateFlow(false)
    val showDetailsDialog: StateFlow<Boolean> = _showDetailsDialog.asStateFlow()

    init {
        loadSampleStatistics()
    }

    private fun loadSampleStatistics() {
        _statistics.value = listOf(
            DayStatistics(
                date = "2024-01-15",
                dayOfWeek = "Понедельник",
                completedTasks = 3,
                totalTasks = 5,
                incompleteTasks = listOf(
                    IncompleteTask("1", "Сделать зарядку", TaskPriority.HIGH),
                    IncompleteTask("2", "Выучить Kotlin", TaskPriority.HIGH)
                )
            ),
            DayStatistics(
                date = "2024-01-16",
                dayOfWeek = "Вторник",
                completedTasks = 4,
                totalTasks = 5,
                incompleteTasks = listOf(
                    IncompleteTask("3", "Прочитать книгу", TaskPriority.MEDIUM)
                )
            ),
            DayStatistics(
                date = "2024-01-17",
                dayOfWeek = "Среда",
                completedTasks = 2,
                totalTasks = 5,
                incompleteTasks = listOf(
                    IncompleteTask("1", "Сделать зарядку", TaskPriority.HIGH),
                    IncompleteTask("4", "Помыть посуду", TaskPriority.LOW),
                    IncompleteTask("5", "Прогулка", TaskPriority.MEDIUM)
                )
            ),
            DayStatistics(
                date = "2024-01-18",
                dayOfWeek = "Четверг",
                completedTasks = 5,
                totalTasks = 5,
                incompleteTasks = emptyList()
            ),
            DayStatistics(
                date = "2024-01-19",
                dayOfWeek = "Пятница",
                completedTasks = 3,
                totalTasks = 5,
                incompleteTasks = listOf(
                    IncompleteTask("2", "Выучить Kotlin", TaskPriority.HIGH),
                    IncompleteTask("6", "Позвонить маме", TaskPriority.MEDIUM)
                )
            )
        )
    }

    fun showDayDetails(day: DayStatistics) {
        _selectedDay.value = day
        _showDetailsDialog.value = true
    }

    fun closeDetailsDialog() {
        _selectedDay.value = null
        _showDetailsDialog.value = false
    }
}