package com.android.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.calculator.databinding.ActivitySplitBinding
import java.text.DecimalFormat

class SplitActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplitBinding
    private lateinit var splitViewModel: SpiltActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_split)
        splitViewModel = ViewModelProvider(this).get(SpiltActivityViewModel::class.java)

        initialize()
        backButtonClick()
        splitTheBill()

    }

    private fun initialize() {
        binding.splitViewModel = splitViewModel
        binding.lifecycleOwner = this
    }

    private fun backButtonClick() {
        binding.btGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun splitTheBill() {
        binding.etPrice.addTextChangedListener {
            splitViewModel.goDutch()
        }
    }
}
