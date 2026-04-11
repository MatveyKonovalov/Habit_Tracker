package com.example.habittracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habittracker.data.local.daos.DayDao
import com.example.habittracker.data.local.daos.MaxStreakDao
import com.example.habittracker.data.local.daos.TaskDao
import com.example.habittracker.data.local.entities.DayEntity
import com.example.habittracker.data.local.entities.MaxStreakEntity
import com.example.habittracker.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, DayEntity::class, MaxStreakEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase(){
    abstract fun tasksDao(): TaskDao
    abstract fun daysDao(): DayDao

    abstract fun maxStreakDao(): MaxStreakDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}