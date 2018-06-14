package com.simon_eye.cardsScoring

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.scores_list.view.*

class ScoreAdapter(val items: ArrayList<RoundScores>, val context: Context) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val roundNumber = view.round_number
        val roundScoreItem = view.roundScoreItem
    }

    init {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }


    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = items.get(position)
        holder.roundNumber.text = currentItem.roundNumber
        for (i in 0 until holder.roundScoreItem.childCount) {
            val view = holder.roundScoreItem.getChildAt(i) as TextView
            view.text = currentItem.getPlayerScore(i)

        }
        holder.itemView.setOnLongClickListener { view ->
            if (context is gameActivity) {
                if (position == 0 && !context.mGameEnd) {
                    context.redisplayLastScore()
                    return@setOnLongClickListener true
                }
            }
            false
        }


    }

    override fun getItemViewType(position: Int): Int {
        val item = items.get(position)
        return item.numOfPlayers
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, numOfPlayers: Int): ScoreAdapter.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.scores_list, parent, false) as CardView
        val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        val roundScoreView = view.findViewById<LinearLayout>(R.id.roundScoreItem)
        for (i in 0 until numOfPlayers) {
            val roundScore = TextView(context, null, 0, R.style.roundScoresTextFields)
            roundScore.layoutParams = params
            roundScoreView.addView(roundScore)
        }
        return ViewHolder(view)
    }
}