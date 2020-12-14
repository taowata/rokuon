package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rokuon.data.Record
import com.example.rokuon.data.RecordDao

class RecordListViewModel(
    private val recordDao: RecordDao
) : ViewModel() {

    val recordList: LiveData<List<Record>> = recordDao.allRecord()

    suspend fun insertNewRecord(record: Record): Long {
        return recordDao.insert(record)
    }
}