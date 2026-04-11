package com.example.habittracker.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habittracker.data.local.entities.DayEntity

@Dao
interface DayDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(dayEntity: DayEntity): Long

    @Delete
    suspend fun delete(dayEntity: DayEntity): Int

    @Query("SELECT * FROM days ORDER BY id_day DESC")
    suspend fun getAllDay(): List<DayEntity>
    @Update
    suspend fun updateDay(day: DayEntity): Int

    @Query("SELECT * FROM days WHERE id_day = :id")
    suspend fun getDayByData(id: String): List<DayEntity>
}