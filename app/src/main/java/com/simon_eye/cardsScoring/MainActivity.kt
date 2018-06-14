package com.simon_eye.cardsScoring

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.simon_eye.cardsScoring.R.id.likha
import com.simon_eye.cardsScoring.R.id.tarneeb
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the View that shows the numbers category
//        val likha = findViewById<TextView>(R.id.likha)
//        val tarneeb = findViewById<TextView>(R.id.tarneeb)

        // Set a click listener on that View
        // The code in this method will be executed when the numbers View is clicked on.
        likha.setOnClickListener {
            val likhaActivity = Intent(this@MainActivity, com.simon_eye.cardsScoring.LikhaGame::class.java)
            startActivity(likhaActivity)
        }
        // The code in this method will be executed when the numbers View is clicked on.
        tarneeb.setOnClickListener {
            val tarneebActivity = Intent(this@MainActivity, com.simon_eye.cardsScoring.TarneebGame::class.java)
            startActivity(tarneebActivity)
        }
    }
}
