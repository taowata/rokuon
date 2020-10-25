package com.example.rokuon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rokuon.databinding.RecordListItemBinding

class RecordListAdapter(
    private val itemClickAction: (Record) -> Unit
) : RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private var records: List<Record> = mutableListOf(Record())

    class RecordViewHolder(private val binding: RecordListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record, onItemClick: (Record) -> Unit) {
            binding.record = item
            binding.onItemClick = onItemClick
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
        holder.bind(current, itemClickAction)
    }

    override fun getItemCount(): Int = records.size

    fun setRecords(records: List<Record>) {
        this.records = records
        notifyDataSetChanged()
    }

}