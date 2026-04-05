// presentation/components/TaskDialog.kt
package com.example.habittracker.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.domain.models.DayStatistics
import com.example.habittracker.domain.models.IncompleteTask
import com.example.habittracker.domain.models.TaskPriority
import com.example.habittracker.R

@Composable
fun TaskDialog(
    isEditing: Boolean,
    initialTitle: String = "",
    initialDescription: String = "",
    initialPriority: TaskPriority = TaskPriority.MEDIUM,
    onConfirm: (title: String, description: String, priority: TaskPriority) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }
    var selectedPriority by remember { mutableStateOf(initialPriority) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing) "Редактировать задачу" else "Новая задача",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название задачи") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание (необязательно)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Выбор приоритета
                Text(
                    text = "Приоритет",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskPriority.values().reversed().forEach { priority ->
                        val isSelected = selectedPriority == priority
                        val (color, text) = when (priority) {
                            TaskPriority.HIGH -> Pair(0xFFFF5252, "Высокий")
                            TaskPriority.MEDIUM -> Pair(0xFFFFA726, "Средний")
                            TaskPriority.LOW -> Pair(0xFF66BB6A, "Низкий")
                        }

                        Button(
                            onClick = { selectedPriority = priority },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected)
                                    Color(color)
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected)
                                    Color.White
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(text = text, fontSize = 9.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, description, selectedPriority)
                    }
                }
            ) {
                Text(if (isEditing) "Сохранить" else "Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}