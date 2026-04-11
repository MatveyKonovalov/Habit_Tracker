package com.example.habittracker.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habittracker.data.local.entities.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity): Long

    @Query("DELETE FROM tasks where id_task = :id")
    suspend fun deleteTask(id: String): Int

    @Query("SELECT * FROM tasks WHERE id_task = :id")
    suspend fun getTaskById(id: String): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Update
    suspend fun updateTask(task: TaskEntity): Int
}