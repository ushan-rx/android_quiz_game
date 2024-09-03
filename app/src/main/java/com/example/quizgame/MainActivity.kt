package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var opSelectSpinner: Spinner
    private lateinit var difSelectSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize spinners
        opSelectSpinner = findViewById(R.id.op_select)
        difSelectSpinner = findViewById(R.id.dif_select)

        // Populate spinners with data
        val operators = arrayOf(getString(R.string.operator1), getString(R.string.operator2), getString(R.string.operator3))
        val difficulties = arrayOf(getString(R.string.difficulty1), getString(R.string.difficulty2), getString(R.string.difficulty3))

        val operatorAdapter = ArrayAdapter(this, R.layout.custom_spinner, operators)
        val difficultyAdapter =
            ArrayAdapter(this, R.layout.custom_spinner, difficulties)
        // set values to spinners
        opSelectSpinner.adapter = operatorAdapter
        difSelectSpinner.adapter = difficultyAdapter


        // Handle the "Start Game" button click
        val startButton: Button = findViewById(R.id.btn_start)
        startButton.setOnClickListener {
            // Get the selected difficulty
            val selectedDifficulty = difficulties[difSelectSpinner.selectedItemPosition]

            // Start game play activity only when the button is clicked
            if (selectedDifficulty.isNotEmpty()) {
                //navigate to quiz page with values needed
                val intent = Intent(this@MainActivity, QuizPage::class.java)
                intent.putExtra("timeInMillis", getTimeInMillis(selectedDifficulty))
                intent.putExtra("operator", operators[opSelectSpinner.selectedItemPosition])
                startActivity(intent)
            }

        }
    }

    private fun getTimeInMillis(selectedDifficulty: String): Long {
        return when (selectedDifficulty) {
            "Easy (4 min)" -> 240_000L
            "Medium (2 min)" -> 120_000L
            "Hard (1 min)" -> 60_000L
            else -> 60_000L
        }
    }


}