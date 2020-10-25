package com.example.rokuon

import android.media.MediaRecorder
import android.util.Log
import java.io.IOException

class AudioRecorder {
    private var recorder: MediaRecorder? = null

    fun startRecording(filePath: String) {
        recorder = MediaRecorder().apply {
            // 音源
            setAudioSource(MediaRecorder.AudioSource.MIC)
            // 出力ファイル形式
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            // 出力ファイル名
            setOutputFile(filePath)
            // 音声エンコーダ
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        }
        try {
            // 初期化の完了
            recorder?.prepare()
        } catch (e: IOException) {
            Log.e("AudioRecorder", "prepare() failed")
        }
        // レコーダーの開始
        recorder?.start()
    }

    fun stopRecording() {
        // レコーダーの停止
        recorder?.stop()
        // MediaRecorderインスタンスの使用を終えたらできるだけ早くリソースを解放する
        recorder?.release()
    }

    fun pauseRecording() {
        recorder?.pause()
    }

    fun resumeRecording() {
        recorder?.resume()
    }

    fun releaseRecorder() {
        recorder?.release()
    }


}