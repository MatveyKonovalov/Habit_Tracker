package com.example.habittracker.di

import android.content.Context
import com.example.habittracker.data.local.AppDataBase
import com.example.habittracker.data.local.DayDao
import com.example.habittracker.data.local.DayEntityMapper
import com.example.habittracker.data.local.DayOfWeekMapper
import com.example.habittracker.data.local.IncompleteTasksMapper
import com.example.habittracker.data.local.MaxStreakDao
import com.example.habittracker.data.local.PriorityMapper
import com.example.habittracker.data.local.TaskEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataModule {
    companion object {
        @Provides
        @Singleton
        fun providePriorityMapper() = PriorityMapper()

        @Provides
        @Singleton
        fun provideIncompleteTaskMapper() = IncompleteTasksMapper()

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context) = AppDataBase.getInstance(context)

        @Provides
        @Singleton
        fun provideTasksDao(dataBase: AppDataBase) = dataBase.tasksDao()

        @Provides
        @Singleton
        fun provideDaysDao(dataBase: AppDataBase) = dataBase.daysDao()

        @Provides
        @Singleton
        fun providesTaskEntityMapper(priorityMapper: PriorityMapper) =
            TaskEntityMapper(priorityMapper)

        @Provides
        @Singleton
        fun provideDayEntityMapper(incompleteTasksMapper: IncompleteTasksMapper, dayOfWeekMapper: DayOfWeekMapper) =
            DayEntityMapper(
                incompleteTasksMapper,
                dayOfWeekMapper = dayOfWeekMapper
            )
        @Provides
        @Singleton
        fun provideMaxStreakDao(dataBase: AppDataBase) = dataBase.maxStreakDao()

        @Provides
        @Singleton
        fun provideDataMapper() = DayOfWeekMapper()
    }
}