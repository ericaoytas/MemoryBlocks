package com.example.memoryblocks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mPlayButton;
    Button mHelpButton;
    Button mScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayButton = (Button) findViewById(R.id.play_button);
        mHelpButton = (Button) findViewById(R.id.help_button);
        mScoresButton = (Button) findViewById(R.id.scores_button);
    }

    public void goToDifficultyScreen(View view) {
        Intent intent = new Intent(this, DifficultyActivity.class);
        startActivity(intent);
    }
    public void goToScoresScreen(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }
    public void goToHowToPlayScreen(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
