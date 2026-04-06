package com.example.habittracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayEntity: DayEntity): Long

    @Delete
    suspend fun delete(dayEntity: DayEntity): Int

    @Query("SELECT * FROM days")
    suspend fun getAllDay(): List<DayEntity>
    @Update
    suspend fun updateDay(day: DayEntity): Int

    @Query("SELECT * FROM days WHERE id_day = :id")
    suspend fun getDayByData(id: String): List<DayEntity>
}