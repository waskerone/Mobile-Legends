package com.example.wakasugi

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class ResultRandomActivity : AppCompatActivity() {

    private lateinit var tvRange: TextView
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_random)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvRange = findViewById(R.id.tvRange)
        tvResult = findViewById(R.id.tvResult)

        val maxValue = intent.getIntExtra("max_value", 0)

        tvRange.text = "Это случайное число между 0 и $maxValue"

        val randomValue = Random.nextInt(0, maxValue + 1)
        tvResult.text = randomValue.toString()
    }
}