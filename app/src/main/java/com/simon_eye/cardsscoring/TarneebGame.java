package com.simon_eye.cardsscoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by rami_m on 5/26/2018.
 */

public class TarneebGame extends gameActivity {

    // define variables for LIKHA game
    protected static final int FINAL_SCORE = 51;  // First player to reach 101 or higher loses
    private static final int GAME_TITLE = R.string.tarneebGameTitle;
    private static final int NUM_OF_PLAYERS = 2; // number of players in Likha
    private static final String PLAYER_INITIAL = "T"; // player Initial in Likha game

    public TarneebGame() {
        super(GAME_TITLE, NUM_OF_PLAYERS, PLAYER_INITIAL);
    }

    @Override
    public boolean gameEndCheck() {
        for (int i = 0; i < playerScores.length; i++) {
            if (playerScores[i] >= FINAL_SCORE) {
                displayGameResult(String.format("%s Won", playerNames.get(i)));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doubleTapAction(int autoFillField) {
        return false;
    }

    @Override
    public boolean scoreCheck(int[] tempScore) {
        submittedScore = tempScore;
        return true;
    }
}
