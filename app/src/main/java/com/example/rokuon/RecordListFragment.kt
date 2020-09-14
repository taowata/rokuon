package com.example.rokuon

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rokuon.databinding.FragmentRecordListBinding
import com.example.rokuon.databinding.RecordListItemBinding

class RecordListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = FragmentRecordListBinding.inflate(layoutInflater, container, false)
        // RecyclerViewのセットアップ
        val recyclerView = binding.recordList
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter = RecordListAdapter()
        recyclerView.adapter = adapter


        val dirPath = "${context?.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath}"

        // viewModelの初期化
        val activity = requireActivity()
        val dataSource = RecordDatabase.getInstance(activity.application).recordDao
        val viewModelFactory = RecordListViewModelFactory(dataSource)
        val viewModel: RecordListViewModel by activityViewModels { viewModelFactory }

        viewModel.recordList.observe(viewLifecycleOwner) {
            adapter.setRecords(it)
            adapter.notifyDataSetChanged()
        }

        val fab = binding.fab
        val recordName = binding.editTextRecordName
        fab.setOnClickListener {
            val order = viewModel.largestOrder.value ?: 0
            val filePath = dirPath + "$order"
            val newRecord = Record(
                name = recordName.text.toString(),
                filePath = filePath,
                recordOrder = order + 1,
                recordDate = Record.getDate()
            )
            viewModel.newRecord = newRecord
            findNavController().navigate(R.id.action_recordListFragment_to_recordFragment)
        }

        return binding.root
    }

    private fun showDialogFragment(order: Int) {
        val newFragment = NewRecordDialogFragment.newInstance(order)
        newFragment.show(childFragmentManager, "new_record_dialog")
    }
}