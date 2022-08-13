package com.android.calculator

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.android.calculator.databinding.ItemHistoryBinding
import com.android.calculator.model.History
import com.android.calculator.model.HistoryDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryAdapter(private val context: Context) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

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

        // 삭제 버튼 클릭 시,
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

    private fun deleteList(history: History) {
        items.remove(history)   // RecyclerView에서 삭제

        // Room에서 삭제
        val historyDB = HistoryDatabase.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            historyDB?.historyDao()?.delete(history)
        }

        notifyDataSetChanged()
    }
}