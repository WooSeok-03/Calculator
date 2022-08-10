package com.android.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.calculator.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyList: List<HistoryData>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryData) {
            binding.historyFormula.text = history.history_formula
            binding.historyResult.text = history.history_result
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size
}