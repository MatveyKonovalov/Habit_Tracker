// data/datastore/SettingsDataStore.kt
package com.example.habittracker.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context  // Добавьте аннотацию
) {
    companion object {
        private val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        private val REMINDER_HOUR = stringPreferencesKey("reminder_hour")
        private val REMINDER_MINUTE = stringPreferencesKey("reminder_minute")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_SURNAME = stringPreferencesKey("user_surname")
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }

    val isReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[REMINDER_ENABLED] ?: false }

    val reminderHour: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[REMINDER_HOUR]?.toIntOrNull() ?: 9 }

    val reminderMinute: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[REMINDER_MINUTE]?.toIntOrNull() ?: 0 }

    val userName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME] ?: "" }

    val userSurname: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_SURNAME] ?: "" }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_DARK_THEME] ?: true }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED] = enabled
        }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_HOUR] = hour.toString()
            preferences[REMINDER_MINUTE] = minute.toString()
        }
    }

    suspend fun setUserInfo(name: String, surname: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_SURNAME] = surname
        }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = enabled
        }
    }
}