package com.android.calculator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var history_formula: String,
    var history_result: String,
)
