package com.android.calculator.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private var instance: HistoryDatabase? = null

        @Synchronized
        fun getInstance(context: Context): HistoryDatabase {
            if (instance == null) {
                synchronized(HistoryDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}