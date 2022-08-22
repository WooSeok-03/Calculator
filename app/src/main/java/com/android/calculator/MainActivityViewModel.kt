package com.android.calculator

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.android.calculator.model.History
import com.android.calculator.model.HistoryDao
import com.android.calculator.model.HistoryDatabase
import kotlinx.coroutines.*
import java.lang.NumberFormatException
import java.text.DecimalFormat
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class MainActivityViewModel(private val historyDao: HistoryDao) : ViewModel() {

    private val operatorList = listOf("+", "-", "*", "/")

    // 계산기록 보기/가리기
    private var lvHistoryState = MutableLiveData(false)
    val historyState : LiveData<Boolean>
    get() = lvHistoryState

    // 계산식 LiveData
    private val liveDataFormula = MutableLiveData<String>("0")
    val formula : LiveData<String>
    get() = liveDataFormula

    // 계산결과 LiveData
    private val liveDataResult = MutableLiveData<String>()
    val result : LiveData<String>
    get() = liveDataResult

    // 계산 기록 LiveData
    private val liveDataHistory = MutableLiveData<List<History>>()
    val history : LiveData<List<History>>
    get() = liveDataHistory

    private val scriptEngine by lazy { ScriptEngineManager().getEngineByName("rhino") }


    fun clear() {
        liveDataFormula.value = "0"
        liveDataResult.value = ""
    }

    fun appendFormula(item: String) {
        val currentString = liveDataFormula.value ?: "0"

        if(currentString == "0" && item.toIntOrNull() == 0) return

        if(operatorList.contains(item)) {
            val lastItem = currentString.last().toString()
            if(operatorList.contains(currentString.last().toString()) && operatorList.contains(item)) {
                removeFormula()
            } else if(lastItem == ".") {
                return
            }
        }

        if(item == ".") {
            val lastOperatorIndex = currentString.lastIndexOfAny(operatorList)
            if(lastOperatorIndex == currentString.length - 1) return                    // 10+

            val lastNumber =
                if(lastOperatorIndex == -1) currentString                               // 10
                else currentString.substring(lastOperatorIndex + 1)            // 10+10

            if(lastNumber.isNullOrEmpty() || lastNumber.contains(".")) return     // 10.1234
        } else {
            if(currentString == "0") liveDataFormula.value = ""
        }

        liveDataFormula.value = liveDataFormula.value?.plus(item)
    }

    fun removeFormula() {
        val currentString = liveDataFormula.value ?: return
        if(currentString.length > 1) {
            liveDataFormula.value = currentString.substring(0, currentString.length -1)
        } else {
            liveDataFormula.value = "0"
        }
    }

    fun evaluate() {
        val currentFormula = liveDataFormula.value ?: return
        scriptEngine.runCatching {
            eval(currentFormula)
        }.onSuccess {
            val result = "%.2f".format(it.toString().toDouble()).run {
                if (currentFormula.contains(".")) this
                else replace(".00", "")
            }

            liveDataResult.value = result

            historyInsert(
                History(
                    history_formula = currentFormula,
                    history_result = result
                )
            )
        }.onFailure { }
    }


    /* [ 계산 기록 ] */
    fun viewHistory() {
        lvHistoryState.value = lvHistoryState.value == true
        historyGetAll()
    }

    private fun historyGetAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = historyDao.getAll()
            liveDataHistory.postValue(items)
        }
    }

    private fun historyInsert(history: History) {
        viewModelScope.launch(Dispatchers.IO){
            historyDao.insert(history)
        }
    }

    fun historyDeleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            historyDao.deleteAll()
            liveDataHistory.postValue(listOf())
        }
    }

    fun historyDelete(history: History) = viewModelScope.launch(Dispatchers.IO) {
        historyDao.delete(history)
        historyGetAll()
    }
}