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

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

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
            onDeleteClickCallBack?.invoke(items[position])
        }
    }

    private var onDeleteClickCallBack: ((History)-> Unit)? = null
    fun setOnHistoryDeleteClickListner(callback: (History)-> Unit) {
        onDeleteClickCallBack = callback
    }

    override fun getItemCount(): Int = items.size

    fun setList(histories: List<History>) {
        items.clear()
        items.addAll(histories)
        notifyDataSetChanged()
    }

}