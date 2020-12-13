package com.example.rokuon

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rokuon.databinding.FragmentRecordBinding
import com.linecorp.lich.component.getComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

class RecordFragment : Fragment() {
    private val args: RecordFragmentArgs by navArgs()

    // デシベルベースラインの設定
    val dB_baseline: Double = 12.0

    // 分解能の計算
    private val bufSize = AudioRecord.getMinBufferSize(
        SAMPLING_RATE,
        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
    )

    private lateinit var audioRecord: AudioRecord
    private lateinit var visualizer: Visualizer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecordBinding.inflate(layoutInflater, container, false)

        // viewModelの初期化
        val audioRecorder = AudioRecorder()
        val context = requireContext()
        val dataSource = context.getComponent(RecordDatabase).recordDao
        val recordViewModelFactory = RecordViewModelFactory(audioRecorder, dataSource)
        val recordViewModel: RecordViewModel by viewModels { recordViewModelFactory }

        val newRecordId = args.recordId
        // IDを使って録音ファイル名をつける
        viewLifecycleOwner.lifecycleScope.launch {
            recordViewModel.updateRecordName(newRecordId)
        }

        val recordFile =
            RecordFileManager.getRecordFile(context, newRecordId) ?: error("RecordFile is missing")

        val startButton = binding.startRecordButton
        recordViewModel.startButtonVisibility.observe(viewLifecycleOwner) {
            startButton.visibility = it
        }
        startButton.setOnClickListener {
            recordViewModel.startRecording(recordFile.absolutePath)
        }

        val pauseButton = binding.pauseButton
        recordViewModel.pauseButtonVisibility.observe(viewLifecycleOwner) {
            pauseButton.visibility = it
        }
        pauseButton.setOnClickListener {
            recordViewModel.pauseRecording()
        }

        val resumeButton = binding.resumeButton
        recordViewModel.resumeButtonVisibility.observe(viewLifecycleOwner) {
            resumeButton.visibility = it
        }
        resumeButton.setOnClickListener {
            recordViewModel.resumeRecording()
        }

        val finishButton = binding.finishButton
        recordViewModel.finishButtonVisibility.observe(viewLifecycleOwner) {
            finishButton.visibility = it
        }
        finishButton.setOnClickListener {
            // 録音時間を保存する
            viewLifecycleOwner.lifecycleScope.launch {
                recordViewModel.updateRecordTime(newRecordId, recordFile.absolutePath)
            }
            recordViewModel.finishRecording()
            findNavController().navigate(R.id.action_recordFragment_to_recordListFragment)
        }

        val progressBar = binding.recordingBar
        recordViewModel.recordingBarVisibility.observe(viewLifecycleOwner) {
            progressBar.visibility = it
        }

        val timeText = binding.timeText
        recordViewModel.recordingTime.observe(viewLifecycleOwner) {
            timeText.text = it.toString()
        }

        startMeasureDb(binding.fftview)


        return binding.root
    }

    fun startMeasureDb(fftView: FftView) {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            8000, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT, bufSize
        )

        audioRecord.startRecording()

        visualizer = Visualizer(audioRecord.audioSessionId)
        visualizer.enabled = false
        visualizer.captureSize = Visualizer.getCaptureSizeRange()[1]

        //キャプチャしたデータを定期的に取得するリスナーを設定
        visualizer.setDataCaptureListener(
            object : Visualizer.OnDataCaptureListener {
                //Wave形式のキャプチャーデータ
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer, bytes: ByteArray,
                    samplingRate: Int
                ) {
                    fftView.update(bytes)
                }

                //高速フーリエ変換のキャプチャーデータ
                override fun onFftDataCapture(
                    visualizer: Visualizer, bytes: ByteArray,
                    samplingRate: Int
                ) {
                }
            },
            Visualizer.getMaxCaptureRate() / 2,  //キャプチャーデータの取得レート（ミリヘルツ）
            true,  //これがTrueだとonWaveFormDataCaptureにとんでくる
            false
        ) //これがTrueだとonFftDataCaptureにとんでくる

        visualizer.enabled = true
    }

    companion object {
        const val SAMPLING_RATE = 44100
    }
}