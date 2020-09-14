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
import androidx.fragment.app.viewModels
import java.io.IOException

class RecordFragment : Fragment() {

    private var fileName: String = ""

    private lateinit var recorder: MediaRecorder
    private lateinit var player: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_record, container, false)

        fileName = "${context?.externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        // viewModelの初期化
        val activity = requireActivity()
        val dataSource = RecordDatabase.getInstance(activity.application).recordDao
        val viewModelFactory = RecordViewModelFactory(dataSource)
        val viewModel: RecordViewModel by viewModels { viewModelFactory }

        val recordButton = v.findViewById<Button>(R.id.record_button)
        viewModel.recordingState.observe(viewLifecycleOwner) { recordingState ->
            recordButton.setOnClickListener {
                if (recordingState == RecordingState.RECORDING) stopRecording() else startRecording()
                viewModel.onClickRecordButton()
            }
        }
        viewModel.recordingTag.observe(viewLifecycleOwner) {
            recordButton.text = it
        }

        val playButton = v.findViewById<Button>(R.id.play_button)
        viewModel.playingState.observe(viewLifecycleOwner) { playingState ->
            playButton.setOnClickListener {
                if (playingState == PlayingState.PLAYING) stopPlaying() else startPlaying()
                viewModel.onClickPlayButton()
            }
        }
        viewModel.playingTag.observe(viewLifecycleOwner) {
            playButton.text = it
        }

        return v
    }

    private fun startPlaying() {
        player = MediaPlayer()
        player.setDataSource(fileName)
        try {
            player.prepare()
            player.start()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }

    }

    private fun stopPlaying() {
        player.release()
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
        }
        try {
            // 初期化の完了
            recorder.prepare()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }
        // レコーダーの開始
        recorder.start()
    }

    private fun stopRecording() {
        // レコーダーの停止
        recorder.stop()
        // MediaRecorderインスタンスの使用を終えたらできるだけ早くリソースを解放する
        recorder.release()
    }

    override fun onStop() {
        super.onStop()
        recorder.release()
        player.release()
    }

    private fun showDialogFragment(order: Int) {
        val newFragment = NewRecordDialogFragment.newInstance(order)
        newFragment.show(childFragmentManager, "new_record_dialog")
    }

    companion object {
        private const val LOG_TAG = "RecordFragment"
    }

}