package com.example.rokuon

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rokuon.databinding.FragmentRecordBinding
import java.io.IOException

class RecordFragment : Fragment() {

    private var dirPath: String = ""
    private var filePath: String = ""
    private var order: Int = 0

    private  var recorder: MediaRecorder? = null
    private  var player: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecordBinding.inflate(layoutInflater, container, false)

        dirPath = "${context?.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath}"

        // viewModelの初期化
        val activity = requireActivity()
        val dataSource = RecordDatabase.getInstance(activity.application).recordDao
        val viewModelFactory = RecordViewModelFactory(dataSource)
        val viewModel: RecordViewModel by viewModels { viewModelFactory }

        val recordButton = binding.recordButton
        val editTextView = binding.recordNameEdit
        viewModel.recordingState.observe(viewLifecycleOwner) { recordingState ->
            recordButton.setOnClickListener {
                if (recordingState == RecordingState.RECORDING) {
//                    val newRecord = Record(
//                        name = editTextView.text.toString(),
//                        filePath = filePath,
//                        time = Record.getDate(),
//                        recordOrder = order
//                    )
//                    editTextView.text.clear()
//                    viewModel.insertRecord(newRecord)
                    stopRecording()
                } else {
//                    order = viewModel.largestOrder.value ?: 1
//                    filePath = dirPath + order
                    startRecording(filePath)
                }
                viewModel.onClickRecordButton()
            }
        }
        viewModel.recordingTag.observe(viewLifecycleOwner) {
            recordButton.text = it
        }

        val playButton = binding.playButton
        viewModel.playingState.observe(viewLifecycleOwner) { playingState ->
            playButton.setOnClickListener {
                if (playingState == PlayingState.PLAYING) stopPlaying() else startPlaying()
                viewModel.onClickPlayButton()
            }
        }
        viewModel.playingTag.observe(viewLifecycleOwner) {
            playButton.text = it
        }

        return binding.root
    }

    private fun startPlaying() {
        player = MediaPlayer()
        player?.setDataSource(filePath)
        try {
            player?.prepare()
            player?.start()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }

    }

    private fun stopPlaying() {
        player?.release()
    }

    private fun startRecording(filePath: String) {
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
            Log.e(LOG_TAG, "prepare() failed")
        }
        // レコーダーの開始
        recorder?.start()
    }

    private fun stopRecording() {
        // レコーダーの停止
        recorder?.stop()
        // MediaRecorderインスタンスの使用を終えたらできるだけ早くリソースを解放する
        recorder?.release()
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        player?.release()
    }

    private fun showDialogFragment(order: Int) {
        val newFragment = NewRecordDialogFragment.newInstance(order)
        newFragment.show(childFragmentManager, "new_record_dialog")
    }

    companion object {
        private const val LOG_TAG = "RecordFragment"
    }
}