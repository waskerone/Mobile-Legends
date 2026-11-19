package com.example.wakasugi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    fun toProfile(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun toMedia(view: View){
        val intent = Intent(this, MediaActivity::class.java)
        startActivity(intent)
    }

    fun toRandom(view: View){
        val intent = Intent(this, RandomActivity::class.java)
        startActivity(intent)
    }

    fun exit(view: View){
        finishAffinity()
    }
}