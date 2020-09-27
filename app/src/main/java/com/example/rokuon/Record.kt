package com.example.rokuon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "record_table")
data class Record(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "record_date") var recordDate: Date = Date(),
    @PrimaryKey(autoGenerate = true) val recordId: Long = 0
)