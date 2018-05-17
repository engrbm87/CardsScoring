package com.simon_eye.cardsscoring;


/**
 * {@link RoundScores} represents a single Android platform release.
 * Each object has 3 properties: name, version number, and image resource ID.
 */

public class RoundScores {

    private String mPlayerOneScore;
    private String mPlayerTwoScore;
    private String mPlayerThreeScore;
    private String mPlayerFourScore;
    private int mRoundNumber = 0;

    public RoundScores(String PlayerOneScore, String PlayerTwoScore, String PlayerThreeScore, String PlayerFourScore, int RoundNumber)
    {
        mPlayerOneScore = PlayerOneScore;
        mPlayerTwoScore = PlayerTwoScore;
        mPlayerThreeScore = PlayerThreeScore;
        mPlayerFourScore = PlayerFourScore;
        mRoundNumber = RoundNumber;
    }

    public RoundScores(int [] PlayerScores, int RoundNumber)
    {
        mPlayerOneScore = String.valueOf(PlayerScores[0]);
        mPlayerTwoScore = String.valueOf(PlayerScores[1]);
        mPlayerThreeScore = String.valueOf(PlayerScores[2]);
        mPlayerFourScore = String.valueOf(PlayerScores[3]);
        mRoundNumber = RoundNumber;
    }
    /**
     * Get the score of Player One
     */
    public String getPlayerOneScore() {
        return mPlayerOneScore;
    }

    /**
     * Get the score of Player Two
     */
    public String getPlayerTwoScore() {
        return mPlayerTwoScore;
    }

    /**
     * Get the score of Player Three
     */
    public String getPlayerThreeScore() {
        return mPlayerThreeScore;
    }

    /**
     * Get the score of Player Four
     */
    public String getPlayerFourScore() {
        return mPlayerFourScore;
    }/**
     * Get the Round Number
     */
    public String getRoundNumber() {
        return String.valueOf(mRoundNumber);
    }
}
