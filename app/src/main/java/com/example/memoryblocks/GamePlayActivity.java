package com.example.memoryblocks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GamePlayActivity extends Activity{

    private int currentPatternIdx = 0; // index of pattern array to keep track of which button user is on
    private Difficulty difficulty;
    private int size = 4;
    private int totalBlocks;
    private List<Integer> pattern;
    ArrayList<ArrayList<Button>> buttonGrid;
    TextView mScoreCounterTextView;
    TextView mHighScoreCounterTextView;
    private int currentScore;
    private int savedHighScore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
//        Toast.makeText(getApplicationContext(), "onCreate(Bundle)", Toast.LENGTH_SHORT).show();

        // Set References
        mScoreCounterTextView = (TextView) findViewById(R.id.score_counter_text_view);
        mHighScoreCounterTextView = (TextView) findViewById(R.id.high_score_counter_text_view);
        currentScore = 0;

        // Get Difficulty
        Bundle data = getIntent().getExtras();
        this.difficulty = Difficulty.EASY;
        if (data != null){
            difficulty = (Difficulty) data.get("difficulty");
        }
        size = difficulty.getDims();
        totalBlocks = size*size;

        // Create Grid
        createGrid(difficulty);

        // Create Pattern List
        pattern = new ArrayList<Integer>();
        pattern.add(getRandomBlock());
    }

    @Override
    protected void onStart() {
        int delayMs = 1000;
        Handler handler = new Handler();
        // If incorrect, start from the beginning
        // Show red
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after time
                showPatternClicked(null);
            }
        }, delayMs);
        super.onStart();
    }

    private void createGrid(Difficulty difficulty) {
        float pxWidth = getDp()[0];
        int margins = 10; //<!--TODO: Change so value isn't hardcoded-->
        buttonGrid = new ArrayList<>(size);
        GridLayout mMemoryBlockGridLayout = (GridLayout) findViewById(R.id.memory_block_grid_layout);
        int buttonDims = (int) Math.floor(pxWidth / size) - margins*2;

        for(int i=0; i < size; i++) {
            buttonGrid.add(new ArrayList<Button>());
            for (int j=0; j < size; j++) {
                Button button = new Button(getApplicationContext());
                GridLayout.LayoutParams params= new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(i+1);
                params.columnSpec = GridLayout.spec(j+1 );
                params.width = buttonDims;
                params.height = buttonDims;
                params.setMargins(margins,margins,margins,margins);
                button.setId(calculateId(i,j));
                button.setLayoutParams(params);
                button.setBackgroundResource(R.drawable.block_buttons);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        blockButtonClicked(v);
                    }
                });


                // Create local list of buttons
                buttonGrid.get(i).add(button);

                // Add buttons to GridLayout
                mMemoryBlockGridLayout.addView(button);
            }
        }
    }


    private float[] getDp() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return new float[]{outMetrics.widthPixels, outMetrics.heightPixels };
    }

    public void showPatternClicked(View view){
        // <!-- TODO: Make it so that show pattern can only be used a number of times -->
        Button mCurrentButton;
        final int buttonLightDelayMs = 500;
        this.currentPatternIdx = 0; // Restarts index to beginning

        showPattern(0, buttonLightDelayMs); // recursively shows pattern
    }

    private void showPattern(final int idx, final int delayMs) {
        // Reference to specific button
        currentPatternIdx = 0;
        int currentId = pattern.get(idx);
        Button mCurrentButton = (Button) findViewById(currentId);

        // Light up buttons
        mCurrentButton.setBackgroundResource(R.drawable.pattern_button);
        Handler handler = new Handler();
        final Button finalMCurrentButton = mCurrentButton;
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after time
                finalMCurrentButton.setBackgroundResource(R.drawable.block_buttons);
                if (idx != pattern.size()-1)
                    showPattern(idx+1,delayMs);
            }
        }, delayMs);
    }

    public void blockButtonClicked(View view) {
        // check if button id matches with current block in pattern
        final int delayMs = 500;
        int userClickedButtonId = view.getId();
        int currentPatternButtonId = pattern.get(currentPatternIdx);
        int lastPatternIdx = pattern.size()-1;

        if (currentPatternButtonId == userClickedButtonId){ // Correct
            // Check if last. If last element in the pattern list, then add new block to pattern
            // add points
            if (currentPatternIdx == lastPatternIdx){
                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                // New block cannot be the same as the topmost
                pattern.add(getRandomBlock());
                currentScore += 10;
                mScoreCounterTextView.setText(String.valueOf(currentScore));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after time
                        showPatternClicked(null);
                    }
                }, delayMs);
            }

            // If correct, then add another block to pattern, then increment currentPatternIDx
            currentPatternIdx++;
        }
        else { // Incorrect
            Button mCurrentButton = (Button) findViewById(userClickedButtonId);
            mCurrentButton.setBackgroundResource(R.drawable.block_buttons_red);
            Handler handler = new Handler();
            final Button finalMCurrentButton = mCurrentButton;
            // If incorrect, start from the beginning
            // Show red
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after time
                    finalMCurrentButton.setBackgroundResource(R.drawable.block_buttons);
                }
            }, delayMs);
            currentPatternIdx=0;
            Toast.makeText(getApplicationContext(), "Incorrect. Restart.", Toast.LENGTH_SHORT).show();

            currentScore=0;
            mScoreCounterTextView.setText(String.valueOf(currentScore));
        }

    }


//    private void loadHighScore() {
//        // <!-- TODO: Load highscore
//        // store into saved high score
//    }
//
//    private void saveHighScore() {
//        // <--! TODO: Save highscore
//        SharedPreferences preferences = getSharedPreferences("HIGH_SCORE_PREF", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        String key = "";
//        switch (this.difficulty) {
//            case EASY:
//                key = "highScoreEasy";
//                break;
//            case MEDIUM:
//                key = "highScoreMedium";
//                break;
//            case HARD:
//                key = "highScoreHard";
//                break;
//            default:
//                break;
//        }
//
//        // <!-- TODO: Get high score and compare
//        savedHighScore = currentScore;
//        editor.putInt(key, savedHighScore);
//        // save high score
//        mHighScoreCounterTextView.setText(savedHighScore);
//    }

//    private Pair<Integer, Integer> getRandomBlock() {
//        // get a random row
//        Integer row = generateRandomIntIntRange(1, size);
//        // get a random column
//        Integer column = generateRandomIntIntRange(1, size);
//        // return the pair
//        return new Pair<Integer, Integer>(row, column);
//    }

    private int calculateId(int row, int col) {
        return  (row * size) + col;
    }

    private int getRandomBlock() {
        int returnBlock = 0;
        if (!pattern.isEmpty()){
            int lastBlock = pattern.get(pattern.size()-1);
            if (lastBlock < Math.floor(totalBlocks/2.0))
                returnBlock = generateRandomIntIntRange(lastBlock+1, totalBlocks);
            else
                returnBlock = generateRandomIntIntRange(1, lastBlock);
        }
        else
            returnBlock = generateRandomIntIntRange(1, totalBlocks);

        return returnBlock;
    }

    private int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}
