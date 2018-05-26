package com.simon_eye.cardsscoring;

import android.content.res.Configuration;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

public class likhaActivity extends AppCompatActivity {


    private static final int FINAL_SCORE = 101;  // First player to reach 101 or higher loses
    private static final int ROUND_TOTAL = 36;  // Round score total must be 36
    private static final int NUM_OF_PLAYERS = 4; // number of players in Likha
    public View vTouch;  // Holder for doubleTapped  view
    protected ArrayList<String> playerNames = new ArrayList<>(); // Store PlayerNames and display them in new games
    private int roundIndex = 1;  // Round index holder
    private int[] playerScores = new int[NUM_OF_PLAYERS];  // Array to hold players score
    private ArrayList<RoundScores> roundScores = new ArrayList<>(); // Array list of round scores displayed in the listView
    private GestureDetectorCompat mDetector;  // Gesture Detector for doubleTap to complete the round score
    private int[] submittedScore = new int[NUM_OF_PLAYERS];
    private RoundScoreAdapter roundScoreAdapter;
    private boolean mGameEnd = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                recreate();
                return true;
            case R.id.right_to_left:
                setLayoutDirection("ar");
                recreate();
                return true;
            case R.id.left_to_right:
                setLayoutDirection("en");
                recreate();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    /**
     *
     * @param locale is used to change the layout direction for Dealing right or left
     */

    public void setLayoutDirection (String locale) {

        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(locale));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        // Setup Toolbar
        Toolbar myToolbar = findViewById(R.id.game_toolbar);
        setSupportActionBar(myToolbar);

        // Add EditText fields for each player name and score
        final LinearLayout playerNameView = findViewById(R.id.players_initials);
        final LinearLayout currentScoreLayout  = findViewById(R.id.currentScore);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        for (int i = 0; i < NUM_OF_PLAYERS; i++) {

            EditText playerInitial = new EditText(this, null, 0, R.style.playerNames);
            playerInitial.setHint(String.format("P%s", String.valueOf(i + 1)));
            playerInitial.setLayoutParams(params);
            if (i == 0) playerInitial.requestFocus();
            playerNameView.addView(playerInitial);

            // same for the currentScore Layout
            EditText playerScore = new EditText(this, null, 0, R.style.currentScore);
            playerScore.setHint("0");
            playerScore.setLayoutParams(params);
            playerScore.setId(i);
            currentScoreLayout.addView(playerScore);
        }
        // TODO put back player names if they are still cached

        // Create an {@link com.simon_eye.cardsscoring.RoundScoreAdapter}, whose data source is a list of
        // {@link RoundScores}. The adapter knows how to create list item views for each item
        // in the list.
        roundScoreAdapter = new RoundScoreAdapter(this, roundScores);
        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView roundScoreList = findViewById(R.id.scoresList);
        roundScoreList.setAdapter(roundScoreAdapter);

