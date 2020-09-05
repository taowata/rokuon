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

@Entity
data class Record(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "file_path") var filePath: String = "",
    @ColumnInfo(name = "time") var time: Long = 0L,
    @ColumnInfo(name = "record_date") var recordDate: String = "",
    @PrimaryKey(autoGenerate = true) val recordId: Int = 0
) {
    companion object {
        private fun getDate(): String {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy/M/dd hh:mm:ss")
                    val formatted = current.format(formatter)
                    formatted.toString()
                }
                else -> {
                    val sdf = SimpleDateFormat("yyyy/M/dd hh:mm:ss", Locale.getDefault())
                    val currentDate = sdf.format(Date())
                    return currentDate.toString()
                }
            }
        }

        fun newInstance(context: Context?): Record {
            val record = Record()
            record.filePath = "${context?.filesDir}/record/レコード固有の番号など"
            record.recordDate = getDate()
            return record
        }
    }
}