package com.example.rokuon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.linecorp.lich.component.ComponentFactory

@Database(entities = [Record::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class RecordDatabase : RoomDatabase() {

    abstract val recordDao: RecordDao

    companion object : ComponentFactory<RecordDatabase>() {
        override fun createComponent(context: Context): RecordDatabase =
            Room.databaseBuilder(context, RecordDatabase::class.java, "record_db").build()
    }
}