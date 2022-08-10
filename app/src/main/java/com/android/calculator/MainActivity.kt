package com.android.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private val sampleList = listOf(
        HistoryData("1+1", "2"),
        HistoryData("1+2", "3"),
        HistoryData("1+3", "4"),
        HistoryData("1+4", "5"),
        HistoryData("1+5", "6"),
        HistoryData("1+6", "7"),
        HistoryData("1+7", "8"),
        HistoryData("1+8", "9"),
        HistoryData("1+9", "10"),
        HistoryData("1+0", "1"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        initialize()
    }

    private fun initialize() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // RecyclerView init
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = HistoryAdapter(sampleList)
        binding.recyclerView.scrollToPosition(sampleList.size -1)   // 스크롤 최하단부터 보여주기
    }
}