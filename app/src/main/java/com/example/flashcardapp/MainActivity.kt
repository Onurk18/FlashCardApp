package com.example.flashcardapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var words: MutableList<Word>
    private lateinit var adapter: CardsAdapter
    private lateinit var ivReturn: ImageView
    private lateinit var ivCheck: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyAssets()

        words = loadWords().toMutableList()
        words.shuffle(Random(System.currentTimeMillis()))

        adapter = CardsAdapter(this, words)
        val frame = findViewById<SwipeFlingAdapterView>(R.id.frame)
        frame.adapter = adapter

        ivReturn = findViewById(R.id.ivReturn)
        ivCheck = findViewById(R.id.ivCheck)

        frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                if (words.isNotEmpty()) {
                    words.removeAt(0)
                    adapter.notifyDataSetChanged()
                }else reloadWords()
            }

            override fun onLeftCardExit(dataObject: Any) {
                val word = dataObject as Word
                word.learningLevel = 1  // Not learned
                saveWord(filesDir, word)
            }
            override fun onRightCardExit(dataObject: Any) {
                val word = dataObject as Word
                word.learningLevel = 2  //  learned
                saveWord(filesDir, word)
            }
            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {}
            override fun onScroll(scrollProgressPercent: Float) {
                val alpha = Math.abs(scrollProgressPercent)
                if (scrollProgressPercent < 0) {
                    ivReturn.alpha = alpha
                    ivCheck.alpha = 0f
                } else {
                    ivReturn.alpha = 0f
                    ivCheck.alpha = alpha
                }
            }

        })

        frame.setOnItemClickListener { _, _ ->
            val topView = frame.selectedView
            val tvWord = topView.findViewById<TextView>(R.id.tvWord)
            val word = words[0]
            tvWord.text = if (tvWord.text == word.english) word.turkish else word.english
        }

    }
    private fun reloadWords() {
        words.clear()
        words.addAll(loadWords())
        words.shuffle(Random(System.currentTimeMillis()))
        adapter.notifyDataSetChanged()
    }
    private fun copyAssets() {
        val assetManager = assets
        val files = assetManager.list("") ?: return

        for (filename in files) {
            if (filename == "words.json") {
                val inputStream = assetManager.open(filename)
                val outFile = File(filesDir, filename)
                if (!outFile.exists()) {
                    val outputStream = outFile.outputStream()
                    inputStream.copyTo(outputStream)
                    inputStream.close()
                    outputStream.close()
                }
            }
        }
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
        return Gson().fromJson<List<Word>?>(jsonString, listType).filter { it.learningLevel == 0 ||it.learningLevel == 1 }
    }
    companion object {
        fun saveWord(fileDir: File, word: Word) {
            try {
                // Tüm kelimeleri oku
                val jsonString: String
                val file = File(fileDir, "words.json")
                jsonString = file.bufferedReader().use { it.readText() }

                val listType = object : TypeToken<MutableList<Word>>() {}.type
                val wordList: MutableList<Word> = Gson().fromJson(jsonString, listType)

                // İlgili kelimeyi güncelle
                val index = wordList.indexOfFirst { it.english == word.english }
                if (index != -1) {
                    wordList[index] = word
                }

                // Güncellenmiş listeyi dosyaya yaz
                val updatedJsonString = Gson().toJson(wordList)
                FileWriter(file).use { it.write(updatedJsonString) }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }
}
