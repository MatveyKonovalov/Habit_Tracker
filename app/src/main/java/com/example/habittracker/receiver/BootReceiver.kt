// receiver/BootReceiver.kt
package com.example.habittracker.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.habittracker.utils.AlarmHelper
import com.example.habittracker.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            runBlocking {
                val dataStore = SettingsDataStore(context)
                val isEnabled = dataStore.isReminderEnabled.first()
                val hour = dataStore.reminderHour.first()
                val minute = dataStore.reminderMinute.first()

                if (isEnabled) {
                    // Проверяем разрешение перед установкой будильника
                    if (hasExactAlarmPermission(context)) {
                        AlarmHelper.scheduleDailyReminder(context, hour, minute)
                    }
                }
            }
        }
    }

    private fun hasExactAlarmPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            return alarmManager.canScheduleExactAlarms()
        }
        return true
    }
}