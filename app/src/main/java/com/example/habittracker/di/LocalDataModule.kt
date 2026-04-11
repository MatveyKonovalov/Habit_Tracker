package com.example.habittracker.di

import android.content.Context
import com.example.habittracker.data.local.AppDataBase
import com.example.habittracker.data.local.daos.DayDao
import com.example.habittracker.data.local.mappers.DayEntityMapper
import com.example.habittracker.data.local.mappers.DayOfWeekMapper
import com.example.habittracker.data.local.mappers.IncompleteTasksMapper
import com.example.habittracker.data.local.daos.MaxStreakDao
import com.example.habittracker.data.local.helpers.WorkWithTimeHelper
import com.example.habittracker.data.local.mappers.PriorityMapper
import com.example.habittracker.data.local.mappers.TaskEntityMapper
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

        @Provides
        @Singleton
        fun provideWorkWithTimeHelper() = WorkWithTimeHelper()
    }
}