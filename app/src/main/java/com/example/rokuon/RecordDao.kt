package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Insert
    fun insert(record: Record)

    @Query("select * from record_table")
    fun allRecord(): LiveData<List<Record>>
}