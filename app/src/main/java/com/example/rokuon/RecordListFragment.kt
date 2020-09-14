package com.example.rokuon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

//        viewModel.recordList.observe(viewLifecycleOwner) {
//            adapter.setRecords(it)
//            recyclerView.adapter = adapter
//        }
        return binding.root
    }
}