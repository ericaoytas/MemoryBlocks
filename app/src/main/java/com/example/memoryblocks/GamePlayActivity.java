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
    private int dims = 4;
    private int totalBlocks;
    private List<Integer> pattern;
    ArrayList<ArrayList<Button>> buttonGrid;
    TextView mScoreCounterTextView;
    TextView mHighScoreCounterTextView;
    Button mShowPatternButton;
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
        mShowPatternButton = (Button) findViewById(R.id.show_pattern_button);
        currentScore = 0;

        // Get Dims
        Bundle data = getIntent().getExtras();
        if (data != null){
            dims = (int) data.get("difficulty");
        }

        totalBlocks = dims*dims;

        // Create Grid
        createGrid();

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

    private void createGrid() {
        float pxWidth = getDp()[0];
        int margins = 10; //<!--TODO: Change so value isn't hardcoded-->
        buttonGrid = new ArrayList<>(dims);
        GridLayout mMemoryBlockGridLayout = (GridLayout) findViewById(R.id.memory_block_grid_layout);
        int buttonDims = (int) Math.floor(pxWidth / dims) - margins*2;

        for(int i=0; i < dims; i++) {
            buttonGrid.add(new ArrayList<Button>());
            for (int j=0; j < dims; j++) {
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

        mShowPatternButton.setEnabled(false);

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
        try {
            mCurrentButton.setBackgroundResource(R.drawable.pattern_button); // <--!TODO fix FATAL EXCEPTION. Saying attempt to invoke virtual method on a null object reference
            Handler handler = new Handler();
            final Button finalMCurrentButton = mCurrentButton;
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after time
                    finalMCurrentButton.setBackgroundResource(R.drawable.block_buttons);
                    if (idx != pattern.size()-1)
                        showPattern(idx+1,delayMs);
                    mShowPatternButton.setEnabled(true);
                }
            }, delayMs);
        }
        catch (Exception e) {
            mShowPatternButton.setEnabled(true);
            onStart();
        }


    }

    public void blockButtonClicked(View view) {
        // check if button id matches with current block in pattern
        final int delayMs = 500;
        int userClickedButtonId = view.getId();
        int currentPatternButtonId = pattern.get(currentPatternIdx);
        int lastPatternIdx = pattern.size()-1;
//        Toast toast = new Toast(this);
//        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0);
        if (currentPatternButtonId == userClickedButtonId){ // Correct
            // Check if last. If last element in the pattern list, then add new block to pattern
            // add points
            if (currentPatternIdx == lastPatternIdx){
//                toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
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
//            toast.makeText(getApplicationContext(), "Incorrect. Restart.", Toast.LENGTH_SHORT).show();

            // Save high score
            saveHighScore();

            // Reset score
            currentScore=0;
            mScoreCounterTextView.setText(String.valueOf(currentScore));

            // Reset Pattern
            pattern.clear();
            pattern.add(getRandomBlock());
            this.onStart();
        }

    }


//    private void loadHighScore() {
//        // <!-- TODO: Load highscore
//        // store into saved high score
//    }
//
    private void saveHighScore() {
        // <--! TODO: Save highscore
        if (currentScore > savedHighScore) {
            savedHighScore = currentScore;
            mHighScoreCounterTextView.setText(String.valueOf(savedHighScore));
        }

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
    }


    private int calculateId(int row, int col) {
        return  (row * dims) + col;
    }

    private int getRandomBlock() {
        int returnBlock = 0;
        if (!pattern.isEmpty()){
            int lastBlock = pattern.get(pattern.size()-1);
            if (lastBlock < Math.floor(totalBlocks/2.0))
                returnBlock = generateRandomIntIntRange(lastBlock+1, totalBlocks);
            else
                returnBlock = generateRandomIntIntRange(1, lastBlock-1);
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
