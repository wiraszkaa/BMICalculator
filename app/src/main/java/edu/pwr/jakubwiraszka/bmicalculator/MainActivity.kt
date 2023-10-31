package edu.pwr.jakubwiraszka.bmicalculator

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.pwr.jakubwiraszka.bmicalculator.databinding.ActivityMainBinding
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var history: BMIHistory
    private lateinit var binding: ActivityMainBinding
    private lateinit var weightLabel: TextView
    private lateinit var heightLabel: TextView
    private lateinit var weightInput: EditText
    private lateinit var heightInput: EditText
    private lateinit var bmiResult: TextView
    private var isImperial = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        history = BMIHistory(this)

        weightLabel = binding.weightLabel
        heightLabel = binding.heightLabel
        weightInput = binding.weightInput
        heightInput = binding.heightInput
        bmiResult = binding.bmiResult

        val calculateButton: Button = binding.calculateButton

        calculateButton.setOnClickListener {
            calculateBMI()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_metrics -> when (isImperial) {
                true -> {
                    isImperial = false
                    item.title = getString(R.string.imperial_metrics)
                    weightLabel.text = getString(R.string.weight_kg)
                    heightLabel.text = getString(R.string.height_centimeters)
                }
                false -> {
                    isImperial = true
                    item.title = getString(R.string.european_metrics)
                    weightLabel.text = getString(R.string.weight_lbs)
                    heightLabel.text = getString(R.string.height_inches)
                }
            }
            R.id.action_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun calculateBMI() {
        val weightText = weightInput.text.toString()
        val heightText = heightInput.text.toString()

        if (weightText.isEmpty() || heightText.isEmpty()) {
            // q: set back the default color for bmiResult
            val color = bmiResult.textColors
            bmiResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            bmiResult.text = getString(R.string.enter_both)
            return
        }

        val weight = weightText.toDouble()
        val height = heightText.toDouble()

        if (weight <= 0 || height <= 0) {
            bmiResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            bmiResult.text = getString(R.string.must_be_positive)
            return
        }

        // Calculate BMI
        val bmi = when (isImperial) {
            true -> round(weight * lbs2kg / (height * inches2cm * cm2m).pow(2))
            false -> round(weight / (height * cm2m).pow(2))
        }

        // Save the result
        val bmiEntry =
            BMIEntry(weight, height, bmi, isImperial, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).toString())
        history.addEntry(bmiEntry)
        history.saveHistory(this)

        // Display the result
        when (bmi) {
            in 0.0..18.5 -> bmiResult.setTextColor(Color.YELLOW)
            in 18.5..24.9 -> bmiResult.setTextColor(Color.GREEN)
            in 25.0..29.9 -> bmiResult.setTextColor(Color.YELLOW)
            else -> bmiResult.setTextColor(Color.RED)
        }
        bmiResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        bmiResult.text = getString(R.string.bmi_result, bmi.toString())
    }
}
