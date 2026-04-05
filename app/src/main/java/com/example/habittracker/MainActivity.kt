// MainActivity.kt
package com.example.habittracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittracker.presentation.MainScreen
import com.example.habittracker.presentation.navigation.Screen
import com.example.habittracker.presentation.viewmodels.SettingsViewModel
import com.example.habittracker.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Запрашиваем разрешения при запуске
        checkAndRequestPermissions()


        setContent {
            val settingModel: SettingsViewModel = hiltViewModel()
            val darkTheme by settingModel.isDarkTheme.collectAsStateWithLifecycle()
            HabitTrackerTheme(darkTheme = darkTheme) {
                MainScreen()
            }
        }
    }

    private fun checkAndRequestPermissions() {
        // Для Android 13+ нужно разрешение на уведомления
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission()
            }
        }

        // Для Android 12+ нужно разрешение на точные будильники
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                requestExactAlarmPermission()
            }
        }
    }

    private fun requestNotificationPermission() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                android.util.Log.d("Permissions", "Notification permission granted")
            } else {
                android.util.Log.d("Permissions", "Notification permission denied")
            }
        }

        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun requestExactAlarmPermission() {
        val intent = android.content.Intent(
            android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
            android.net.Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }
}