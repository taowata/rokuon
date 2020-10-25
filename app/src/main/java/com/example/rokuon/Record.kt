package com.example.rokuon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "record_table")
data class Record constructor(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "record_date") var recordDate: LocalDateTime = LocalDateTime.now(),
    @PrimaryKey(autoGenerate = true) val recordId: Long = 0
)