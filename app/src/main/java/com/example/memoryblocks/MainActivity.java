package com.example.memoryblocks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToDifficultyScreen(View view) {
        // Go to DifficultyActivity
        Intent intent = new Intent(this, DifficultyActivity.class);
        startActivity(intent);
    }
    public void goToHowToPlayScreen(View view) {
        // Go to HelpActivity (How To Play)
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
