package com.example.memoryblocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends Activity {

    NumberPicker mDifficultyPicker;
    String [] difficultyLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        mDifficultyPicker = (NumberPicker) findViewById(R.id.difficulty_picker);
        difficultyLevels = new String[]{"Beginner", "Easy", "Normal", "Intermediate", "Hard", "Expert"};
        mDifficultyPicker.setMinValue(0);
        mDifficultyPicker.setMaxValue(difficultyLevels.length-1);
        mDifficultyPicker.setWrapSelectorWheel(false);
        mDifficultyPicker.setDisplayedValues(difficultyLevels);
    }

    public void goToGamePlayScreen(int dims) {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("difficulty", dims);
        startActivity(intent);
    }


    public void difficultyConfirmButtonClicked(View view) {
        String difficulty = difficultyLevels[mDifficultyPicker.getValue()];
        int dims=4;

        switch (difficulty){
            case "Beginner":
                dims = 2;
                break;
            case "Easy":
                dims = 3;
                break;
            case "Normal":
                dims = 4;
                break;
            case "Intermediate":
                dims = 5;
                break;
            case "Hard":
                dims = 6;
                break;
            case "Expert":
                dims = 7;
                break;

        }
        goToGamePlayScreen(dims);
    }
}
