package com.example.rokuon

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.IOException

class RecordFragment : Fragment() {

    private var fileName: String = ""

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    private lateinit var viewModel: RecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_record, container, false)

        fileName = "${context?.externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        viewModel.initRecordingState()
        viewModel.initPlayingState()

        val recordButton = v.findViewById<Button>(R.id.record_button)
        viewModel.isRecording.observe(viewLifecycleOwner, Observer { isRecording ->

            if (isRecording) {
                recordButton.apply {
                    setOnClickListener {
                        stopRecording()
                        viewModel.switchRecordingState()
                    }
                    text = "録音停止"
                }

            } else {
                recordButton.apply {
                    setOnClickListener {
                        startRecording()
                        viewModel.switchRecordingState()
                    }
                    text = "録音"
                }
            }
        })

        val playButton = v.findViewById<Button>(R.id.play_button)
        viewModel.isPlaying.observe(viewLifecycleOwner, Observer { isPlaying ->
            if (isPlaying) {
                playButton.apply {
                    setOnClickListener {
                        stopPlaying()
                        viewModel.switchPlayingState()
                    }
                    text = "再生停止"
                }
            } else {
                playButton.apply {
                    setOnClickListener {
                        startPlaying()
                        viewModel.switchPlayingState()
                    }
                    text = "再生"
                }
            }
        })

        return v
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            // 音源
            setAudioSource(MediaRecorder.AudioSource.MIC)
            // 出力ファイル形式
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            // 出力ファイル名
            setOutputFile(fileName)
            // 音声エンコーダ
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                // 初期化の完了
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
            // レコーダーの開始
            start()
            Log.i(LOG_TAG, "start() called")
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            // レコーダーの停止
            stop()
            Log.i(LOG_TAG, "stop() called")
            // MediaRecorderインスタンスの使用を終えたらできるだけ早くリソースを解放する
            release()
        }
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }

    companion object {
        private const val LOG_TAG = "RecordFragment"
    }

}