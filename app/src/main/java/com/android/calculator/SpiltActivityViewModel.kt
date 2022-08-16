package com.android.calculator

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.DecimalFormat

class SpiltActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val sApplication = application

    // N
    private val liveDataCount = MutableLiveData<String>("1")
    val countNumber : LiveData<String>
    get() = liveDataCount

    // N등분할 금액
    var liveDataPrice = MutableLiveData<String>()
//    val price : LiveData<String>
//    get() = liveDataPrice

    // N등분 이후의 금액
    private val liveDataResult = MutableLiveData<String>()
    val result : LiveData<String>
    get() = liveDataPrice


    fun increaseClick(view: View) {
        liveDataCount.value = liveDataCount.value?.toInt()?.plus(1).toString()
    }

    fun decreaseClick(view: View) {
        if(liveDataCount.value!! <= "1") {
            Toast.makeText(sApplication, "1미만으로 나눌 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        liveDataCount.value = liveDataCount.value?.toInt()?.minus(1).toString()
    }

}