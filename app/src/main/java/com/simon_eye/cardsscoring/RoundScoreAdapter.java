package com.simon_eye.cardsscoring;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
* {@link RoundScoreAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link RoundScores} objects.
*/

public class RoundScoreAdapter extends ArrayAdapter<RoundScores> {

    /**
     * Create a new {@link RoundScoreAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param roundScores is the list of {@link RoundScores}s to be displayed.
     */
    public RoundScoreAdapter(Context context, ArrayList<RoundScores> roundScores) {
        super(context, 0, roundScores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout roundScoreLayout;
        TextView roundScore;

        // Get the {@link RoundScores} object located at this position in the list
        RoundScores currentRoundScore = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.scores_list, parent, false);
            roundScoreLayout = listItemView.findViewById(R.id.roundScoreItem);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            for (int i = 0; i < currentRoundScore.getNumOfPlayers(); i++) {
                roundScore = new TextView(getContext(), null, 0, R.style.roundScoresTextFields);
                roundScore.setLayoutParams(params);
                roundScoreLayout.addView(roundScore);
            }
        }

        roundScoreLayout = listItemView.findViewById(R.id.roundScoreItem);
        for (int i = 0; i < roundScoreLayout.getChildCount(); i++) {
            View view = roundScoreLayout.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setText(currentRoundScore.getPlayerScore(i));
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
        TextView roundNumber = listItemView.findViewById(R.id.round_number);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        roundNumber.setText(currentRoundScore.getRoundNumber());


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }


}
