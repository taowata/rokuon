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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rokuon.databinding.FragmentRecordBinding
import java.io.IOException

class RecordFragment : Fragment() {

    private var dirPath: String = ""
    private var filePath: String = ""
    private var order: Int = 0

    private  lateinit var recorder: MediaRecorder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecordBinding.inflate(layoutInflater, container, false)

        // viewModelの初期化
        val recordViewModel: RecordViewModel by viewModels()
        val recordListViewModel: RecordListViewModel by activityViewModels()

        filePath = recordListViewModel.newRecord.filePath

        val recordButton = binding.recordButton
        recordViewModel.recordingState.observe(viewLifecycleOwner) { recordingState ->
            recordButton.setOnClickListener {
                if (recordingState == RecordingState.RECORDING) stopRecording() else startRecording(filePath)
                recordViewModel.onClickRecordButton()
            }
        }
        recordViewModel.recordingTag.observe(viewLifecycleOwner) {
            recordButton.text = it
        }
        return binding.root
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
    }

    companion object {
        private const val LOG_TAG = "RecordFragment"
    }
}