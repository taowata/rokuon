package com.example.rokuon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rokuon.databinding.FragmentRecordBinding

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
        val recordFile = RecordFileManager.getRecordFile(context, newRecordId) ?: error("RecordFile is missing")
        val recordButton = binding.recordButton
        recordButton.setOnClickListener {
            recordViewModel.onClickRecordButton(recordFile.absolutePath)
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