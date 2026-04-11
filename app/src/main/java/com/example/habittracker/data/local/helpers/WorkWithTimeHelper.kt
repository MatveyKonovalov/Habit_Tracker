package com.example.habittracker.data.local.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import javax.inject.Inject

class WorkWithTimeHelper @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDateTime(date: String): LocalDateTime {
        val dateMas = date.split("-")
        return LocalDateTime.of(
                dateMas[0].toInt(),
                dateMas[1].toInt(),
                dateMas[2].toInt(),
                0,
                0,
                0)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun isNext(dtPrev: LocalDateTime, dtNext: LocalDateTime): Boolean{
        return dtNext == dtPrev.plusDays(1)
    }
}