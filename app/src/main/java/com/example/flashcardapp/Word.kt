package com.example.flashcardapp

data class Word(
    val english: String,
    val turkish: String,
    var learningLevel: Int = 0
)
