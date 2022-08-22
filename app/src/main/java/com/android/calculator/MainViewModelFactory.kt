package com.android.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.calculator.model.HistoryDatabase

class MainViewModelFactory(private val historyDatabase: HistoryDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(historyDatabase.historyDao()) as T
    }
}