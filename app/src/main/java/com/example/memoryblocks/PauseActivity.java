package com.example.memoryblocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PauseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_game);
    }

    public void goToDifficultyScreen(View view) {
        Intent intent = new Intent(this, DifficultyActivity.class);
        startActivity(intent);
    }


    public void goToHomeScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void resumeButtonClicked(View view) {
        finish();
    }
}
