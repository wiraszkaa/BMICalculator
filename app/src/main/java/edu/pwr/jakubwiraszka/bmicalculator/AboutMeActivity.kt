package edu.pwr.jakubwiraszka.bmicalculator

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_me)

        val goBackBtn: Button = findViewById(R.id.prevButton)
        goBackBtn.setOnClickListener { finish() }
    }
}