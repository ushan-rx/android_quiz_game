package com.example.quizgame

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("com.example.quizgame.USER_SCORE", Context.MODE_PRIVATE)

    fun saveScore(combinedKey: String, score: Int) {
        sharedPreferences.edit().putInt(combinedKey, score).apply()
    }

    fun getScore(combinedKey: String): Int {
        return sharedPreferences.getInt(combinedKey, 0)
    }

}