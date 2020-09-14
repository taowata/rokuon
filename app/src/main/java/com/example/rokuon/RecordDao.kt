package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Insert
    fun insert(record: Record)

    @Query("select * from record_table")
    fun allRecord(): LiveData<List<Record>>

    // record_orderの最大値を取得
    @Query("select max(record_order) from record_table")
    fun getLargestOrder(): LiveData<Int>
}