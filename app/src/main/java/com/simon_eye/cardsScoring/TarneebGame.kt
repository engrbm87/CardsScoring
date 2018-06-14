package com.simon_eye.cardsScoring

/**
 * Created by rami_m on 5/26/2018.
 */

class TarneebGame : gameActivity(GAME_TITLE, NUM_OF_PLAYERS, PLAYER_INITIAL) {

    override fun gameEndCheck(): Boolean {
        for (i in 0 until playerScores.size) {
            if (playerScores[i] >= FINAL_SCORE) {
                displayGameResult("${playerNames[i]} Won")
                return true
            }
        }
        return false
    }

    override fun doubleTapAction(autoFillField: Int): Boolean {
        return false
    }

    override fun scoreCheck(tempScore: IntArray): Boolean {
        submittedScore = tempScore
        return true
    }

    companion object {

        // define variables for LIKHA game
        protected val FINAL_SCORE = 51  // First player to reach 101 or higher loses
        private val GAME_TITLE = R.string.tarneebGameTitle
        private val NUM_OF_PLAYERS = 2 // number of players in Likha
        private val PLAYER_INITIAL = "T" // player Initial in Likha game
    }
}
