package com.example.rokuon

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordViewModel(
    private val audioRecorder: AudioRecorder
) : ViewModel() {

    private val _recordingState: MutableLiveData<RecordingState> =
        MutableLiveData(RecordingState.NOT_RECORDING)
    val recordingState: LiveData<RecordingState> = _recordingState

    private val _recordingTag: MutableLiveData<String> = MutableLiveData("録音開始")
    val recordingTag: LiveData<String>
        get() = _recordingTag

    @MainThread
    fun onClickRecordButton(filePath: String) {
        when (recordingState.value) {
            RecordingState.NOT_RECORDING -> {
                audioRecorder.startRecording(filePath)
                _recordingState.value = RecordingState.RECORDING
                _recordingTag.value = "録音停止"
            }
            RecordingState.RECORDING -> {
                audioRecorder.stopRecording()
                _recordingState.value = RecordingState.NOT_RECORDING
                _recordingTag.value = "録音開始"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorder.releaseRecorder()
    }
}

enum class RecordingState {
    RECORDING, NOT_RECORDING
}