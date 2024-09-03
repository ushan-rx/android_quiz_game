package com.example.quizgame

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class QuizPage : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel
    private val quizHelper = QuizHelper() // helper class to generate quizes


    private lateinit var mediaPlayer: MediaPlayer // Declare MediaPlayer

    private lateinit var qusetionTextView: TextView
    private lateinit var scoreTextView : TextView
    private lateinit var answerButton1: Button
    private lateinit var answerButton2: Button
    private lateinit var answerButton3: Button
    private lateinit var answerButton4: Button
    private lateinit var nextButton: Button
    private lateinit var homeButton: Button
    private lateinit var timerTxt : TextView
    private lateinit var countdownTimer: CountDownTimer

    private var correctAnswer: Int = 0
    private var score: Int = 0
    private var initialTimeInMillis: Long = 0L
    private var num1: Int = 0
    private var num2: Int = 0
    private var operator: String = "+"
    private var isPressed: Boolean = false
    private var quizCount: Int = 1
    private var wrongAnswers: Int = 0
    private var BASE_MARK: Int = 2  // mark per correct answer
    private var quizMarker: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_page)

        //initialize elements
        timerTxt = findViewById(R.id.txt_timer)
        qusetionTextView = findViewById(R.id.txt_display_quiz)
        scoreTextView = findViewById(R.id.txt_score)
        answerButton1 = findViewById(R.id.btn_ans1)
        answerButton2 = findViewById(R.id.btn_ans2)
        answerButton3 = findViewById(R.id.btn_ans3)
        answerButton4 = findViewById(R.id.btn_ans4)
        nextButton = findViewById(R.id.btn_next)
        homeButton = findViewById(R.id.btn_home)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        // Initialize MediaPlayer with the sound resource
        mediaPlayer = MediaPlayer.create(this, R.raw.gameover)

        // Set the initial time
        initialTimeInMillis = intent.getLongExtra("timeInMillis", 60_000L)
        //set operator
        operator = when (intent.getStringExtra("operator")) {
            "Addition" -> "+"
            "Subtraction" -> "-"
            "Multiplication" -> "*"
            else -> "+"
        }

        //start timer
        countdownTimer = object : CountDownTimer(initialTimeInMillis, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                //set the time color to red when the time goes lower 10sec
                if(secondsRemaining < 10){
                    timerTxt.setTextColor(Color.RED)
                }
                // display timer
                timerTxt.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                showTimeUpDialog()
                mediaPlayer.start()
            }
        }
        //start timer
        countdownTimer.start()
        setupQuiz()
        // next question button
        nextButton.setOnClickListener {
            quizCount++
            setupQuiz()
        }
        //handle home button navigation
        homeButton.setOnClickListener {
            val intent = Intent(this@QuizPage, MainActivity::class.java)
            countdownTimer.cancel()
            startActivity(intent)
            finish()
        }

        // Set up answer buttons
        answerButton1.setOnClickListener { checkAnswer(answerButton1.text.toString().toInt()) }
        answerButton2.setOnClickListener { checkAnswer(answerButton2.text.toString().toInt()) }
        answerButton3.setOnClickListener { checkAnswer(answerButton3.text.toString().toInt()) }
        answerButton4.setOnClickListener { checkAnswer(answerButton4.text.toString().toInt()) }
        
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer.cancel() // Cancel the timer when the activity is destroyed
        mediaPlayer.release()
    }

    private fun showTimeUpDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.time_expired))
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                // Handle OK button click
                dialog.dismiss()
                //navigate to result activity
                val intent = Intent(this@QuizPage, ScoreScreen::class.java)
                intent.putExtra("Score", score)
                intent.putExtra("quizCount", quizCount)
                intent.putExtra("operator", operator)
                intent.putExtra("wrongCount", wrongAnswers)
                intent.putExtra("difficulty", initialTimeInMillis.toString())
                startActivity(intent)
                finish()
            }
            .setCancelable(false) // Prevent dismissing the dialog by tapping outside
        val dialog = builder.create()
        dialog.show()
    }

    // generate quiz and display answers and question
    @SuppressLint("SetTextI18n")
    private fun setupQuiz() {
        isPressed = false
        val values: MutableList<Int> =  quizHelper.generateQuiz(quizCount, operator)
        correctAnswer = quizHelper.correctAnswer
        num1 = quizHelper.num1
        num2 = quizHelper.num2
        resetButtonColors()
        qusetionTextView.setTextColor(Color.BLACK)
        //set answer values to the buttons
        answerButton1.text = values[0].toString()
        answerButton2.text = values[1].toString()
        answerButton3.text = values[2].toString()
        answerButton4.text = values[3].toString()
        // display question
        qusetionTextView.textSize = 50F
        qusetionTextView.text = "$num1 $operator $num2"
    }

    //reset button colors to normal
    private fun resetButtonColors() {
        answerButton1.setBackgroundResource(R.drawable.btn_ans_background)
        answerButton2.setBackgroundResource(R.drawable.btn_ans_background)
        answerButton3.setBackgroundResource(R.drawable.btn_ans_background)
        answerButton4.setBackgroundResource(R.drawable.btn_ans_background)
    }

    @SuppressLint("SetTextI18n")
    private fun checkAnswer(selectedAnswer: Int) {
        if(!isPressed){
            isPressed = true
            if (selectedAnswer == correctAnswer) {
                 score += BASE_MARK
                // Update score display
                qusetionTextView.textSize = 40F
                qusetionTextView.setTextColor(Color.GREEN)
                scoreTextView.text = "Score: $score"
                // Change correct answer button color to green
//                changeColor(correctAnswer)
            }else{
                qusetionTextView.textSize = 40F
                qusetionTextView.setTextColor(Color.RED)
                scoreTextView.text = "Score: $score"
                wrongAnswers++
            }
            qusetionTextView.text = "${qusetionTextView.text} = $correctAnswer"
        }
    }

//    //change color of the buttons according to the correct answer
//    private fun changeColor(correctAnswer: Int) {
//        answerButton1.setBackgroundResource(R.drawable.btn_incorrect_background)
////        answerButton1.requestLayout()
//        answerButton2.setBackgroundResource((R.drawable.btn_incorrect_background))
////        answerButton2.requestLayout()
//        answerButton3.setBackgroundResource(R.drawable.btn_incorrect_background)
////        answerButton3.requestLayout()
//        answerButton4.setBackgroundResource(R.drawable.btn_incorrect_background)
////        answerButton4.requestLayout()
//        // color correct answer
//        when (correctAnswer) {
//            answerButton1.text.toString()
//                .toInt() -> {
//                    answerButton1.setBackgroundResource(R.drawable.btn_correct_background)
//                    qusetionTextView.setTextColor(Color.GREEN)
//                }
//            answerButton2.text.toString()
//                .toInt() -> {
//                    answerButton2.setBackgroundResource(R.drawable.btn_correct_background)
//                    qusetionTextView.setTextColor(Color.GREEN)
//                }
//            answerButton3.text.toString()
//                .toInt() -> {
//                    answerButton3.setBackgroundResource(R.drawable.btn_correct_background)
//                    qusetionTextView.setTextColor(Color.GREEN)
//                }
//            answerButton4.text.toString()
//                .toInt() -> {
//                    answerButton4.setBackgroundResource(R.drawable.btn_correct_background)
//                    qusetionTextView.setTextColor(Color.GREEN)
//                }
//        }
//
////        answerButton1.requestLayout()
////        answerButton2.requestLayout()
////        answerButton3.requestLayout()
////        answerButton4.requestLayout()
//
//    }

}