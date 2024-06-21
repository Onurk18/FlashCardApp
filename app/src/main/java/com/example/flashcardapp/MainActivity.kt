package com.example.flashcardapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flashcardapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var flashCards: List<FlashCard>
    data class FlashCard(val english: String, val turkish: String)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        flashCards = loadFlashCards()
        setupViewPager()
    }

    private fun loadFlashCards(): List<FlashCard> {
        val inputStream = resources.openRawResource(R.raw.words)
        val reader = InputStreamReader(inputStream)
        val flashCardType = object : TypeToken<List<FlashCard>>() {}.type
        return Gson().fromJson(reader, flashCardType)
    }

    private fun setupViewPager() {
        val adapter = FlashCardAdapter(this, flashCards)
        binding.viewPager.adapter = adapter
    }
}
