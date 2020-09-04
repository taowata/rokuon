package com.example.rokuon

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
data class Record(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "file_path") var filePath: String = "",
    @ColumnInfo(name = "time") var time: Long = 0L,
    @ColumnInfo(name = "record_date") var recordDate: String = "",
    @PrimaryKey(autoGenerate = true) val recordId: Int = 0
) {
    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getDate(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_DATE
            val formatted = current.format(formatter)
            return formatted.toString()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(context: Context): Record {
            val record = Record()
            record.filePath = "${context.externalCacheDir?.absolutePath}/${record.recordId}"
            record.recordDate = getDate()
            return record
        }
    }
}