        // set the mDetector to attach it to the onTouchListener
        mDetector = new GestureDetectorCompat(likhaActivity.this, new MyGestureListener());
        for (int i = 0; i < currentScoreLayout.getChildCount(); i++) {
            EditText tempScore = (EditText) currentScoreLayout.getChildAt(i);

            tempScore.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                        vTouch = v;
                        return mDetector.onTouchEvent(event);
                }
            });
        }

        // assign the different Buttons and attach onClickListeners to them

        final Button submitBtn = findViewById(R.id.submit_button);
        final Button startBtn = findViewById(R.id.start_button);
        final Button resetBtn = findViewById(R.id.reset_button);


        // When pressing Start: Save Player Names and disable Text Fields then show the currentScore Layout and Submit button
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0, count = playerNameView.getChildCount(); i < count; ++i) {
                    View view = playerNameView.getChildAt(i);
                    if (view instanceof EditText) {
                        String name = ((EditText) view).getText().toString();
                        if (name.equals("")) {
                            playerNames.add(((EditText) view).getHint().toString());
                        } else
                            playerNames.add(((EditText) view).getText().toString());
                    }
                    view.setEnabled(false);
                }
                // TODO use shared preferences to save player Names


                startBtn.setVisibility(View.INVISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                currentScoreLayout.setVisibility(View.VISIBLE);

            }
        });

        // Set onitemclick listener to edit the last round score submitted

        roundScoreList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0 && !mGameEnd) {
                    redisplayLastScore();
                    return true;
                }
                return false;
            }
        });

        // When pressing Submit get the scores and store them after validating the round score total
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {
                    return;
                }
                // check submitted score and update it
                if (!updateScore()) {
                    Toast.makeText(getApplicationContext(), "Sum must be 36",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if Game ended
                if (gameEndCheck()) {
                    currentScoreLayout.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    resetBtn.setVisibility(View.VISIBLE);

                }
                // clear currentScore fields
                clearCurrentScore();
            }
        });

    }

    private void redisplayLastScore() {

        //display the scores from the submittedScores []
        ViewGroup group = findViewById(R.id.currentScore);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText(String.valueOf(submittedScore[i]));
            }
        }
        // hide the submit button
        final Button submitBtn = findViewById(R.id.submit_button);
        submitBtn.setVisibility(View.INVISIBLE);

        final LinearLayout editScoreLayout = findViewById(R.id.editScoreButtons);
        editScoreLayout.setVisibility(View.VISIBLE);
        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editScoreLayout.setVisibility(View.INVISIBLE);
                clearCurrentScore();
                submitBtn.setVisibility(View.VISIBLE);
            }
        });

        Button editBtn = findViewById(R.id.edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < submittedScore.length; i++) {
                    playerScores[i] -= submittedScore[i];
                }
                roundIndex--;
                roundScores.remove(0);
                updateScore();
                editScoreLayout.setVisibility(View.INVISIBLE);
                clearCurrentScore();
                submitBtn.setVisibility(View.VISIBLE);
            }
        });
        MarginTransition cancelBtnAnim = new MarginTransition(cancelBtn, 150, 0);
        // Animation's duration
        cancelBtnAnim.setDuration(800);
        // Start my animation
        editBtn.startAnimation(cancelBtnAnim);
        MarginTransition editBtnAnim = new MarginTransition(editBtn, 0, 150);
        // Animation's duration
        editBtnAnim.setDuration(800);
        // Start my animation
        editBtn.startAnimation(editBtnAnim);
    }

    public void resetGame(View view) {
        recreate();
    }

    private void clearCurrentScore()
    {
        // clear score input fields for next round
        ViewGroup group = findViewById(R.id.currentScore);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }

    private boolean gameEndCheck() {
        for (int i = 0; i < playerScores.length; i++) {
            if (playerScores[i] >= FINAL_SCORE) {
                TextView resultTextView = findViewById(R.id.resultTextView);
                resultTextView.setText(String.format("%s Lost", playerNames.get(i)));
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500); //You can manage the time of the blink with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.RESTART);
                anim.setRepeatCount(Animation.INFINITE);
                resultTextView.startAnimation(anim);
                // Display game result
                LinearLayout resultView = findViewById(R.id.resultView);
                resultView.setVisibility(View.VISIBLE);
                mGameEnd = true;
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param autoFillField is used to identify the field that needs to be auto completed when double tapped
     * @return false if the total input score is more 36 otherwise true
     */

    private boolean autoCompleteScore(int autoFillField) {

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

    /**
     *
     * @return false if the round total != 36 otherwise, the players scores are updated
     */

    private boolean updateScore() {

        // get value from editText fields

        ViewGroup group = findViewById(R.id.currentScore);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                String score = ((EditText) view).getText().toString();
                if (score.equals(""))
                    submittedScore[i] = 0;
                else
                    submittedScore[i] = Integer.parseInt(score);
                Log.i("RoundScores", "Player1Score = " + submittedScore[i]);

            }
        }

        // check if the values sum to 36
        int tempTotalScore = 0;

        // create int [] out of the string [] of scores
        for (int aSubmittedScore : submittedScore) {
            tempTotalScore += aSubmittedScore;
        }
        // if the scores sum is not 36 return false
        if (tempTotalScore != ROUND_TOTAL) {
            return false;
        }
        // otherwise add the score for each player and return true
        for (int i = 0; i < submittedScore.length; i++) {
            playerScores[i] += submittedScore[i];
        }
        // create a new @link roundScore object from the currentScore
        roundScores.add(0, new RoundScores(playerScores, roundIndex, RoundScores.LIKHA));
        roundScoreAdapter.notifyDataSetChanged();
        // TODO animate adding new item to listView
        roundIndex++;
        return true;
    }

    // custom class to handle doubleTap gestures on currentScore fields

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            if(!autoCompleteScore(vTouch.getId()))
            {
                Toast.makeText(getApplicationContext(), String.valueOf(vTouch.getId()),
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

}
