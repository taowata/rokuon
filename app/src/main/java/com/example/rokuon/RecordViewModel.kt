package com.example.rokuon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordViewModel(
    private val recordDao: RecordDao
) : ViewModel() {

    val recordList: LiveData<List<Record>> = recordDao.allRecord()

    private var _recordingState: MutableLiveData<RecordingState> = MutableLiveData(RecordingState.NOT_RECORDING)
    val recordingState: LiveData<RecordingState> = _recordingState

    private var _recordingTag: MutableLiveData<String> = MutableLiveData("録音開始")
    val recordingTag: LiveData<String>
        get() = _recordingTag

    private var _isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private var _playingTag: MutableLiveData<String> = MutableLiveData("再生開始")
    val playingTag: LiveData<String>
        get() = _playingTag

    suspend fun largestOrder() = withContext(Dispatchers.IO) {
        val order = recordDao.getLargestOrder()
        order ?: 1
    }

    fun insertRecord(record: Record) = viewModelScope.launch(Dispatchers.IO) {
        recordDao.insert(record)
    }

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

    fun onClickPlayButton() {
        when (isPlaying.value) {
            true -> {
                _playingTag.value = "再生開始"
                _isPlaying.value = false
            }
            false -> {
                _playingTag.value = "再生停止"
                _isPlaying.value = true
            }
        }
    }

    enum class RecordingState {
        RECORDING, NOT_RECORDING
    }
}