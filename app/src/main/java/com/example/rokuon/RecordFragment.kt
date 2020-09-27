package com.example.rokuon

import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rokuon.databinding.FragmentRecordBinding
import java.io.IOException

class RecordFragment : Fragment() {
    private val args: RecordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecordBinding.inflate(layoutInflater, container, false)

        // viewModelの初期化
        val audioRecorder = AudioRecorder()
        val recordViewModelFactory =  RecordViewModelFactory(audioRecorder)
        val recordViewModel: RecordViewModel by viewModels { recordViewModelFactory }

        val newRecordId = args.recordId
        val context = requireContext()
        val filePath = Constants.dirPath(context) + "/$newRecordId"
        val recordButton = binding.recordButton
        recordButton.setOnClickListener {
            recordViewModel.onClickRecordButton(filePath)
        }

        recordViewModel.recordingTag.observe(viewLifecycleOwner) {
            recordButton.text = it
        }

        val backToHomeButton = binding.backButton
        backToHomeButton.setOnClickListener {
            findNavController().navigate(R.id.action_recordFragment_to_recordListFragment)
        }
        return binding.root
    }
}

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

    fun releaseRecorder() {
        recorder?.release()
    }
}