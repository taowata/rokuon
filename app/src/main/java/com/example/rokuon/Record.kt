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
    @ColumnInfo(name = "time") var time: Long = 0L,
    @ColumnInfo(name = "record_date") var recordDate: String = "",
    @ColumnInfo(name = "record_order") var recordOrder: Int = 0,
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

        fun newInstance(context: Context?, order: Int): Record {
            val record = Record()
            record.apply {
                filePath = "${context?.filesDir}/record/レコード固有の番号など"
                recordDate = getDate()
                recordOrder = order
                return record
            }
        }
    }
}