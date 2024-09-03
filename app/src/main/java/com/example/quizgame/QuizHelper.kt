package com.example.quizgame

import kotlin.random.Random

class QuizHelper {

    var correctAnswer: Int = 0
    var num1:Int = 0
    var num2:Int = 0

    fun generateQuiz(quizCount: Int, operator: String): MutableList<Int> {

//        first 4 quizes
        if(quizCount < 6){
            num1 = Random.nextInt(5, 11) // Random number between 5 and 10
            num2 = Random.nextInt(5, 11)
        } else if(quizCount < 15){
            //generate two random operands
            num1 = Random.nextInt(10, 101) // Random number between 10 and 100
            num2 = Random.nextInt(10, 101)
        }else{
            //generate two random operands
            num1 = Random.nextInt(100, 1001) // Random number between 100 and 1000
            num2 = Random.nextInt(100, 1001)
        }


        // Calculate the correct answer
        correctAnswer = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            else -> 0
        }
        //generate incorrect answers
        val incorrectAnswers = generateIncorrect(correctAnswer, quizCount)

        // Shuffle all the answers
        val allAnswers = mutableListOf(correctAnswer)
        allAnswers += incorrectAnswers
        allAnswers.shuffle()

        return allAnswers
    }

     private fun generateIncorrect(correctAnswer: Int, quizCount: Int): MutableList<Int>{
        val incorrectAnswers = mutableListOf<Int>()
        while (incorrectAnswers.size < 3) {
            if(quizCount < 6){
                val incorrectAnswer = Random.nextInt(correctAnswer - 2, correctAnswer + 2) // Random number between 1 and 100
                if (incorrectAnswer != correctAnswer &&
                    incorrectAnswer.toString().length == correctAnswer.toString().length &&
                    !incorrectAnswers.contains(incorrectAnswer)) {
                    incorrectAnswers.add(incorrectAnswer)
                }
            } else if(quizCount < 20){
                val incorrectAnswer = Random.nextInt(correctAnswer - 20, correctAnswer + 20) // Random number between 1 and 100
                if (incorrectAnswer != correctAnswer &&
                    incorrectAnswer.toString().length == correctAnswer.toString().length &&
                    !incorrectAnswers.contains(incorrectAnswer)) {
                    incorrectAnswers.add(incorrectAnswer)
                }
            }else{
                val incorrectAnswer = Random.nextInt(correctAnswer - 100, correctAnswer + 100) // Random number between 1 and 100
                if (incorrectAnswer != correctAnswer &&
                    incorrectAnswer.toString().length == correctAnswer.toString().length &&
                    !incorrectAnswers.contains(incorrectAnswer)) {
                    incorrectAnswers.add(incorrectAnswer)
                }
            }

        }
        return incorrectAnswers
    }



}