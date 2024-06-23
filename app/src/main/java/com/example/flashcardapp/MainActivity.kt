package com.example.flashcardapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var words: MutableList<Word>
    private lateinit var adapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        words = loadWords().toMutableList()

        adapter = CardsAdapter(this, words)
        val frame = findViewById<SwipeFlingAdapterView>(R.id.frame)
        frame.adapter = adapter

        frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                if (words.isNotEmpty()) {
                    words.removeAt(0)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onLeftCardExit(dataObject: Any) {}
            override fun onRightCardExit(dataObject: Any) {}
            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {}
            override fun onScroll(scrollProgressPercent: Float) {}
        })

        frame.setOnItemClickListener { _, _ ->
            val topView = frame.getSelectedView()
            val tvWord = topView.findViewById<TextView>(R.id.tvWord)
            val word = words[0]
            tvWord.text = if (tvWord.text == word.english) word.turkish else word.english
        }
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
}
