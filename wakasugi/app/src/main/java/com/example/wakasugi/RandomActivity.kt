package com.example.wakasugi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RandomActivity : AppCompatActivity() {

    private var currentValue = 0
    private lateinit var tvNumber: TextView
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnRandom: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvNumber = findViewById(R.id.tvNumber)
        btnPlus = findViewById(R.id.btnPlus)
        btnMinus = findViewById(R.id.btnMinus)
        btnRandom = findViewById(R.id.btnRandom)

        updateNumber()

        btnPlus.setOnClickListener {
            currentValue++
            updateNumber()
        }

        btnMinus.setOnClickListener {
            if (currentValue > 0) {
                currentValue--
                updateNumber()
            }
        }

        btnRandom.setOnClickListener {
            val intent = Intent(this, ResultRandomActivity::class.java)
            intent.putExtra("max_value", currentValue)
            startActivity(intent)
        }
    }

    private fun updateNumber() {
        tvNumber.text = currentValue.toString()
    }
}