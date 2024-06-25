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

        if (words.size < 4) {
            Toast.makeText(this, "You need to learn more words before taking the quiz.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        val btnOption1 = findViewById<Button>(R.id.btnOption1)
        val btnOption2 = findViewById<Button>(R.id.btnOption2)
        val btnOption3 = findViewById<Button>(R.id.btnOption3)
        val btnOption4 = findViewById<Button>(R.id.btnOption4)

        generateQuestion()

        tvQuestion.text = "What is the Turkish meaning of '${correctWord.english}'?"

        setOptionText(btnOption1, options[0])
        setOptionText(btnOption2, options[1])
        setOptionText(btnOption3, options[2])
        setOptionText(btnOption4, options[3])

        btnOption1.setOnClickListener { checkAnswer(btnOption1.text.toString()) }
        btnOption2.setOnClickListener { checkAnswer(btnOption2.text.toString()) }
        btnOption3.setOnClickListener { checkAnswer(btnOption3.text.toString()) }
        btnOption4.setOnClickListener { checkAnswer(btnOption4.text.toString()) }
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

    private fun setOptionText(button: Button, text: String) {
        button.text = text
    }

    private fun checkAnswer(selectedText: String) {
        if (selectedText == correctWord.turkish) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            correctWord.learningLevel = 1
            MainActivity.saveWord(filesDir, correctWord)
            Toast.makeText(this, "Wrong! Correct answer is ${correctWord.turkish}. Practice with Flash Cards.", Toast.LENGTH_SHORT).show()
        }
        generateQuestion()
        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        tvQuestion.text = "What is the Turkish meaning of '${correctWord.english}'?"
        val btnOption1 = findViewById<Button>(R.id.btnOption1)
        val btnOption2 = findViewById<Button>(R.id.btnOption2)
        val btnOption3 = findViewById<Button>(R.id.btnOption3)
        val btnOption4 = findViewById<Button>(R.id.btnOption4)
        setOptionText(btnOption1, options[0])
        setOptionText(btnOption2, options[1])
        setOptionText(btnOption3, options[2])
        setOptionText(btnOption4, options[3])
    }
}
