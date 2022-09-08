package com.android.calculator

import androidx.lifecycle.*
import com.android.calculator.model.History
import com.android.calculator.model.HistoryDao
import kotlinx.coroutines.*
import javax.script.ScriptEngineManager

class MainActivityViewModel(private val historyDao: HistoryDao) : ViewModel() {

    // 연산자 List
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


    // 계산식, 계산결과 초기화
    fun clear() {
        liveDataFormula.value = "0"
        liveDataResult.value = ""
    }

    // 계산식 (숫자, 연산자)입력
    fun appendFormula(item: String) {
        val currentString = liveDataFormula.value ?: "0"

        if(currentString == "0" && item.toIntOrNull() == 0) return  // 처음 입력한 숫자가 0인 경우

        if(operatorList.contains(item)) {
            val lastItem = currentString.last().toString()
            if(operatorList.contains(currentString.last().toString()) && operatorList.contains(item)) {
                removeFormula()             // 계산식에 연속으로 연산자가 들어온 경우
            } else if(lastItem == ".") {
                return                      // 마지막에 입력된 것이 .인 경우
            }
        }

        if(item == ".") {
            val lastOperatorIndex = currentString.lastIndexOfAny(operatorList)
            if(lastOperatorIndex == currentString.length - 1) return                    // CASE : 10+

            val lastNumber =
                if(lastOperatorIndex == -1) currentString                               // CASE : 10
                else currentString.substring(lastOperatorIndex + 1)            // CASE : 10+10

            if(lastNumber.isNullOrEmpty() || lastNumber.contains(".")) return     // CASE : 10.1234

        } else {
            if(currentString == "0") liveDataFormula.value = ""     // 계산식에 처음 입력하는 경우
        }

        liveDataFormula.value = liveDataFormula.value?.plus(item)   // 입력
    }

    // BackSpace
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
            // 계산결과 소수점 두 번째까지 나타내기
            val result = "%.2f".format(it.toString().toDouble()).run {
                if (currentFormula.contains(".")) this
                else replace(".00", "")     // (자연수)소수점 2번째까지 모두 0인경우, 소수점 떼기
            }

            liveDataResult.value = result

            // Room Insert
            historyInsert(
                History(
                    history_formula = currentFormula,
                    history_result = "= $result"
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