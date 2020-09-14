package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordListViewModel(
    private val recordDao: RecordDao
) : ViewModel() {

    val recordList: LiveData<List<Record>> = recordDao.allRecord()
    val largestOrder: LiveData<Int> = recordDao.getLargestOrder()

    fun insertRecord(record: Record) = viewModelScope.launch(Dispatchers.IO) {
        recordDao.insert(record)
    }


}