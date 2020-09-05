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

    private var _isRecording = MutableLiveData<Boolean>()
    val isRecording: LiveData<Boolean>
        get() = _isRecording

    private var _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    suspend fun largestOrder() = withContext(Dispatchers.IO) {
        recordDao.getLargestOrder()
    }

    fun insertRecord(record: Record) = viewModelScope.launch(Dispatchers.IO) {
        recordDao.insert(record)
    }

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

    fun initLiveData() {
        _isRecording.postValue(false)
        _isPlaying.postValue(false)
    }
}