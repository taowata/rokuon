package com.example.rokuon

import android.media.MediaMetadataRetriever
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecordViewModel(
    private val audioRecorder: AudioRecorder,
    private val recordDao: RecordDao
) : ViewModel() {

    private val _startButtonVisibility: MutableLiveData<Int> = MutableLiveData(View.VISIBLE)
    val startButtonVisibility: LiveData<Int> = _startButtonVisibility

    private val _pauseButtonVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val pauseButtonVisibility: LiveData<Int> = _pauseButtonVisibility

    private val _resumeButtonVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val resumeButtonVisibility: LiveData<Int> = _resumeButtonVisibility

    private val _finishButtonVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val finishButtonVisibility: LiveData<Int> = _finishButtonVisibility

    private val _recordingBarVisibility: MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)
    val recordingBarVisibility: LiveData<Int> = _recordingBarVisibility

    private val _recordingTime: MutableLiveData<Long> = MutableLiveData(0L)
    val recordingTime: LiveData<Long> = _recordingTime

    @MainThread
    fun startRecording(filePath: String) {
        audioRecorder.startRecording(filePath)
        _startButtonVisibility.value = View.GONE
        _pauseButtonVisibility.value = View.VISIBLE
        _recordingBarVisibility.value = View.VISIBLE
        startTimer()
    }

    @MainThread
    fun pauseRecording() {
        audioRecorder.pauseRecording()
        _pauseButtonVisibility.value = View.GONE
        _resumeButtonVisibility.value = View.VISIBLE
        _finishButtonVisibility.value = View.VISIBLE
        _recordingBarVisibility.value = View.INVISIBLE
    }

    @MainThread
    fun resumeRecording() {
        audioRecorder.resumeRecording()
        _resumeButtonVisibility.value = View.GONE
        _finishButtonVisibility.value = View.GONE
        _pauseButtonVisibility.value = View.VISIBLE
        _recordingBarVisibility.value = View.VISIBLE
        startTimer()
    }

    @MainThread
    fun finishRecording() {
        audioRecorder.stopRecording()
    }

    suspend fun updateRecordName(recordId: Long) {
        val recordName = "録音$recordId"
        val record = recordDao.getRecordById(recordId)
        record.name = recordName
        recordDao.update(record)
    }

    suspend fun updateRecordTime(recordId: Long, filePath: String) {
        val record = recordDao.getRecordById(recordId)
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(filePath)
        val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val recordTime = duration?.toLong()?.div(1000L)
        record.time = recordTime ?: 0
        recordDao.update(record)
    }

    @MainThread
    private fun startTimer() {
        viewModelScope.launch {
            delay(1000)
            while(recordingBarVisibility.value == View.VISIBLE) {
                _recordingTime.value = _recordingTime.value?.plus(1L)
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorder.releaseRecorder()
    }
}