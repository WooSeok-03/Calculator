package com.android.calculator

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val liveDataFormula = MutableLiveData<String>()
    val formula : LiveData<String>
    get() = liveDataFormula

    init {
        liveDataFormula.value = "0"
    }

    fun buttonClick(view: View) {
        if (liveDataFormula.value == null) liveDataFormula.value = "0"
        when(view.id) {
            R.id.bt_clear -> liveDataFormula.value = "0"
            R.id.bt_plus, R.id.bt_minus, R.id.bt_multiplication, R.id.bt_division -> operatorClick(view.id)
            R.id.bt_backspace -> liveDataFormula.value = "개발중..."
            R.id.bt_equals -> liveDataFormula.value = "개발중..."
            R.id.bt_dot -> numberClick(".")
            R.id.bt_double_zero -> numberClick("00")
            R.id.bt_zero -> numberClick("0")
            R.id.bt_one -> numberClick("1")
            R.id.bt_two -> numberClick("2")
            R.id.bt_three -> numberClick("3")
            R.id.bt_four -> numberClick("4")
            R.id.bt_five -> numberClick("5")
            R.id.bt_six -> numberClick("6")
            R.id.bt_seven -> numberClick("7")
            R.id.bt_eight -> numberClick("8")
            R.id.bt_nine -> numberClick("9")
        }
    }

    fun numberClick(number: String) {
        if (liveDataFormula.value == "0") {
            when(number) {
                "1" -> liveDataFormula.value = "1"
                "2" -> liveDataFormula.value = "2"
                "3" -> liveDataFormula.value = "3"
                "4" -> liveDataFormula.value = "4"
                "5" -> liveDataFormula.value = "5"
                "6" -> liveDataFormula.value = "6"
                "7" -> liveDataFormula.value = "7"
                "8" -> liveDataFormula.value = "8"
                "9" -> liveDataFormula.value = "9"
                else -> liveDataFormula.value = "0"
            }
        } else {
            when(number) {
                "." -> liveDataFormula.value = liveDataFormula.value.plus(".")
                "00" -> liveDataFormula.value = liveDataFormula.value.plus("00")
                "0" -> liveDataFormula.value = liveDataFormula.value.plus("0")
                "1" -> liveDataFormula.value = liveDataFormula.value.plus("1")
                "2" -> liveDataFormula.value = liveDataFormula.value.plus("2")
                "3" -> liveDataFormula.value = liveDataFormula.value.plus("3")
                "4" -> liveDataFormula.value = liveDataFormula.value.plus("4")
                "5" -> liveDataFormula.value = liveDataFormula.value.plus("5")
                "6" -> liveDataFormula.value = liveDataFormula.value.plus("6")
                "7" -> liveDataFormula.value = liveDataFormula.value.plus("7")
                "8" -> liveDataFormula.value = liveDataFormula.value.plus("8")
                "9" -> liveDataFormula.value = liveDataFormula.value.plus("9")
            }
        }
    }

    fun operatorClick(viewId : Int) {
        val formulaLength = liveDataFormula.value?.length
        if (liveDataFormula.value == "0") return

        if (viewId == R.id.bt_plus) liveDataFormula.value = liveDataFormula.value.plus("+")
        else if(viewId == R.id.bt_minus) liveDataFormula.value = liveDataFormula.value.plus("-")
        else if(viewId == R.id.bt_multiplication) liveDataFormula.value = liveDataFormula.value.plus("×")
        else if(viewId == R.id.bt_division) liveDataFormula.value = liveDataFormula.value.plus("÷")
    }

}