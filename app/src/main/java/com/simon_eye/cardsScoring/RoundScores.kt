package com.simon_eye.cardsScoring


/**
 * [RoundScores] represents a single Android platform release.
 * Each object has 3 properties: name, version number, and image resource ID.
 */

class RoundScores(PlayerScores: IntArray, RoundNumber: Int) {

    private val mPlayerScores: IntArray
    /**
     * Get the Round Number
     */
    val roundNumber: String

    val numOfPlayers: Int
        get() = mPlayerScores.size


    init {

        mPlayerScores = IntArray(PlayerScores.size)
        for (i in mPlayerScores.indices) {
            mPlayerScores[i] = PlayerScores[i]
        }
        roundNumber = RoundNumber.toString()
    }

    fun getPlayerScore(i: Int): String {
        return mPlayerScores[i].toString()
    }
}
