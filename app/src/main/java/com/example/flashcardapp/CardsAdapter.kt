package com.example.flashcardapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CardsAdapter(private val context: Context, private val words: List<Word>) : BaseAdapter() {

    override fun getCount(): Int {
        return words.size
    }

    override fun getItem(position: Int): Any {
        return words[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        val word = words[position]
        val tvWord = view.findViewById<TextView>(R.id.tvWord)
        tvWord.text = word.english
        return view
    }
}
