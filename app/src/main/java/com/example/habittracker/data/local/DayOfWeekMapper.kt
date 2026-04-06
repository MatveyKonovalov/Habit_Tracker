package com.example.habittracker.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import javax.inject.Inject

class DayOfWeekMapper @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(data: String): DayOfWeek = when(data){
        "Monday" -> DayOfWeek.MONDAY
        "Tuesday" -> DayOfWeek.TUESDAY
        "Thursday" -> DayOfWeek.THURSDAY
        "Wednesday" -> DayOfWeek.WEDNESDAY
        "Saturday" -> DayOfWeek.SATURDAY
        "Friday" -> DayOfWeek.FRIDAY
        "Sunday" -> DayOfWeek.SUNDAY
        else -> throw Exception("Undefined data")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDataBase(data: DayOfWeek): String = when(data){
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.SATURDAY -> "Saturday"
        DayOfWeek.SUNDAY -> "Sunday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
    }
}