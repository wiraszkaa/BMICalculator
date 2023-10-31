package edu.pwr.jakubwiraszka.bmicalculator

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BMIHistory(context: Context) {
    private val historyKey = "bmi_history"
    private var history: MutableList<BMIEntry> = mutableListOf()

    init {
        loadHistory(context)
    }

    fun getHistory(): MutableList<BMIEntry> {
        return history
    }

    fun addEntry(entry: BMIEntry) {
        history.add(entry)
    }

    fun clearHistory() {
        history.clear()
    }

    fun saveHistory(context: Context) {
        val sharedPref = context.getSharedPreferences(historyKey, Context.MODE_PRIVATE)
        val historyJson = Gson().toJson(history)
        sharedPref.edit().putString(historyKey, historyJson).apply()
    }

    private fun loadHistory(context: Context) {
        val sharedPref = context.getSharedPreferences(historyKey, Context.MODE_PRIVATE)
        val historyString = sharedPref.getString(historyKey, null)
        val historyList: MutableList<BMIEntry> =
            historyString?.let { parseHistoryString(it) } ?: mutableListOf()
        history = historyList
    }

    private fun parseHistoryString(historyString: String): MutableList<BMIEntry> {
        val listType = object : TypeToken<MutableList<BMIEntry>>() {}.type
        return Gson().fromJson(historyString, listType)
    }

}