package com.example.seamon.sikkacodingchallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView youtubeButton, igButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //youtube button
        youtubeButton = findViewById(R.id.main_youtube_button);
        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent youtubeIntent = new Intent(getApplicationContext(), YoutubeActivity.class);
                MainActivity.this.startActivity(youtubeIntent);
            }
        });

        //instagram button
        igButton = findViewById(R.id.main_ig_button);
        igButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent igIntent = new Intent(getApplicationContext(), InstagramActivity.class);
                MainActivity.this.startActivity(igIntent);
            }
        });


    }
}
