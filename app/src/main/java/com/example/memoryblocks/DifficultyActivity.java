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
        difficultyLevels = new String[]{"Beginner", "Easy", "Normal", "Medium", "Hard", "Expert"};
        mDifficultyPicker.setMinValue(0);
        mDifficultyPicker.setMaxValue(difficultyLevels.length-1);
        mDifficultyPicker.setWrapSelectorWheel(false);
        mDifficultyPicker.setDisplayedValues(difficultyLevels);
        mDifficultyPicker.setValue(2);
    }

    /** Starts GamePlayActivity */
    public void goToGamePlayScreen(int dims) {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("difficulty", dims);        // Passes the dimensions
        startActivity(intent);
    }


    public void difficultyConfirmButtonClicked(View view) {
        // Dimension of the memory block grid is determined by the difficulty that the user picks
        String difficulty = difficultyLevels[mDifficultyPicker.getValue()];

        int dims=4;  // Default: 4

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
            case "Medium":
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

    public void goBackToHomeScreen(View view) {
        finish();
    }
}
