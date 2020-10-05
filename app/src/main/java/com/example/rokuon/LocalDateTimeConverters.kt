package com.example.rokuon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.*

@RequiresApi(Build.VERSION_CODES.O)
class LocalDateTimeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    @TypeConverter
    fun localDateTimeToTimestamp(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}