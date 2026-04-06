package com.example.habittracker.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.habittracker.domain.models.DayStatistics
import com.example.habittracker.presentation.navigation.Screen
import javax.inject.Inject

class DayEntityMapper @Inject constructor(
    private val incompleteTasksMapper: IncompleteTasksMapper,
    private val dayOfWeekMapper: DayOfWeekMapper
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(dayEntity: DayEntity) = DayStatistics(
        date = dayEntity.idDay,
        dayOfWeek = dayOfWeekMapper.toDomain(dayEntity.dayOfWeek),
        completedTasks = dayEntity.completedTasks,
        totalTasks = dayEntity.totalTasks,
        incompleteTasks = incompleteTasksMapper.toTasksId(dayEntity.incompleteTasks)
    )
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDataBase(dayStatistics: DayStatistics) = DayEntity(
        idDay = dayStatistics.date,
        dayOfWeek = dayOfWeekMapper.toDataBase(dayStatistics.dayOfWeek),
        completedTasks = dayStatistics.completedTasks,
        totalTasks = dayStatistics.totalTasks,
        incompleteTasks = incompleteTasksMapper.toDataBase(dayStatistics.incompleteTasks)
    )
}