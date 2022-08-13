package com.android.calculator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.android.calculator.databinding.ItemHistoryBinding
import com.android.calculator.model.History
import com.android.calculator.model.HistoryDatabase
import kotlinx.coroutines.launch

class HistoryAdapter() : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.historyFormula.text = history.history_formula
            binding.historyResult.text = history.history_result
        }

    }

    private val items = ArrayList<History>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])

        holder.binding.btDelete.setOnClickListener {
            deleteList(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    fun setList(histories: List<History>) {
        items.clear()
        items.addAll(histories)
        notifyDataSetChanged()
    }

    fun deleteList(history: History) {
        items.remove(history)
        notifyDataSetChanged()
    }
}