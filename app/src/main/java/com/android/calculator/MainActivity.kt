package com.android.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        viewModel.history.observe(this, Observer {
            adapter.setList(it)
        })
    }

    private fun initialize() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // RecyclerView init

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter()
        binding.recyclerView.adapter = adapter
        //binding.recyclerView.scrollToPosition(  -1)   // 스크롤 최하단부터 보여주기
    }
}