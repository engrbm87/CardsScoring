package com.simon_eye.cardsscoring;


/**
 * {@link RoundScores} represents a single Android platform release.
 * Each object has 3 properties: name, version number, and image resource ID.
 */

public class RoundScores {

    private int[] mPlayerScores;
    private String mRoundNumber;


    public RoundScores(int[] PlayerScores, int RoundNumber)
    {

        mPlayerScores = new int[PlayerScores.length];
        for (int i = 0; i < mPlayerScores.length; i++) {
            mPlayerScores[i] = PlayerScores[i];
        }
        mRoundNumber = String.valueOf(RoundNumber);
    }
    /**
     * Get the Round Number
     */
    public String getRoundNumber() {
        return mRoundNumber;
    }

    public String getPlayerScore(int i) {
        return String.valueOf(mPlayerScores[i]);
    }

    public int getNumOfPlayers() {
        return mPlayerScores.length;
    }
}
