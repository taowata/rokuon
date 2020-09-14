package com.example.rokuon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecordListViewModelFactory(
    private val dataSource: RecordDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            return RecordListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}