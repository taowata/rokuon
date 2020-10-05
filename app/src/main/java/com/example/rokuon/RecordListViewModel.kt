package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RecordListViewModel(
    private val recordDao: RecordDao
) : ViewModel() {

    val recordList: LiveData<List<Record>> = recordDao.allRecord()

    suspend fun insertNewRecord(record: Record): Long {
        return recordDao.insert(record)
    }
}