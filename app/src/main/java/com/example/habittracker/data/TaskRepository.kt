package com.example.habittracker.data


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.habittracker.data.local.DayDao
import com.example.habittracker.data.local.DayEntityMapper
import com.example.habittracker.data.local.TaskDao
import com.example.habittracker.data.local.TaskEntityMapper
import com.example.habittracker.domain.models.Task
import com.example.habittracker.domain.models.TaskPriority
import com.example.habittracker.domain.TaskRepository
import com.example.habittracker.domain.models.DayStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val tasksDao: TaskDao,
    private val dayDao: DayDao,
    private val taskEntityMapper: TaskEntityMapper,
    private val dayEntityMapper: DayEntityMapper
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
    override fun getTasks(): Flow<List<Task>> = flow {
        loadData()
        emit(tasks)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun loadData() {
        val todayList = dayDao.getDayByData(today.date)
        if (todayList.isEmpty()){
            tasks = tasksDao.getAllTasks().map{taskEntityMapper.toDomain(it)}
            today.totalTasks = tasks.size
            today.incompleteTasks = tasks.map{it.id}
            dayDao.insert(dayEntityMapper.toDataBase(today))
        } else{
            today = dayEntityMapper.toDomain(todayList.first())
            tasks = tasksDao.getAllTasks().map{
                taskEntityMapper.toDomain(it)
                if (it.idTask !in today.incompleteTasks){
                    taskEntityMapper.toDomain(it, true)
                } else{
                    taskEntityMapper.toDomain(it, false)
                }
            }

        }

    }
    override suspend fun completeTask(id: String) {
        tasks = tasks.map { task ->
            if (task.id == id) task.copy(isCompleted = true)
            else task
        }
    }

    override suspend fun getMaxStreak(): Int {
        // Логика подсчета максимальной серии
        return tasks.count { it.isCompleted }
    }

    override suspend fun getCurrentStreak(): Int {
        // Логика подсчета текущей серии
        return tasks.count { it.isCompleted }
    }
}