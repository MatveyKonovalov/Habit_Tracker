// presentation/viewmodels/SettingsViewModel.kt
package com.example.habittracker.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.datastore.SettingsDataStore
import com.example.habittracker.utils.AlarmHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val application: Application
) : AndroidViewModel(application) {

    private val _isReminderEnabled = MutableStateFlow(false)
    val isReminderEnabled: StateFlow<Boolean> = _isReminderEnabled.asStateFlow()

    private val _reminderHour = MutableStateFlow(9)
    val reminderHour: StateFlow<Int> = _reminderHour.asStateFlow()

    private val _reminderMinute = MutableStateFlow(0)
    val reminderMinute: StateFlow<Int> = _reminderMinute.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userSurname = MutableStateFlow("")
    val userSurname: StateFlow<String> = _userSurname.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker: StateFlow<Boolean> = _showTimePicker.asStateFlow()

    private val _showUserDialog = MutableStateFlow(false)
    val showUserDialog: StateFlow<Boolean> = _showUserDialog.asStateFlow()

    private val _showPermissionDialog = MutableStateFlow(false)
    val showPermissionDialog: StateFlow<Boolean> = _showPermissionDialog.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsDataStore.isReminderEnabled.collect { enabled ->
                _isReminderEnabled.value = enabled
            }
        }

        viewModelScope.launch {
            settingsDataStore.reminderHour.collect { hour ->
                _reminderHour.value = hour
            }
        }

        viewModelScope.launch {
            settingsDataStore.reminderMinute.collect { minute ->
                _reminderMinute.value = minute
            }
        }

        viewModelScope.launch {
            settingsDataStore.userName.collect { name ->
                _userName.value = name
            }
        }

        viewModelScope.launch {
            settingsDataStore.userSurname.collect { surname ->
                _userSurname.value = surname
            }
        }

        viewModelScope.launch {
            settingsDataStore.isDarkTheme.collect { dark ->
                _isDarkTheme.value = dark
            }
        }
    }

    fun toggleReminder(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setReminderEnabled(enabled)
            if (enabled) {
                // ✅ Используем AlarmHelper.canScheduleExactAlarms()
                if (!AlarmHelper.canScheduleExactAlarms(application.applicationContext)) {
                    _showPermissionDialog.value = true
                    return@launch
                }
                val hour = _reminderHour.value
                val minute = _reminderMinute.value
                AlarmHelper.scheduleDailyReminder(application.applicationContext, hour, minute)
            } else {
                AlarmHelper.cancelReminder(application.applicationContext)
            }
        }
    }

    fun updateReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsDataStore.setReminderTime(hour, minute)
            _reminderHour.value = hour
            _reminderMinute.value = minute

            if (_isReminderEnabled.value) {
                if (!AlarmHelper.canScheduleExactAlarms(application.applicationContext)) {
                    _showPermissionDialog.value = true
                    return@launch
                }
                AlarmHelper.scheduleDailyReminder(application.applicationContext, hour, minute)
            }
        }
    }

    fun updateUserInfo(name: String, surname: String) {
        viewModelScope.launch {
            settingsDataStore.setUserInfo(name, surname)
            _userName.value = name
            _userSurname.value = surname
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDarkTheme(isDark)
            _isDarkTheme.value = isDark
        }
    }

    fun testNotification() {
        AlarmHelper.testNotification(application.applicationContext)
    }

    fun showTimePicker() {
        _showTimePicker.value = true
    }

    fun hideTimePicker() {
        _showTimePicker.value = false
    }

    fun showUserDialog() {
        _showUserDialog.value = true
    }

    fun hideUserDialog() {
        _showUserDialog.value = false
    }

    fun hidePermissionDialog() {
        _showPermissionDialog.value = false
    }

    fun openPermissionSettings() {
        val intent = android.content.Intent(
            android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
            android.net.Uri.parse("package:${application.packageName}")
        )
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
        _showPermissionDialog.value = false
    }
    fun openNotificationSettings() {
        val intent = android.content.Intent(
            android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS,
            android.net.Uri.parse("package:${application.packageName}")
        ).apply {
            putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, application.packageName)
        }
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }
}