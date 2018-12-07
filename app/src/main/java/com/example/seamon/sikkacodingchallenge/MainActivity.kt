package com.example.seamon.sikkacodingchallenge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private lateinit var youtubeButton: ImageView
    private lateinit var igButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //youtube button
        youtubeButton = findViewById(R.id.main_youtube_button)
        youtubeButton.setOnClickListener {
            val youtubeIntent = Intent(applicationContext, YoutubeActivity::class.java)
            this@MainActivity.startActivity(youtubeIntent)
        }

        //instagram button
        igButton = findViewById(R.id.main_ig_button)
        igButton.setOnClickListener {
            val igIntent = Intent(applicationContext, InstagramActivity::class.java)
            this@MainActivity.startActivity(igIntent)
        }


    }
}
