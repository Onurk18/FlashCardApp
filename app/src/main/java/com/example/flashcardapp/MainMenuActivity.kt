package com.example.flashcardapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val flashcardsButton = findViewById<Button>(R.id.btnFlashcards)
        val quizButton = findViewById<Button>(R.id.btnQuiz)
        val settingsButton = findViewById<Button>(R.id.btnSettings)

        flashcardsButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            // settings activity'yi başlat
        }

        quizButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }
}
