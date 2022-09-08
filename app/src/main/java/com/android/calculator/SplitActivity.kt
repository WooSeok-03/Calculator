package com.android.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
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
            finish()
        }
    }

    private fun splitTheBill() {
        var price = ""
        val df = DecimalFormat("#,###")

        binding.etPrice.addTextChangedListener{
            if(!TextUtils.isEmpty(it.toString()) && it.toString() != price) {
                val tempPrice = it.toString().replace(",", "")
                price = df.format(tempPrice.toInt())
                binding.etPrice.setText(price)
                binding.etPrice.setSelection(price.length)
                splitViewModel.goDutch()
            } else {
                binding.splitResult.text = "0"
            }
        }
    }
}
