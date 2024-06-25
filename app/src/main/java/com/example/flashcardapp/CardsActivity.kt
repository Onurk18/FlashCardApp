package com.example.flashcardapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException

class CardsActivity : AppCompatActivity() {

    private lateinit var learnedWordsAdapter: ArrayAdapter<String>
    private lateinit var notLearnedWordsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        val learnedWords = mutableListOf<String>()
        val notLearnedWords = mutableListOf<String>()

        val words = loadWords()


        for (word in words) {
            if (word.learningLevel == 2) {
                learnedWords.add(word.english + " - " + word.turkish)
            }
            if(word.learningLevel==1) {
                notLearnedWords.add(word.english + " - " + word.turkish)
            }
        }

        learnedWordsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, learnedWords)
        notLearnedWordsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notLearnedWords)

        val lvLearnedWords = findViewById<ListView>(R.id.lvLearnedWords)
        val lvNotLearnedWords = findViewById<ListView>(R.id.lvNotLearnedWords)

        lvLearnedWords.adapter = learnedWordsAdapter
        lvNotLearnedWords.adapter = notLearnedWordsAdapter
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
        return Gson().fromJson(jsonString, listType)
    }
}
