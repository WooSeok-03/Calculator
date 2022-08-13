package com.android.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.calculator.databinding.ActivityMainBinding
import com.android.calculator.model.History

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        initialize()
        historyLayoutOnOff()
        showHistory()
    }

    private fun initialize() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // RecyclerView init
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.scrollToPosition(adapter.itemCount -1)   // 스크롤 최하단부터 보여주기
    }

    private fun historyLayoutOnOff() {
        // HistoryLayout on/off
        viewModel.historyState.observe(this, Observer {
            if(binding.historyLayout.visibility == View.GONE) {
                binding.historyLayout.visibility = View.VISIBLE
            } else {
                binding.historyLayout.visibility = View.GONE
            }
        })
    }

    private fun showHistory() {
        viewModel.history.observe(this, Observer {
            adapter.setList(it)
            binding.recyclerView.scrollToPosition(adapter.itemCount -1)
        })
    }

    override fun onBackPressed() {
        if(binding.historyLayout.visibility == View.VISIBLE) {
            binding.historyLayout.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}