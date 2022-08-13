package com.android.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpiltActivityViewModel : ViewModel() {

    // N등분 이후의 금액
    private val liveDataResult = MutableLiveData<String>()
    val result : LiveData<String>
    get() = liveDataResult

    // N
    private val liveDataCount = MutableLiveData<String>()
    val countNumber : LiveData<String>
    get() = liveDataCount

}