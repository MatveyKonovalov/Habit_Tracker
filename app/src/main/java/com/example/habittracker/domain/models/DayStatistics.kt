package com.example.habittracker.domain.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek


data class DayStatistics(
    val date: String,
    val dayOfWeek: DayOfWeek,
    var completedTasks: Int,    // Количество выполненных задач
    var totalTasks: Int,        // Общее количество задач
    var incompleteTasks: List<String> // Список невыполненных задач
) {
    val dayOfWeekRussian: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Понедельник"
            DayOfWeek.TUESDAY -> "Вторник"
            DayOfWeek.WEDNESDAY -> "Среда"
            DayOfWeek.THURSDAY -> "Четверг"
            DayOfWeek.FRIDAY -> "Пятница"
            DayOfWeek.SATURDAY -> "Суббота"
            DayOfWeek.SUNDAY -> "Воскресенье"
        }
}