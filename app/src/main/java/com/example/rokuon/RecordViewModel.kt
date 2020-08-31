package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordViewModel: ViewModel() {

    private var _isRecording = MutableLiveData<Boolean>()
    val isRecording: LiveData<Boolean>
        get() = _isRecording

    fun switchRecordingState() {
        when (isRecording.value) {
            true -> _isRecording.postValue(false)
            false -> _isRecording.postValue(true)
        }
    }

    fun initIsRecording() {
        _isRecording.postValue(false)
    }

}