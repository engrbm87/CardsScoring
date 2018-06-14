package com.simon_eye.cardsScoring

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import com.simon_eye.cardsScoring.R.id.*
import kotlinx.android.synthetic.main.game_layout.*
import java.util.*


/**
 * Created by rami_m on 5/26/2018.
 */

abstract class gameActivity(val gameTitle: Int, val NUM_OF_PLAYERS: Int, val PLAYER_INITIAL: String) : AppCompatActivity() {
    internal var playerScores = IntArray((NUM_OF_PLAYERS))  // Array to hold players score
    internal var lastScore = IntArray((NUM_OF_PLAYERS))     // Last correct submitted score
    internal var submittedScore = IntArray((NUM_OF_PLAYERS)) // current submitted score that will be checked
    internal var mGameEnd = false
    private var vTouch: View? = null  // Holder for doubleTapped  view
    private var roundIndex = 1  // Round index holder
    private val roundScores = ArrayList<RoundScores>() // Array list of round scores displayed in the listView
    private var mDetector: GestureDetectorCompat? = null  // Gesture Detector for doubleTap to complete the round score
    private var roundScoreAdapter: ScoreAdapter? = null
    internal var playerNames = mutableListOf<String>() // Store PlayerNames and display them in new games


    override fun onBackPressed() {
        playerNames.clear()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reset -> {
                recreate()
                return true
            }
            R.id.right_to_left -> {
                setLayoutDirection("ar")
                recreate()
                return true
            }
            R.id.left_to_right -> {
                setLayoutDirection("en")
                recreate()
                return true
            }
            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }

    private fun setLayoutDirection(locale: String) {

        val configuration = resources.configuration
        configuration.setLayoutDirection(Locale(locale))
        applyOverrideConfiguration(configuration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_layout)
        // Setup Toolbar
        game_toolbar.title = getString(gameTitle)
        setSupportActionBar(game_toolbar)
        //        getActionBar().setDisplayHomeAsUpEnabled(true);


        // Add EditText fields for each player name and score
//        val players_initials = findViewById<LinearLayout>(R.id.players_initials)
//        val currentScore = findViewById<LinearLayout>(R.id.currentScore)
        val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        for (i in 0 until NUM_OF_PLAYERS) {

            val playerInitial = EditText(this, null, 0, R.style.playerNames)
            playerInitial.hint = String.format("%s%s", PLAYER_INITIAL, (i + 1).toString())
            if (playerNames.isNotEmpty())
                playerInitial.setText(playerNames[i])
            playerInitial.layoutParams = params
            if (i == 0) playerInitial.requestFocus()
            players_initials.addView(playerInitial)

            // same for the currentScore Layout
            val playerScore = EditText(this, null, 0, R.style.currentScore)
            playerScore.hint = "0"
            playerScore.layoutParams = params
            playerScore.id = i
            currentScore.addView(playerScore)
        }

        // Create an {@link com.simon_eye.cardsScoring.RoundScoreAdapter}, whose data source is a list of
        // {@link RoundScores}. The adapter knows how to create list item views for each item
        // in the list.
//        roundScoreAdapter = RoundScoreAdapter(this, roundScores)
        // Get a reference to the ListView, and attach the adapter to the listView.
//        val scoresList = findViewById<ListView>(R.id.scoresList)
//        scoresList.adapter = roundScoreAdapter

        // define recycler adapter
        scoresList.layoutManager = LinearLayoutManager(this)
        roundScoreAdapter = ScoreAdapter(roundScores, this)
        scoresList.adapter = roundScoreAdapter
        scoresList.setHasFixedSize(true)

        // set the mDetector to attach it to the onTouchListener
        mDetector = GestureDetectorCompat(this, MyGestureListener())
        for (i in 0 until currentScore.childCount) {
            val tempScore = currentScore.getChildAt(i) as EditText

            tempScore.setOnTouchListener { v, event ->
                vTouch = v
                mDetector!!.onTouchEvent(event)
            }
        }
        // When pressing Start: Save Player Names and disable Text Fields then show the currentScore Layout and Submit button
        start_button.setOnClickListener {
            var i = 0
//            val count =
            while (i < players_initials.childCount) {
                val view = players_initials.getChildAt(i)
                if (view is EditText) {
//                    val name = view.text.toString()
//                    playerNames.add( if (view.text.toString().isNotEmpty()) {
//                        view.text.toString()
//                    } else "$PLAYER_INITIAL $i" )
                    if (view.text.toString().isEmpty()) {
                        playerNames.add(i, String.format("%s%s", PLAYER_INITIAL, i + 1))
                    } else
                        playerNames.add(i, view.text.toString())
                }
                view.isEnabled = false
                ++i
            }
            // TODO use shared preferences to save player Names


            start_button.visibility = View.INVISIBLE
            submit_button.visibility = View.VISIBLE
            currentScore.visibility = View.VISIBLE
        }

        // Set onitemclick listener to edit the last round score submitted

//        scoresList.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
//            if (position == 0 && !mGameEnd) {
//                redisplayLastScore()
//                return@OnItemLongClickListener true
//            }
//            false
//        }

        // When pressing Submit get the scores and store them after validating the round score total
        submit_button.setOnClickListener(View.OnClickListener {
            try {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

            } catch (e: Exception) {
                return@OnClickListener
            }

            // check submitted score and update it
            if (submittedScoreCheck())
                updatePlayerScore()

            // Check if Game ended
            if (gameEndCheck()) {
                mGameEnd = true
                currentScore.visibility = View.INVISIBLE
                submit_button.visibility = View.INVISIBLE
                reset_button.visibility = View.VISIBLE

            }
            // clear currentScore fields
            clearCurrentScore()
        })

    }

