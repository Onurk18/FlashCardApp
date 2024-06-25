package com.example.flashcardapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val flashcardsButton = findViewById<Button>(R.id.btnFlashcards)
        val quizButton = findViewById<Button>(R.id.btnQuiz)
        val cardsButton = findViewById<Button>(R.id.btnCards)
        val resetButton = findViewById<Button>(R.id.btnResetLearnedWords)

        flashcardsButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cardsButton.setOnClickListener {
            val intent = Intent(this, CardsActivity::class.java)
            startActivity(intent)
        }

        quizButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        resetButton.setOnClickListener {
            resetLearnedWords()
        }
    }

    private fun resetLearnedWords() {
        val words = loadWords()
        words.forEach { it.learningLevel = 0 }
        saveWords(words)
    }

    private fun loadWords(): List<Word> {
        val jsonString: String
        try {
            jsonString = assets.open("words.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }
        val listType = object : TypeToken<List<Word>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    private fun saveWords(words: List<Word>) {
        val file = File(filesDir, "words.json")
        val updatedJsonString = Gson().toJson(words)
        FileWriter(file).use { it.write(updatedJsonString) }
    }
}
