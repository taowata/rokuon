package com.example.rokuon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecordViewModelFactory(
    private val audioRecorder: AudioRecorder,
    private val dataSource: RecordDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            return RecordViewModel(audioRecorder, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}