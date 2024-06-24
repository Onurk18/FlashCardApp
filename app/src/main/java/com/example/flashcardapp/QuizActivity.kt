package com.example.flashcardapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException


class QuizActivity : AppCompatActivity() {

    private lateinit var words: List<Word>
    private lateinit var correctWord: Word
    private lateinit var options: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        words = loadWords()
        if (words.size<4) {
            Toast.makeText(this, "You need to learn before quiz", Toast.LENGTH_SHORT).show()
            finish()
            return
        }



        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        val rgOptions = findViewById<RadioGroup>(R.id.rgOptions)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        generateQuestion()

        tvQuestion.text = "What is the Turkish meaning of '${correctWord.english}'?"

        rgOptions.removeAllViews()
        options.forEach { option ->
            val radioButton = RadioButton(this)
            radioButton.text = option
            rgOptions.addView(radioButton)
        }

        btnSubmit.setOnClickListener {
            val selectedOption = rgOptions.checkedRadioButtonId
            if (selectedOption != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedOption)
                val selectedText = selectedRadioButton.text
                if (selectedText == correctWord.turkish) {
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                } else {
                    correctWord.learningLevel = 1
                    MainActivity.saveWord(filesDir, correctWord)
                    Toast.makeText(this, "Wrong! Correct answer is ${correctWord.turkish}, Practice with flash Cards", Toast.LENGTH_SHORT).show()
                }
                generateQuestion()
                tvQuestion.text = "What is the Turkish meaning of '${correctWord.english}'?"
                rgOptions.removeAllViews()
                options.forEach { option ->
                    val radioButton = RadioButton(this)
                    radioButton.text = option
                    rgOptions.addView(radioButton)
                }
            } else {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateQuestion() {
        val randomIndex = words.indices.random()
        correctWord = words[randomIndex]

        options = mutableListOf(correctWord.turkish)
        while (options.size < 4) {
            val randomOption = words.random().turkish
            if (randomOption != correctWord.turkish && !options.contains(randomOption)) {
                options.add(randomOption)
            }
        }
        options.shuffle()
    }

    private fun loadWords(): List<Word> {
        val jsonString: String
        try {
            jsonString = File(filesDir, "words.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }
        val listType = object : TypeToken<List<Word>>() {}.type
        return Gson().fromJson<List<Word>?>(jsonString, listType).filter { it.learningLevel == 2 }
    }
}
