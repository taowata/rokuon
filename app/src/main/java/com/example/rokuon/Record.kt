package com.example.rokuon

import android.content.Context
import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "record_table")
data class Record(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "file_path") var filePath: String = "",
    @ColumnInfo(name = "record_date") var recordDate: Date = Date(),
    @ColumnInfo(name = "record_order") var recordOrder: Int = 0,
    @PrimaryKey(autoGenerate = true) val recordId: Int = 0
)