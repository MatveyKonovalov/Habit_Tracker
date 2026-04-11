package com.example.habittracker.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_task") val idTask: String,
    val title: String,
    val description: String,
    val priority: Char,
    @ColumnInfo(name="created_at") val createdAt: Long,
)