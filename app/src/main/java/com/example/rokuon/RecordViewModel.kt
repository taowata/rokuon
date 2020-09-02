package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordViewModel: ViewModel() {

    private var _isRecording = MutableLiveData<Boolean>()
    val isRecording: LiveData<Boolean>
        get() = _isRecording

    private var _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    fun switchRecordingState() {
        when (isRecording.value) {
            true -> _isRecording.postValue(false)
            false -> _isRecording.postValue(true)
        }
    }

    fun switchPlayingState() {
        when (isPlaying.value) {
            true -> _isPlaying.postValue(false)
            false -> _isPlaying.postValue(true)
        }
    }

    fun initRecordingState() {
        _isRecording.postValue(false)
    }

    fun initPlayingState() {
        _isPlaying.postValue(false)
    }

}