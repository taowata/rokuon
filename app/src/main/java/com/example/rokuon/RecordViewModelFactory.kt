package com.example.rokuon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecordViewModelFactory(
    private val audioRecorder: AudioRecorder
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            return RecordViewModel(audioRecorder) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}