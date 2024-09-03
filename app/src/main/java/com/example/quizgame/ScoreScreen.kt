package com.example.quizgame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class ScoreScreen : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var quizCountTextView: TextView
    private lateinit var wrongCountTextView: TextView
    private lateinit var retryButton: Button

    private var score: Int = 0
    private var difficulty: String = "60_000L"
    private var quizCount: Int = 1
    private var wrongCount: Int = 0
    private var operator: String = "+"

    private lateinit var viewModel: QuizViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_screen)

        scoreTextView = findViewById(R.id.txt_current_score)
        highScoreTextView = findViewById(R.id.txt_high_score)
        quizCountTextView = findViewById(R.id.txt_quiz_count)
        wrongCountTextView = findViewById(R.id.txt_wrong_count)
        retryButton = findViewById(R.id.btn_retry)

        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        //get values from intent extras
        score = intent.getIntExtra("Score", 0)
        quizCount = intent.getIntExtra("quizCount", 0)
        wrongCount = intent.getIntExtra("wrongCount", 0)
        operator = when (intent.getStringExtra("operator")) {
            "+" -> "Addition"
            "-" -> "Subtraction"
            "*" -> "Multiplication"
            "/" -> "Division"
            else -> "Addition"
        }
        difficulty = intent.getStringExtra("difficulty").toString()

        // handle data operations
        val retrievedScore = viewModel.getScore(difficulty, operator)

        // If no score is saved, save the current score and display accordingly
        if (retrievedScore == 0) {
            viewModel.saveScore(difficulty, operator, score)
            highScoreTextView.text = getString(R.string.high_score) + " " + score
        } else {
            // Compare scores and update if current score is higher
            if (score > retrievedScore) {
                viewModel.saveScore(difficulty, operator, score)
                highScoreTextView.text = getString(R.string.high_score) + " " + score
            } else{
                highScoreTextView.text = getString(R.string.high_score) + " " + retrievedScore
            }
        }

        //set values to display
        scoreTextView.text = getString(R.string.display_score)+ " " +score
        quizCountTextView.text =  getString(R.string.quiz_count)+ " " +quizCount
        wrongCountTextView.text = getString(R.string.wrong_answers)+" "+wrongCount

        //retry action
        retryButton.setOnClickListener {
            val intent = Intent(this@ScoreScreen, MainActivity::class.java)
            startActivity(intent)
        }


    }
}