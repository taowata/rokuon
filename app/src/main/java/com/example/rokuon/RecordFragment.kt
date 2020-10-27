package com.example.rokuon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rokuon.databinding.FragmentRecordBinding
import com.linecorp.lich.component.getComponent
import kotlinx.coroutines.launch

class RecordFragment : Fragment() {
    private val args: RecordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecordBinding.inflate(layoutInflater, container, false)

        // viewModelの初期化
        val audioRecorder = AudioRecorder()
        val context = requireContext()
        val dataSource = context.getComponent(RecordDatabase).recordDao
        val recordViewModelFactory =  RecordViewModelFactory(audioRecorder, dataSource)
        val recordViewModel: RecordViewModel by viewModels { recordViewModelFactory }

        val newRecordId = args.recordId
        viewLifecycleOwner.lifecycleScope.launch {
            recordViewModel.updateRecord(newRecordId)
        }

        val recordFile = RecordFileManager.getRecordFile(context, newRecordId) ?: error("RecordFile is missing")

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

        return binding.root
    }
}