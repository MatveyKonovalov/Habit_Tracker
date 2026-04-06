package com.example.habittracker.data.local

import javax.inject.Inject

class IncompleteTasksMapper @Inject constructor() {
    fun toTasksId(data: String): List<String> = data.split(" ")
    fun toDataBase(data: List<String>): String = data.joinToString(" ")
}