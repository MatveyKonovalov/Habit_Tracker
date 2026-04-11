package com.example.habittracker.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "max_streak_table")
data class MaxStreakEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "max_streak") val maxStreak: Int = 1
)