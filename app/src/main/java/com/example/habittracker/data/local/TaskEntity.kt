package com.example.habittracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.habittracker.domain.models.IncompleteTask
import com.example.habittracker.domain.models.TaskPriority


@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_task") val idTask: String,
    val title: String,
    val description: String,
    val priority: Char,
    @ColumnInfo(name="created_at") val createdAt: Long,
)