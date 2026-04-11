package com.example.habittracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.habittracker.data.local.daos.DayDao
import com.example.habittracker.data.local.mappers.DayEntityMapper
import com.example.habittracker.data.local.daos.MaxStreakDao
import com.example.habittracker.data.local.entities.MaxStreakEntity
import com.example.habittracker.data.local.daos.TaskDao
import com.example.habittracker.data.local.helpers.WorkWithTimeHelper
import com.example.habittracker.data.local.mappers.TaskEntityMapper
import com.example.habittracker.domain.models.Task
import com.example.habittracker.domain.models.TaskPriority
import com.example.habittracker.domain.TaskRepository
import com.example.habittracker.domain.models.DayStatistics
import com.example.habittracker.domain.models.IncompleteTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val tasksDao: TaskDao,
    private val dayDao: DayDao,
    private val taskEntityMapper: TaskEntityMapper,
    private val dayEntityMapper: DayEntityMapper,
    private val maxStreakDao: MaxStreakDao,
    private val workWithTimeHelper: WorkWithTimeHelper
) : TaskRepository {

    private var tasks = emptyList<Task>()

    @RequiresApi(Build.VERSION_CODES.O)
    private var today = DayStatistics(
        date = LocalDate.now().toString(),
        dayOfWeek = LocalDate.now().dayOfWeek,
        completedTasks = 0,
        totalTasks = 0,
        incompleteTasks = emptyList()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun loadData() {
        withContext(Dispatchers.IO) {
            val todayDate = LocalDate.now().toString()
            val existingDay = dayDao.getDayByData(todayDate)

            // Загружаем все задачи из БД
            val allTasksFromDb = tasksDao.getAllTasks().map { taskEntityMapper.toDomain(it) }

            if (existingDay.isEmpty()) {
                tasks = allTasksFromDb.map { it.copy(isCompleted = false) }

                today = DayStatistics(
                    date = todayDate,
                    dayOfWeek = LocalDate.now().dayOfWeek,
                    completedTasks = 0,
                    totalTasks = tasks.size,
                    incompleteTasks = tasks.map { it.id }
                )

                dayDao.insert(dayEntityMapper.toDataBase(today))
            } else {
                // Существующий день: загружаем статус из dayDao
                today = dayEntityMapper.toDomain(existingDay.first())

                // Восстанавливаем статус задач из today.incompleteTasks
                tasks = allTasksFromDb.map { task ->
                    val isCompleted = task.id !in today.incompleteTasks
                    task.copy(isCompleted = isCompleted)
                }

                // Обновляем totalTasks на случай, если добавились новые задачи
                if (today.totalTasks != tasks.size) {
                    today = today.copy(totalTasks = tasks.size)
                    dayDao.updateDay(dayEntityMapper.toDataBase(today))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            tasks
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            tasksDao.getAllTasks().map { taskEntityMapper.toDomain(it, false) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun completeTask(id: String) {
        withContext(Dispatchers.IO) {
            // 1. Обновляем локальный список задач
            tasks = tasks.map {
                if (it.id == id) {
                    it.copy(isCompleted = !it.isCompleted)
                } else {
                    it
                }
            }

            // 2. Находим задачу и обновляем её в БД
            val updatedTask = tasks.find { it.id == id }
            if (updatedTask != null) {
                val taskEntity = taskEntityMapper.toDataBase(updatedTask)
                tasksDao.updateTask(taskEntity)
            }

            // 3. Обновляем incompleteTasks в today
            val task = tasks.find { it.id == id }
            if (task != null) {
                if (task.isCompleted) {
                    // Задача выполнена - удаляем из списка невыполненных
                    today = today.copy(
                        incompleteTasks = today.incompleteTasks.filter { it != id },
                        completedTasks = tasks.count { it.isCompleted }
                    )
                } else {
                    // Задача не выполнена - добавляем в список невыполненных
                    if (id !in today.incompleteTasks) {
                        today = today.copy(
                            incompleteTasks = today.incompleteTasks + id,
                            completedTasks = tasks.count { it.isCompleted }
                        )
                    }
                }
            }

            // 4. Сохраняем обновленную статистику дня
            dayDao.updateDay(dayEntityMapper.toDataBase(today))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addTask(task: Task) {
        withContext(Dispatchers.IO) {
            // 1. Сохраняем задачу в БД
            tasksDao.insertTask(taskEntityMapper.toDataBase(task))

            // 2. Добавляем в локальный список
            tasks = tasks + task.copy(isCompleted = false)

            // 3. Обновляем статистику дня
            today = today.copy(
                totalTasks = tasks.size,
                incompleteTasks = today.incompleteTasks + task.id
            )

            // 4. Сохраняем обновленную статистику
            dayDao.updateDay(dayEntityMapper.toDataBase(today))
        }
    }

    override suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            // 1. Обновляем задачу в БД
            tasksDao.updateTask(taskEntityMapper.toDataBase(task))

            // 2. Обновляем локальный список
            tasks = tasks.map {
                if (it.id == task.id) {
                    task.copy(isCompleted = it.isCompleted)
                } else {
                    it
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun deleteTaskById(id: String) {
        withContext(Dispatchers.IO) {
            // 1. Удаляем задачу из БД
            tasksDao.deleteTask(id)

            // 2. Удаляем из локального списка
            tasks = tasks.filter { it.id != id }

            // 3. Обновляем статистику дня
            today = today.copy(
                totalTasks = tasks.size,
                incompleteTasks = today.incompleteTasks.filter { it != id },
                completedTasks = tasks.count { it.isCompleted }
            )

            // 4. Сохраняем обновленную статистику
            dayDao.updateDay(dayEntityMapper.toDataBase(today))
        }
    }

    override suspend fun getTaskById(id: String): IncompleteTask {
        return withContext(Dispatchers.IO) {
            val taskEntity = tasksDao.getTaskById(id).first()
            val task = taskEntityMapper.toDomain(taskEntity)
            IncompleteTask(
                id = id,
                title = task.title,
                priority = task.priority ?: TaskPriority.LOW
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getMaxStreak(): Int {
        return withContext(Dispatchers.IO) {
            val max = maxStreakDao.getMaxStreak()
            if (max.isEmpty()) 0 else max.first().maxStreak
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCurrentStreak(): Int {
        return withContext(Dispatchers.IO) {
            val days = dayDao.getAllDay()
            var streak = 0

            if (days.isNotEmpty()) {
                if (days.size > 1) {
                    var currentDay = workWithTimeHelper.toDateTime(days.first().idDay)

                    for (i in 1 until days.size) {
                        val day = days[i]
                        val dateI = workWithTimeHelper.toDateTime(day.idDay)
                        if (day.completedTasks > 0 &&
                            workWithTimeHelper.isNext(dateI, currentDay)) {
                            streak++
                            currentDay = dateI
                        } else {
                            break
                        }
                    }
                }
            }

            // Обновляем макс. стрик
            val max = maxStreakDao.getMaxStreak()
            if (today.completedTasks > 0) {
                streak += 1
            }

            if (max.isEmpty()) {
                maxStreakDao.insertMaxStreak(MaxStreakEntity(streak))
            } else if (max.first().maxStreak < streak) {
                maxStreakDao.deleteStreak()
                maxStreakDao.insertMaxStreak(MaxStreakEntity(streak))
            }

            streak
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllDays(): List<DayStatistics> {
        return withContext(Dispatchers.IO) {
            val allTasksId = getAllTasks().map{it.id}.toSet()

            dayDao.getAllDay().map { dayEntity ->
                val currentDay = dayEntityMapper.toDomain(dayEntity)
                val actualTaskList = mutableListOf<String>()
                for (task in currentDay.incompleteTasks){
                    if (task in allTasksId){
                        actualTaskList.add(task)
                    }
                }
                val actualDay = currentDay.copy(incompleteTasks = actualTaskList)
                dayDao.updateDay(dayEntityMapper.toDataBase(actualDay))
                actualDay
            }
        }
    }
}