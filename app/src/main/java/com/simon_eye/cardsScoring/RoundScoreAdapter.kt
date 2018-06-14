package com.simon_eye.cardsScoring

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView

import java.util.ArrayList

/**
 * [RoundScoreAdapter] is an [ArrayAdapter] that can provide the layout for each list
 * based on a data source, which is a list of [RoundScores] objects.
 */

class RoundScoreAdapter
/**
 * Create a new [RoundScoreAdapter] object.
 *
 * @param context is the current context (i.e. Activity) that the adapter is being created in.
 * @param roundScores is the list of [RoundScores]s to be displayed.
 */
(context: Context, roundScores: ArrayList<RoundScores>) : ArrayAdapter<RoundScores>(context, 0, roundScores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var roundScoreLayout: LinearLayout
        var roundScore: TextView

        // Get the {@link RoundScores} object located at this position in the list
        val currentRoundScore = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.scores_list, parent, false)
            roundScoreLayout = listItemView!!.findViewById(R.id.roundScoreItem)
            val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            for (i in 0 until currentRoundScore!!.numOfPlayers) {
                roundScore = TextView(context, null, 0, R.style.roundScoresTextFields)
                roundScore.layoutParams = params
                roundScoreLayout.addView(roundScore)
            }
        }

        roundScoreLayout = listItemView.findViewById(R.id.roundScoreItem)
        for (i in 0 until roundScoreLayout.childCount) {
            val view = roundScoreLayout.getChildAt(i)
            if (view is TextView) {
                view.text = currentRoundScore!!.getPlayerScore(i)
            }
        }


        // same for round score items


        // Assign player scores to layout childs


        //        // Find the TextView in the list_item.xml layout with the ID version_name
        //        TextView playerOneScore = listItemView.findViewById(R.id.scoreP1);
        //        // Get the version name from the current AndroidFlavor object and
        //        // set this text on the name TextView
        //        playerOneScore.setText(currentRoundScore.getPlayerOneScore());
        //
        //        // Find the TextView in the list_item.xml layout with the ID version_number
        //        TextView playerTwoScore = listItemView.findViewById(R.id.scoreP2);
        //        // Get the version number from the current AndroidFlavor object and
        //        // set this text on the number TextView
        //        playerTwoScore.setText(currentRoundScore.getPlayerTwoScore());
        //
        //        // Find the TextView in the list_item.xml layout with the ID version_name
        //        TextView playerThreeScore = listItemView.findViewById(R.id.scoreP3);
        //        // Get the version name from the current AndroidFlavor object and
        //        // set this text on the name TextView
        //        playerThreeScore.setText(currentRoundScore.getPlayerThreeScore());
        //
        //        // Find the TextView in the list_item.xml layout with the ID version_number
        //        TextView playerFourScore = listItemView.findViewById(R.id.scoreP4);
        //        // Get the version number from the current AndroidFlavor object and
        //        // set this text on the number TextView
        //        playerFourScore.setText(currentRoundScore.getPlayerFourScore());

        // Find the TextView in the list_item.xml layout with the ID round_number
        val roundNumber = listItemView.findViewById<TextView>(R.id.round_number)
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        roundNumber.text = currentRoundScore!!.roundNumber


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView
    }


}
