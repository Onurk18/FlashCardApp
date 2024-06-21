package com.example.flashcardapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.flashcardapp.R

class CardsAdapter(context: Context, private val words: List<Word>) :
    ArrayAdapter<Word>(context, R.layout.item_card, words) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        val word = words[position]
        val tvWord = view.findViewById<TextView>(R.id.tvWord)
        tvWord.text = word.english
        return view
    }
}
