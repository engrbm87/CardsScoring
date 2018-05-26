package com.simon_eye.cardsscoring;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by rami_m on 5/26/2018.
 */


public class LikhaGame extends gameActivity {

    public static final int ROUND_TOTAL = 36;  // Round score total must be 36
    public static final int NUM_OF_PLAYERS = 4; // number of players in Likha
    public static final int gameType = RoundScores.LIKHA; // specify game type for scoreAdapter
    // define variables for LIKHA game
    protected static final int FINAL_SCORE = 101;  // First player to reach 101 or higher loses

    public LikhaGame() {
        super(FINAL_SCORE, ROUND_TOTAL, NUM_OF_PLAYERS, gameType);

    }


    @Override
    public boolean gameEndCheck() {
        for (int i = 0; i < playerScores.length; i++) {
            if (playerScores[i] >= FINAL_SCORE) {
                displayGameResult(String.format("%s Lost", playerNames.get(i)));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean autoCompleteScore(int autoFillField) {
        int remainingScore = 0;

        ViewGroup group = findViewById(R.id.currentScore);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                String score = ((EditText) view).getText().toString();
                if (!score.equals("") && view.getId() != autoFillField) {
                    remainingScore += Integer.parseInt(score);
                }
            }
        }
        if (remainingScore > ROUND_TOTAL)
            return false;
        remainingScore = ROUND_TOTAL - remainingScore;
        EditText scoreField = findViewById(autoFillField);
        scoreField.setText(String.valueOf(remainingScore));
        return true;
    }

    @Override
    public boolean scoreCheck(int[] tempScore) {
        // check if the values sum to 36
        int tempTotalScore = 0;

        // create int [] out of the string [] of scores
        for (int atempScore : tempScore) {
            tempTotalScore += atempScore;
        }
        // if the scores sum is not 36 return false
        if (tempTotalScore != ROUND_TOTAL) {
            Toast.makeText(getApplicationContext(), "Sum must be 36",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        submittedScore = tempScore;
        return true;
    }
}
