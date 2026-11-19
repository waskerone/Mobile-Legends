package com.example.wakasugi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cloud1: ImageView = findViewById(R.id.cloud1)
        val translate1: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_cloud1)
        cloud1.startAnimation(translate1)

        val cloud2: ImageView = findViewById(R.id.cloud2)
        val translate2: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_cloud2)
        cloud2.startAnimation(translate2)

        val cloud3: ImageView = findViewById(R.id.cloud3)
        val translate3: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_cloud3)
        cloud3.startAnimation(translate3)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}