    fun redisplayLastScore() {

        //display the scores from the submittedScores []
//        val currentScore = findViewById<ViewGroup>(R.id.currentScore)
//        run {
        var i = 0
//            val count = currentScore.childCount
        while (i < currentScore.childCount) {
            val view = currentScore.getChildAt(i)
            if (view is EditText) {
                view.setText(lastScore[i].toString())
            }
            ++i
        }
//        }
        // hide the submit button
//        val submit_button = findViewById<Button>(R.id.submit_button)
        submit_button.visibility = View.INVISIBLE

//        val editScoreButtons = findViewById<LinearLayout>(R.id.editScoreButtons)
        editScoreButtons.visibility = View.VISIBLE
//        val cancel_button = findViewById<Button>(R.id.cancel_button)
        cancel_button.setOnClickListener {
            editScoreButtons.visibility = View.INVISIBLE
            clearCurrentScore()
            submit_button.visibility = View.VISIBLE
        }

//        val edit_button = findViewById<Button>(R.id.edit_button)
        edit_button.setOnClickListener {
            if (submittedScoreCheck()) {
                for (i in lastScore.indices) {
                    playerScores[i] -= lastScore[i]
                }
                roundIndex--
                roundScores.removeAt(0)
                roundScoreAdapter?.notifyItemRemoved(0)
                updatePlayerScore()
                editScoreButtons.visibility = View.INVISIBLE
                clearCurrentScore()
                submit_button.visibility = View.VISIBLE
            }
        }
        val cancelBtnAnim = MarginTransition(cancel_button, 150, 0)
        // Animation's duration
        cancelBtnAnim.duration = 800
        // Start my animation
        edit_button.startAnimation(cancelBtnAnim)
        val editBtnAnim = MarginTransition(edit_button, 0, 150)
        // Animation's duration
        editBtnAnim.duration = 800
        // Start my animation
        edit_button.startAnimation(editBtnAnim)
    }

    fun resetGame(view: View) {
        recreate()
    }

    private fun clearCurrentScore() {
        // clear score input fields for next round
//        val currentScore = findViewById<ViewGroup>(R.id.currentScore)
        var i = 0
//        val count = currentScore.childCount
        while (i < currentScore.childCount) {
            val view = currentScore.getChildAt(i)
            if (view is EditText) {
                view.setText("")
            }
            ++i
        }
    }

    internal fun displayGameResult(gameResult: String) {
//        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        resultTextView.text = gameResult
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500 //You can manage the time of the blink with this parameter
        anim.startOffset = 20
        anim.repeatMode = Animation.RESTART
        anim.repeatCount = Animation.INFINITE
        resultTextView.startAnimation(anim)
        // Display game result
//        val resultView = findViewById<LinearLayout>(R.id.resultView)
        resultView.visibility = View.VISIBLE
    }

    abstract fun gameEndCheck(): Boolean

    /**
     * @param autoFillField is used to identify the field that needs to be auto completed when double tapped
     * @return false if the total input score is more 36 otherwise true
     */

    abstract fun doubleTapAction(autoFillField: Int): Boolean

    /**
     * Store submitted score in @link lastScore array
     */

    abstract fun scoreCheck(tempScore: IntArray): Boolean

    private fun submittedScoreCheck(): Boolean {
        // get value from editText fields

        val tempScore = IntArray(NUM_OF_PLAYERS)
//        val currentScore = findViewById<ViewGroup>(R.id.currentScore)
        var i = 0
//        val count = currentScore.childCount
        while (i < currentScore.childCount) {
            val view = currentScore.getChildAt(i)
            if (view is EditText) {
                val score = view.text.toString()
                if (score == "")
                    tempScore[i] = 0
                else
                    tempScore[i] = score.toInt()
                Log.i("RoundScores", "Player1Score = " + tempScore[i])

            }
            ++i
        }
        return scoreCheck(tempScore)
    }

    private fun updatePlayerScore() {

        lastScore = submittedScore
        // add the score for each player and return true
        for (i in submittedScore.indices) {
            playerScores[i] += submittedScore[i]
        }
        // create a new @link roundScore object from the currentScore
        roundScores.add(0, RoundScores(playerScores, roundIndex))
//        roundScoreAdapter!!.notifyDataSetChanged()
        roundScoreAdapter?.notifyItemInserted(0)
        // TODO animate adding new item to listView
        roundIndex++
    }

    // custom class to handle doubleTap gestures on currentScore fields

    internal inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent): Boolean {

            return doubleTapAction(vTouch!!.id)
        }
    }

}
