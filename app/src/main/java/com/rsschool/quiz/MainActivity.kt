package com.rsschool.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rsschool.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), QuizFragment.OnQuizFragmentListener {

    // TODO : Create Result screen

    private lateinit var binding: ActivityMainBinding
    private var answersList = intArrayOf(-1, -1, -1, -1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openQuizFragment(0, answersList)

    }

    private fun openQuizFragment(position: Int, selectedOptions: IntArray) {
        val fragment = QuizFragment.newInstance(position, selectedOptions)
        supportFragmentManager
            .beginTransaction()
            .replace(binding.container.id, fragment)
            .commit()
    }

    override fun onQuizFragmentListener(position: Int, selectedOptions: IntArray) {
        openQuizFragment(position, selectedOptions)
    }
}