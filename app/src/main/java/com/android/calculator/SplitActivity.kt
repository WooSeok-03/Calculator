package com.android.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.calculator.databinding.ActivitySplitBinding

class SplitActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplitBinding
    private lateinit var splitViewModel: SpiltActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_split)
        splitViewModel = ViewModelProvider(this).get(SpiltActivityViewModel::class.java)

        initialize()
    }

    private fun initialize() {
        binding.splitViewModel = splitViewModel
        binding.lifecycleOwner = this
    }
}
