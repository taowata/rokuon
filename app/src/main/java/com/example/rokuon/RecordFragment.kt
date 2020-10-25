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
        val recordButton = binding.recordButton
        recordButton.setOnClickListener {
            recordViewModel.onClickRecordButton(recordFile.absolutePath)
        }

        val pauseButton = binding.pauseButton
        pauseButton.setOnClickListener {
            recordViewModel.onClickPauseButton()
        }

        val resumeButton = binding.resumeButton
        resumeButton.setOnClickListener {
            recordViewModel.onClickResumeButton()
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