package com.android.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.calculator.databinding.ActivityMainBinding
import com.android.calculator.model.History
import com.android.calculator.model.HistoryDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var vm: MainActivityViewModel
    private lateinit var adapter: HistoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProvider(this, MainViewModelFactory(HistoryDatabase.getInstance(applicationContext)))
            .get(MainActivityViewModel::class.java)


        initialize()
        showHistory()
        goSplitActivity()

        with(binding) {
            val operator = listOf(
                btPlus to "+",
                btMinus to "-",
                btMultiplication to "*",
                btDivision to "/",
                btDot to "."
            )
            val numbers = listOf(
                btZero,
                btOne,
                btTwo,
                btThree,
                btFour,
                btFive,
                btSix,
                btSeven,
                btEight,
                btNine,
                btDoubleZero
            ).mapIndexed { index, view ->
                view to if(index < 10) "$index" else "00"
            }

            btClear.setOnClickListener { vm.clear() }
            btBackspace.setOnClickListener { vm.removeFormula() }
            btEquals.setOnClickListener { vm.evaluate() }

            (operator + numbers).forEach {
                it.first.setOnClickListener { _ -> vm.appendFormula(it.second) }
            }
        }

    }

    private fun initialize() {
        binding.viewModel = vm
        binding.lifecycleOwner = this

        // RecyclerView init
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.scrollToPosition(adapter.itemCount -1)   // 스크롤 최하단부터 보여주기

        adapter.setOnHistoryDeleteClickListner {
            vm.historyDelete(it)
        }
    }

    private fun showHistory() {
        // HistoryLayout on/off
        vm.historyState.observe(this, Observer {
            if(binding.historyLayout.visibility == View.GONE) {
                binding.historyLayout.visibility = View.VISIBLE
                binding.btHistory.setImageResource(R.drawable.ic_carculator_icon)
            } else {
                binding.historyLayout.visibility = View.GONE
                binding.btHistory.setImageResource(R.drawable.ic_history_icon)
            }
        })

        vm.history.observe(this, Observer {
            adapter.setList(it)
            binding.recyclerView.scrollToPosition(adapter.itemCount -1)
        })
    }

    private fun goSplitActivity() {
        binding.btSplit.setOnClickListener {
            val intent = Intent(this, SplitActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if(binding.historyLayout.visibility == View.VISIBLE) {
            binding.historyLayout.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}