package com.rsschool.quiz

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)