package com.simon_eye.cardsScoring

import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.game_layout.*

/**
 * Created by rami_m on 5/26/2018.
 */


class LikhaGame : gameActivity(GAME_TITLE, NUM_OF_PLAYERS, PLAYER_INITIAL) {


    override fun gameEndCheck(): Boolean {
        for (i in 0 until playerScores.size) {
            if (playerScores[i] >= FINAL_SCORE) {
                displayGameResult(String.format("%s Lost", playerNames[i]))
                return true
            }
        }
        return false
    }

    override fun doubleTapAction(autoFillField: Int): Boolean {
        var remainingScore = 0

//        val currentScore = findViewById<ViewGroup>(R.id.currentScore)
        var i = 0
//        val count = currentScore.childCount
        while (i < currentScore.childCount) {
            val view = currentScore.getChildAt(i)
            if (view is EditText) {
                val score = view.text.toString()
                if (score != "" && view.getId() != autoFillField) {
                    remainingScore += Integer.parseInt(score)
                }
            }
            ++i
        }
        if (remainingScore > ROUND_TOTAL) {
            Toast.makeText(applicationContext, "Invalid Input",
                    Toast.LENGTH_SHORT).show()
            return false
        }
        remainingScore = ROUND_TOTAL - remainingScore
        val scoreField = findViewById<EditText>(autoFillField)
        scoreField.setText(remainingScore.toString())
        return true
    }

    override fun scoreCheck(tempScore: IntArray): Boolean {
        // check if the values sum to 36
        var tempTotalScore = 0

        // create int [] out of the string [] of scores
        for (atempScore in tempScore) {
            tempTotalScore += atempScore
        }
        // if the scores sum is not 36 return false
        if (tempTotalScore != ROUND_TOTAL) {
            Toast.makeText(applicationContext, "Sum must be 36",
                    Toast.LENGTH_SHORT).show()
            return false
        }
        submittedScore = tempScore
        return true
    }

    companion object {

        // define variables for LIKHA game
        protected val FINAL_SCORE = 101  // First player to reach 101 or higher loses
        private val GAME_TITLE = R.string.likhaGameTitle
        private val ROUND_TOTAL = 36  // Round score total must be 36
        private val NUM_OF_PLAYERS = 4 // number of players in Likha
        private val PLAYER_INITIAL = "P" // player Initial in Likha game
    }
}
