package com.example.habittracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity): Long

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity): Int

    @Query("SELECT * FROM tasks WHERE id_task = :id")
    suspend fun getTaskById(id: String): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Update
    suspend fun updateTask(task: TaskEntity): Int
}