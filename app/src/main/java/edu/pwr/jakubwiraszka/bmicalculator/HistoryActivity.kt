package edu.pwr.jakubwiraszka.bmicalculator

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import edu.pwr.jakubwiraszka.bmicalculator.databinding.ActivityMainBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var history: BMIHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        history = BMIHistory(this)

        val historyListView: ListView = findViewById(R.id.historyListView)

        val goBackBtn: Button = findViewById(R.id.prevButton)
        goBackBtn.setOnClickListener { finish() }

        val clearBtn: Button = findViewById(R.id.clearButton)
        clearBtn.setOnClickListener {
            history.clearHistory()
            history.saveHistory(this)
            updateHistory(historyListView)
        }

        updateHistory(historyListView)
    }

    private fun updateHistory(historyListView: ListView) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            history.getHistory()
                .map {
                    "${it.date}:\nWeight: ${it.weight}${if (it.isImperial) "lbs" else "kg"} Height: ${it.height}${if (it.isImperial) "in" else "cm"} BMI: ${it.bmi}"
                })
        historyListView.adapter = adapter
    }
}
