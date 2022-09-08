package com.android.calculator

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.text.DecimalFormat

class SpiltActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val sApplication = application

    var liveDataCount = MutableLiveData<String>("1")      // N
    var liveDataPrice = MutableLiveData<String>()              // N등분할 금액
    var liveDataResult = MutableLiveData<String>("0")    // N등분 이후의 금액


    fun goDutch() {
        val df = DecimalFormat("#,###")

        if (liveDataPrice.value?.isNullOrEmpty() == false && liveDataPrice.value?.isNullOrBlank() == false) {
            val price = liveDataPrice.value?.replace(",", "")
            liveDataResult.value = df.format(price?.toInt()!! / liveDataCount.value?.toInt()!!)
        } else {
            liveDataResult.value = "0"
        }
    }

    fun increaseClick(view: View) {
        liveDataCount.value = liveDataCount.value?.toInt()?.plus(1).toString()
        goDutch()
    }

    fun decreaseClick(view: View) {
        if(liveDataCount.value!! <= "1") {
            Toast.makeText(sApplication, "1미만으로 나눌 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        liveDataCount.value = liveDataCount.value?.toInt()?.minus(1).toString()
        goDutch()
    }

}