package com.simon_eye.cardsscoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the View that shows the numbers category
        TextView likha = findViewById(R.id.likha);
        TextView tarneeb = findViewById(R.id.tarneeb);

        // Set a click listener on that View
        likha.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent likhaActivity = new Intent(MainActivity.this, com.simon_eye.cardsscoring.LikhaGame.class);
                startActivity(likhaActivity);
            }
        });
        tarneeb.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tarneebActivity = new Intent(MainActivity.this, com.simon_eye.cardsscoring.tarneebActivity.class);
                startActivity(tarneebActivity);
            }
        });


    }
}
