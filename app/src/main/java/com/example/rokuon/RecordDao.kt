package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDao {
    @Insert
    suspend fun insert(record: Record): Long

    @Update
    suspend fun update(record: Record)

    @Query("select * from record_table")
    fun allRecord(): LiveData<List<Record>>

    @Query("SELECT * FROM record_table WHERE recordId == :id")
    suspend fun getRecordById(id: Long): Record
}