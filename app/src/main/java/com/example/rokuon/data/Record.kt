package com.example.rokuon.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "record_table")
data class Record constructor(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "record_date") var recordDate: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "time") var time: Long = 0,
    @PrimaryKey(autoGenerate = true) val recordId: Long = 0
)