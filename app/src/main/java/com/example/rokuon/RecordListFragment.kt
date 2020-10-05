package com.example.rokuon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rokuon.databinding.FragmentRecordListBinding
import com.linecorp.lich.component.getComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = FragmentRecordListBinding.inflate(layoutInflater, container, false)
        // RecyclerViewのセットアップ
        val recyclerView = binding.recordList
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val onItemClick: (Record) -> Unit = {
            val context = requireContext()
            val newRecordFile = RecordFileManager.getRecordFile(context, it.recordId) ?: error("RecordFile is missing")
            val action = RecordListFragmentDirections.actionRecordListFragmentToPlayFragment(newRecordFile.absolutePath)
            findNavController().navigate(action)
        }
        val adapter = RecordListAdapter(onItemClick)
        recyclerView.adapter = adapter

        // viewModelの初期化
        val context = requireContext()
        val dataSource = context.getComponent(RecordDatabase).recordDao
        val viewModelFactory = RecordListViewModelFactory(dataSource)
        val viewModel: RecordListViewModel by activityViewModels { viewModelFactory }

        viewModel.recordList.observe(viewLifecycleOwner) {
            adapter.setRecords(it)
            adapter.notifyDataSetChanged()
        }

        val fab = binding.fab
        val recordName = binding.editTextRecordName
        fab.setOnClickListener {
            val newRecord = Record(name = recordName.text.toString())
            // recordIdを取得してから画面遷移する
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val recordId: Long = viewModel.insertNewRecord(newRecord)
                val action = RecordListFragmentDirections.actionRecordListFragmentToRecordFragment(recordId)
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

    private fun showDialogFragment(order: Int) {
        val newFragment = NewRecordDialogFragment.newInstance(order)
        newFragment.show(childFragmentManager, "new_record_dialog")
    }
}