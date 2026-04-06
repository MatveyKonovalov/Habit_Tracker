package com.example.habittracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "days")
data class DayEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_day") val idDay: String,
    @ColumnInfo(name="day_of_week") val dayOfWeek: String,
    @ColumnInfo(name="complete_task") val completedTasks: Int,
    @ColumnInfo(name="total_task") val totalTasks: Int,
    @ColumnInfo(name="incomplete_tasks") val incompleteTasks: String
)