package com.example.rokuon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rokuon.databinding.RecordListItemBinding

class RecordListAdapter : RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private var records = emptyList<Record>()

    class RecordViewHolder(private val binding: RecordListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record) {
            binding.record = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): RecordViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecordListItemBinding.inflate(layoutInflater, parent, false)
                return RecordViewHolder(
                    binding
                )
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val current = records[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int = records.size

    fun setRecords(records: List<Record>) {
        this.records = records
        notifyDataSetChanged()
    }

}