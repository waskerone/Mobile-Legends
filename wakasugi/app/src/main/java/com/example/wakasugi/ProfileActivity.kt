package com.example.wakasugi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAge: EditText

    private val prefs by lazy { getSharedPreferences("profile_prefs", Context.MODE_PRIVATE) }
    private val PICK_IMAGE_REQUEST = 1

    companion object {
        const val PREFS_IMAGE_PATH = "imagePath" // Константа для ключа пути к изображению
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.imageView)
        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)
        etEmail = findViewById(R.id.etEmail)
        etAge = findViewById(R.id.etAge)
        val btnPickImage = findViewById<Button>(R.id.btnPickImage)

        // Загрузка сохранённых данных
        etName.setText(prefs.getString("name", ""))
        etSurname.setText(prefs.getString("surname", ""))
        etEmail.setText(prefs.getString("email", ""))
        etAge.setText(prefs.getString("age", ""))

        // Загрузка сохранённого изображения с правильным ключом
        prefs.getString(PREFS_IMAGE_PATH, null)?.let {
            val file = File(it)
            if (file.exists()) {
                imageView.setImageURI(Uri.fromFile(file))
            }
        }

        // Слушатели для автосохранения
        autoSave(etName, "name")
        autoSave(etSurname, "surname")
        autoSave(etEmail, "email")
        autoSave(etAge, "age")

        // Кнопка выбора изображения
        btnPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    private fun autoSave(editText: EditText, key: String) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                prefs.edit().putString(key, s.toString()).apply()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            imageUri?.let {
                val inputStream = contentResolver.openInputStream(it)
                val file = File(filesDir, "profile_image.jpg")
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                // Сохраняем путь с правильным ключом
                prefs.edit().putString(PREFS_IMAGE_PATH, file.absolutePath).apply()

                // Загружаем новое изображение в ImageView
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}