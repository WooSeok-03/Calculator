package com.android.calculator

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.calculator.model.History
import com.android.calculator.model.HistoryDao
import com.android.calculator.model.HistoryDatabase
import kotlinx.coroutines.*
import java.lang.NumberFormatException
import java.text.DecimalFormat

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val mApplication = application

    private val operatorList = listOf<String>("+", "-", "×", "÷")
    private var operatorFlag = false
    private var equalsFlag = false
    private var dotFlag = false

    // 계산기록 보기/가리기
    private var lvHistoryState = MutableLiveData<Int>()
    val historyState : LiveData<Int>
    get() = lvHistoryState

    // 계산식 LiveData
    private val liveDataFormula = MutableLiveData<String>()
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

    init {
        liveDataFormula.value = "0"
        lvHistoryState.value = View.GONE
    }

    fun buttonClick(view: View) {

        if (liveDataFormula.value?.length!! >= 15) {
            Toast.makeText(mApplication, "15자리까지 입력할 수 있습니다.\n(숫자, 연산자 포함)", Toast.LENGTH_SHORT).show()
            return
        }

        when(view.id) {
            R.id.bt_clear -> {
                liveDataFormula.value = "0"
                liveDataResult.value = ""
            }
            R.id.bt_plus, R.id.bt_minus, R.id.bt_multiplication, R.id.bt_division -> {
                if (equalsFlag) return
                operatorClick(view.id)
            }
            R.id.bt_backspace -> liveDataFormula.value = backSpaceClick()
            R.id.bt_dot -> dotClick()
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
            R.id.bt_history -> viewHistory()
            R.id.bt_all_delete -> historyDeleteAll()
            else -> return
        }
    }

    private fun numberClick(number: String) {
        // 이전 계산이 TextView에 남아 있는 경우 ( equalsFlag == true )
        if (equalsFlag) {
            equalsFlag = false
            liveDataFormula.value = "0"
            liveDataResult.value = ""
        }

        // 계산식 TextView가 현재 아무것도 없는 상태에서 숫자 입력
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
                else -> return
            }
        }
    }

    private fun operatorClick(viewId : Int) {
        // 처음에는 연산자가 들어갈 수 없음 || 연산자 1개만 사용
        if (liveDataFormula.value == "0" || operatorFlag) return

        when (viewId) {
            R.id.bt_plus -> liveDataFormula.value = liveDataFormula.value.plus("+")
            R.id.bt_minus -> liveDataFormula.value = liveDataFormula.value.plus("-")
            R.id.bt_multiplication -> liveDataFormula.value = liveDataFormula.value.plus("×")
            R.id.bt_division -> liveDataFormula.value = liveDataFormula.value.plus("÷")
            else -> return
        }

        operatorFlag = true
        dotFlag = false
    }

    private fun dotClick() {
        // . 가 이미 들어간 수에는 입력할 수 없음
        if (dotFlag) return

        // 마지막 문자가 연산자인 경우, . 를 적을 수 없음
        for(i in operatorList) {
            if (liveDataFormula.value?.endsWith(i) == true) return
        }

        liveDataFormula.value = liveDataFormula.value.plus(".")
        dotFlag = true
    }

    private fun backSpaceClick(): String? {
        val currentFormula = liveDataFormula.value?.replaceFirst(".$".toRegex(), "")

        // 초기 값인 0일 때는 백스페이스 적용 X || 백스페이스한 후의 TextView가 비어있지 않도록 한다
        if(liveDataFormula.value == "0" || currentFormula == "") return "0"

        for (i in operatorList) {
            if(formula.value?.contains(i) == false) operatorFlag = false
        }
        return currentFormula
    }

    fun equalsClick() {
        lateinit var numberList : List<String>
        val df = DecimalFormat("#.##")  // 소수점 2자리수 까지만 나타내도록 format

        try {
            if(formula.value?.contains("+") == true){

                numberList = formula.value?.split("+")!!
                val plusFormula = numberList[0].toDouble() + numberList[1].toDouble()
                liveDataResult.value = df.format(plusFormula)

            } else if(formula.value?.contains("-") == true) {

                numberList = formula.value?.split("-")!!
                val minusFormula = numberList[0].toDouble() - numberList[1].toDouble()
                liveDataResult.value = df.format(minusFormula)

            } else if(formula.value?.contains("×") == true) {

                numberList = formula.value?.split("×")!!
                val multiplicationFormula = numberList[0].toDouble() * numberList[1].toDouble()
                liveDataResult.value = df.format(multiplicationFormula)

            } else if(formula.value?.contains("÷") == true) {

                numberList = formula.value?.split("÷")!!
                val divisionFormula = numberList[0].toDouble() / numberList[1].toDouble()
                liveDataResult.value = df.format(divisionFormula)

            } else {
                // 수식에서 연산자를 입력하지 않은 경우
                numberList = listOf("0")
                Toast.makeText(mApplication, "계산할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }
        } catch (e: NumberFormatException) {
            // 수식에서 연산자가 가장 마지막에 적혀있을 때, equalsClick()를 호출한 경우
            Toast.makeText(mApplication, "계산할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val historyFormula: String = formula.value.toString()
        val historyResult: String = result.value.toString()
        historyInsert(History(history_formula = historyFormula, history_result = historyResult))

        operatorFlag = false
        equalsFlag = true
        dotFlag = false
    }


    /* [ 계산 기록 ] */
    private fun viewHistory() {
        if (lvHistoryState.value != View.GONE) lvHistoryState.value = View.GONE
        else lvHistoryState.value = View.VISIBLE

        historyGetAll()
    }


    private fun historyGetAll() {
        val historyDB = HistoryDatabase.getInstance(mApplication)
        CoroutineScope(Dispatchers.IO).launch {
            val items = historyDB?.historyDao()?.getAll()
            withContext(Dispatchers.Main) {
                liveDataHistory.value = items
            }
        }

    }

    private fun historyInsert(history: History) {
        val historyDB = HistoryDatabase.getInstance(mApplication)
        CoroutineScope(Dispatchers.IO).launch {
            historyDB?.historyDao()?.insert(history)
        }
    }

    fun historyDelete(history: History) {
        val historyDB = HistoryDatabase.getInstance(mApplication)
        CoroutineScope(Dispatchers.IO).launch {
            historyDB?.historyDao()?.delete(history)
        }
    }

    private fun historyDeleteAll() {
        val historyDB = HistoryDatabase.getInstance(mApplication)
        CoroutineScope(Dispatchers.IO).launch {
            historyDB?.historyDao()?.deleteAll()
            withContext(Dispatchers.Main) {
                liveDataHistory.value = listOf()
            }
        }
    }
}