package com.example.memoryblocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
    }

    public void easyButtonClicked(View view) {
        goToGamePlayScreen(Difficulty.EASY);
    }

    public void mediumButtonClicked(View view) {
        goToGamePlayScreen(Difficulty.MEDIUM);
    }

    public void hardButtonClicked(View view) {
        goToGamePlayScreen(Difficulty.HARD);
    }

    public void goToGamePlayScreen(Difficulty difficulty) {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }


}
