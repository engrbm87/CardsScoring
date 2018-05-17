package com.simon_eye.cardsscoring;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class likha extends AppCompatActivity {

    private static final int FINAL_SCORE = 101;
    private int roundIndex = 1;
    private static final int ROUND_TOTAL = 36;
    private int [] playerScores = new int[4];
    protected static ArrayList<String> playerNames = new ArrayList<>();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                // User chose the "Settings" item, show the app settings UI...
                recreate();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likha);

        final LinearLayout currentScoreLayout = findViewById(R.id.currentScore);
        currentScoreLayout.setEnabled(false);

        // Setup Toolbar
        Toolbar myToolbar = findViewById(R.id.likha_toolbar);
        setSupportActionBar(myToolbar);

        // put back player names if they are still cached
        // TODO use shared preferences to save and restore player Names

        final Button submitBtn = findViewById(R.id.submit_button);
        final Button startBtn = findViewById(R.id.start_button);
        final Button resetBtn = findViewById(R.id.reset_button);

        // Save Player Names and disabled Text Fields on Start

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentScoreLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < 4; i++) {
                    String playerName = "player" + (i + 1);
                    int resID = getResources().getIdentifier(playerName, "id", getPackageName());
                    EditText playerNameField = findViewById(resID);
                    playerNames.add(playerNameField.getText().toString());
                    playerNameField.setEnabled(false);
                }
                startBtn.setVisibility(View.INVISIBLE);
                submitBtn.setVisibility(View.VISIBLE);

            }
        });

        // Create an {@link com.simon_eye.cardsscoring.RoundScoreAdapter}, whose data source is a list of
        // {@link RoundScores}. The adapter knows how to create list item views for each item
        // in the list.
        final ArrayList<RoundScores> roundScores = new ArrayList<>();
        final RoundScoreAdapter roundScoreAdapter = new RoundScoreAdapter(this, roundScores);

        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView listView = findViewById(R.id.scoresList);
        listView.setAdapter(roundScoreAdapter);

        // get the values and store them
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    return;
                }
                String[] submittedScore = new String[4];
//                 Add new Items to List
                for (int i = 0; i < 4; i++) {
                    String playerIndex = "player" + (i + 1) + "Score";
                    Log.i("RoundScores", "Player1Index = " + playerIndex);
                    int resID = getResources().getIdentifier(playerIndex, "id", getPackageName());
                    EditText submittedScoreField = findViewById(resID);
                    submittedScore[i] = submittedScoreField.getText().toString();
                    if (submittedScore[i].equals("")) {
                        submittedScore[i] = "0";
                    }
                    Log.i("RoundScores", "Player1Score = " + submittedScore[i]);
                }
                if (!updateScore(submittedScore)) {
                    Toast.makeText(getApplicationContext(), "Sum must be 36",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // create a new @link roundScore object from the currentScore
                roundScores.add(0, new RoundScores(playerScores, roundIndex));
                roundIndex++;
                roundScoreAdapter.notifyDataSetChanged();

                if (gameEndCheck()) {

                    currentScoreLayout.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    resetBtn.setVisibility(View.VISIBLE);

                }
                // clear score input fields for next round
                ViewGroup group = findViewById(R.id.currentScore);
                for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                    View view = group.getChildAt(i);
                    if (view instanceof EditText) {
                        ((EditText) view).setText("");
                    }
                }
            }
        });
        // Start New Game when game ends
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    private boolean gameEndCheck() {
        for (int score : playerScores) {
            if (score >= FINAL_SCORE){
                return true;
            }
        }
        return false;
    }

    private boolean updateScore(String[] submittedScore) {
        // TODO check if the values sum to 36
        int tempTotalScore = 0;
        int[] score = new int[submittedScore.length];
        // create int [] out of the string [] of scores
        for (int i=0; i < score.length; i++) {
            score[i] = Integer.parseInt(submittedScore[i]);
            tempTotalScore += score[i];
        }
        // if the scores sum is not 36 return false
        if (tempTotalScore != ROUND_TOTAL) {
            return false;
        }
        // otherwise add the score for each player and return true
        for (int i=0; i< score.length; i++) {
            playerScores[i] += score[i];
        }
        return true;
        }

}
