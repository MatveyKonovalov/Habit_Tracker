package com.example.habittracker.presentation.view.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittracker.domain.models.DayStatistics
import com.example.habittracker.domain.models.IncompleteTask
import com.example.habittracker.domain.models.TaskPriority
import com.example.habittracker.R
import com.example.habittracker.data.local.DayOfWeekMapper
import com.example.habittracker.presentation.viewmodels.StatisticsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayDetailsDialog(
    day: DayStatistics,
    onDismiss: () -> Unit,
    viewModel: StatisticsViewModel
) {
    val tasksCache by viewModel.tasksCache.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = day.dayOfWeek.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = day.date,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Статистика выполнения
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Выполнено задач:",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${day.completedTasks}/${day.totalTasks}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (day.completedTasks == day.totalTasks)
                            Color(0xFF4CAF50)
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }

                // Прогресс-бар
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    val progress = if (day.totalTasks > 0)
                        day.completedTasks.toFloat() / day.totalTasks.toFloat()
                    else 0f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (progress == 1f) Color(0xFF4CAF50)
                                else MaterialTheme.colorScheme.primary
                            )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Заголовок невыполненных задач
                if (day.incompleteTasks.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_warning),
                            contentDescription = null,
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Невыполненные задачи (${day.incompleteTasks.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        items(day.incompleteTasks) { taskId ->
                            val task = tasksCache[taskId]
                            if (task != null) {
                                IncompleteTaskItem(task = task)
                            } else {
                                // Показываем заглушку, пока задача загружается
                                IncompleteTaskPlaceholder(taskId = taskId)
                            }
                        }
                    }
                } else {
                    // Все задачи выполнены
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Все задачи выполнены! 🎉",
                            fontSize = 14.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}



@Composable
private fun IncompleteTaskItem(task: IncompleteTask) {
    val (color, priorityText) = when (task.priority) {
        TaskPriority.HIGH -> Pair(Color(0xFFFF5252), "Высокий")
        TaskPriority.MEDIUM -> Pair(Color(0xFFFFA726), "Средний")
        TaskPriority.LOW -> Pair(Color(0xFF66BB6A), "Низкий")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = priorityText,
                fontSize = 11.sp,
                color = color
            )
        }
    }
}

@Composable
private fun IncompleteTaskPlaceholder(taskId: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp
        )
        Text(
            text = "Загрузка задачи...",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}