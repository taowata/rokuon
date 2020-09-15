package com.example.rokuon

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayViewModel: ViewModel() {

    private var _playingState: MutableLiveData<PlayingState> = MutableLiveData(PlayingState.NOT_PLAYING)
    val playingState: LiveData<PlayingState> = _playingState

    private var _playingTag: MutableLiveData<String> = MutableLiveData("再生開始")
    val playingTag: LiveData<String>
        get() = _playingTag

    @MainThread
    fun onClickPlayButton() {
        when (playingState.value) {
            PlayingState.NOT_PLAYING -> {
                _playingTag.value = "再生停止"
                _playingState.value = PlayingState.PLAYING
            }
            PlayingState.PLAYING -> {
                _playingTag.value = "再生開始"
                _playingState.value = PlayingState.NOT_PLAYING
            }
        }
    }
}

enum class PlayingState {
    PLAYING, NOT_PLAYING
}