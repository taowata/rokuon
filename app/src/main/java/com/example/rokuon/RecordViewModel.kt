package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordViewModel: ViewModel() {

    private var _recordingState: MutableLiveData<RecordingState> = MutableLiveData(RecordingState.NOT_RECORDING)
    val recordingState: LiveData<RecordingState> = _recordingState

    private var _recordingTag: MutableLiveData<String> = MutableLiveData("録音開始")
    val recordingTag: LiveData<String>
        get() = _recordingTag

    fun onClickRecordButton() {
        when (recordingState.value) {
            RecordingState.NOT_RECORDING -> {
                _recordingTag.value = "録音停止"
                _recordingState.value = RecordingState.RECORDING
            }
            RecordingState.RECORDING -> {
                _recordingTag.value = "録音開始"
                _recordingState.value = RecordingState.NOT_RECORDING
            }
        }
    }
}

enum class RecordingState {
    RECORDING, NOT_RECORDING
}