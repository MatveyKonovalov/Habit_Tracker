package com.example.habittracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MaxStreakDao {

    @Query("DELETE FROM max_streak_table")
    suspend fun deleteStreak(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaxStreak(maxStreak: MaxStreakEntity): Long

    @Query("SELECT * FROM max_streak_table")
    suspend fun getMaxStreak(): List<MaxStreakEntity>
}