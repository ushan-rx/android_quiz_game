package com.example.quizgame

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesHelper = SharedPreferencesHelper(application)

    // Methods to save and retrieve data
    fun saveScore(difficulty: String, operation: String, score: Int) {
        val combinedKey = "$difficulty$operation"
        sharedPreferencesHelper.saveScore(combinedKey, score)
    }

    fun getScore(difficulty: String, operation: String): Int {
        val combinedKey = "$difficulty$operation"
        return sharedPreferencesHelper.getScore(combinedKey)
    }
}