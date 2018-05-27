package com.simon_eye.cardsscoring;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by rami_m on 5/26/2018.
 */

public abstract class gameActivity extends AppCompatActivity {

    static int gameTitle; // Game Title to be displayed
    static int NUM_OF_PLAYERS; // number of players in Likha
    static String PLAYER_INITIAL; // Player initial is set based on game
    static ArrayList<String> playerNames = new ArrayList<>(); // Store PlayerNames and display them in new games
    int[] playerScores;  // Array to hold players score
    int[] lastScore;     // Last correct submitted score
    int[] submittedScore; // current submitted score that will be checked
    boolean mGameEnd = false;
    private View vTouch;  // Holder for doubleTapped  view
    private int roundIndex = 1;  // Round index holder
    private ArrayList<RoundScores> roundScores = new ArrayList<>(); // Array list of round scores displayed in the listView
    private GestureDetectorCompat mDetector;  // Gesture Detector for doubleTap to complete the round score
    private RoundScoreAdapter roundScoreAdapter;

    public gameActivity(int gameTitle, int NUM_OF_PLAYERS, String PLAYER_INITIAL) {
        gameActivity.gameTitle = gameTitle;
        gameActivity.NUM_OF_PLAYERS = NUM_OF_PLAYERS;
        gameActivity.PLAYER_INITIAL = PLAYER_INITIAL;

        lastScore = new int[NUM_OF_PLAYERS];
        playerScores = new int[NUM_OF_PLAYERS];
        submittedScore = new int[NUM_OF_PLAYERS];

    }

    @Override
    public void onBackPressed() {
        playerNames.clear();
        super.onBackPressed();
    }

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

    public void setLayoutDirection(String locale) {

        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(locale));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actionbar, menu);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        // Setup Toolbar
        Toolbar myToolbar = findViewById(R.id.game_toolbar);
        myToolbar.setTitle(getString(gameTitle));
        setSupportActionBar(myToolbar);


        // Add EditText fields for each player name and score
        final LinearLayout playerNameView = findViewById(R.id.players_initials);
        final LinearLayout currentScoreLayout = findViewById(R.id.currentScore);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        for (int i = 0; i < NUM_OF_PLAYERS; i++) {

            EditText playerInitial = new EditText(this, null, 0, R.style.playerNames);
            playerInitial.setHint(String.format("%s%s", PLAYER_INITIAL, String.valueOf(i + 1)));
            if (!playerNames.isEmpty())
                playerInitial.setText(playerNames.get(i).toString());
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
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
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
                            playerNames.add(String.format("%s%s", PLAYER_INITIAL, i + 1));
                        } else
                            playerNames.add(i, ((EditText) view).getText().toString());
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
                if (submittedScoreCheck())
                    updatePlayerScore();

                // Check if Game ended
                if (gameEndCheck()) {
                    mGameEnd = true;
                    currentScoreLayout.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    resetBtn.setVisibility(View.VISIBLE);

                }
                // clear currentScore fields
                clearCurrentScore();
            }
        });

    }

    public void redisplayLastScore() {

        //display the scores from the submittedScores []
        ViewGroup group = findViewById(R.id.currentScore);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText(String.valueOf(lastScore[i]));
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
                if (submittedScoreCheck()) {
                    for (int i = 0; i < lastScore.length; i++) {
                        playerScores[i] -= lastScore[i];
                    }
                    roundIndex--;
                    roundScores.remove(0);
                    updatePlayerScore();
                    editScoreLayout.setVisibility(View.INVISIBLE);
                    clearCurrentScore();
                    submitBtn.setVisibility(View.VISIBLE);
                }
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

    private void clearCurrentScore() {
        // clear score input fields for next round
        ViewGroup group = findViewById(R.id.currentScore);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }

    void displayGameResult(String gameResult) {
        TextView resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setText(gameResult);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);
        resultTextView.startAnimation(anim);
        // Display game result
        LinearLayout resultView = findViewById(R.id.resultView);
        resultView.setVisibility(View.VISIBLE);
    }

    public abstract boolean gameEndCheck();

    /**
     * @param autoFillField is used to identify the field that needs to be auto completed when double tapped
     * @return false if the total input score is more 36 otherwise true
     */

    public abstract boolean doubleTapAction(int autoFillField);

    /**
     * Store submitted score in @link lastScore array
     */

    public abstract boolean scoreCheck(int[] tempScore);

    public boolean submittedScoreCheck() {
        // get value from editText fields

        int[] tempScore = new int[NUM_OF_PLAYERS];
        ViewGroup group = findViewById(R.id.currentScore);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                String score = ((EditText) view).getText().toString();
                if (score.equals(""))
                    tempScore[i] = 0;
                else
                    tempScore[i] = Integer.parseInt(score);
                Log.i("RoundScores", "Player1Score = " + tempScore[i]);

            }
        }
        return scoreCheck(tempScore);
    }

    private void updatePlayerScore() {

        lastScore = submittedScore;
        // add the score for each player and return true
        for (int i = 0; i < submittedScore.length; i++) {
            playerScores[i] += submittedScore[i];
        }
        // create a new @link roundScore object from the currentScore
        roundScores.add(0, new RoundScores(playerScores, roundIndex));
        roundScoreAdapter.notifyDataSetChanged();
        // TODO animate adding new item to listView
        roundIndex++;
    }

    // custom class to handle doubleTap gestures on currentScore fields

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            return doubleTapAction(vTouch.getId());
        }
    }
}
