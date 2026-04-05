// data/worker/ReminderWorker.kt
package com.example.habittracker.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habittracker.R
import com.example.habittracker.data.datastore.SettingsDataStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val settingsDataStore: SettingsDataStore
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "reminder_work"
        const val CHANNEL_ID = "habit_reminder_channel"
        const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        return try {
            val isEnabled = settingsDataStore.isReminderEnabled.first()

            if (isEnabled) {
                sendNotification()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал для уведомлений (для Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Напоминания о привычках",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Напоминания о невыполненных задачах"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Напоминание о привычках")
            .setContentText("Пора выполнить сегодняшние задачи!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}