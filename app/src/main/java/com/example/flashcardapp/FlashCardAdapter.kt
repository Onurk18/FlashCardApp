package com.example.flashcardapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FlashCardAdapter(fa: FragmentActivity, private val flashCards: List<MainActivity.FlashCard>) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = flashCards.size

    override fun createFragment(position: Int): Fragment {
        val flashCard = flashCards[position]
        return FlashCardFragment.newInstance(flashCard.english, flashCard.turkish)
    }
}
