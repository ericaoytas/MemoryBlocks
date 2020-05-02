package com.example.memoryblocks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlayActivity extends Activity {

    ArrayList<Button> buttonGrid;
    TextView mScoreCounterTextView;
    TextView mHighScoreCounterTextView;
    Button mReplayPatternButton;
    GridLayout mGridLayout;
    private int currentPatternIdx = 0;  // index of pattern array to keep track of which button user is on
    private int dims = 4;               // width and height of the memory block grid (default: 4)
    private int totalBlocks;            // total amount of blocks on a square memory block grid
    private List<Integer> pattern;      // list of ids representing the patter to be mimicked
    private int currentScore=0;
    private int savedHighScore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Log.i("GamePlayMyTag", "Activity Created");

        // Set References
        mScoreCounterTextView = findViewById(R.id.score_counter_text_view);
        mHighScoreCounterTextView = findViewById(R.id.high_score_counter_text_view);
        mReplayPatternButton = findViewById(R.id.show_pattern_button);
        mGridLayout = findViewById(R.id.memory_block_grid_layout);

        // Get dims based on difficulty
        Bundle data = getIntent().getExtras();
        if (data != null){
            dims = (int) data.get("difficulty");
        }
        totalBlocks = dims*dims;

        pattern = new ArrayList<>();
        pattern.add(getRandomBlock());

        createGrid();
        loadHighScore();
    }

    @Override
    protected void onStart() {
        // Automatically play pattern
        int delayMs = 1000;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after time
                replayPatternClicked(mReplayPatternButton);
            }
        }, delayMs);

        super.onStart();
    }

    @Override
    protected void onPause() {
        // TODO: Add pause popup
        super.onPause();
    }

    /** Programmatically creates n by n blocks and adds to mGridLayout based on difficulty */
    
    private void createGrid() {
        int margins = 10;
        float pxWidth = getPxDims()[0];
        buttonGrid = new ArrayList<>(dims);
        int buttonDims = (int) Math.floor(pxWidth / dims) - margins*2;

        for(int i=0; i < dims; i++) {
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
                buttonGrid.add(button);

                // Add buttons to GridLayout
                mGridLayout.addView(button);
            }
        }
    }

    /** Calls replayButton() helper function, after ReplayPattern Button is clicked */
    
    public void replayPatternClicked(View view){
        // Disable all the buttons on the grid while pattern is showing
        disableGrid();
        mReplayPatternButton.setEnabled(false);
        this.currentPatternIdx = 0; // Restarts index to beginning
        final int buttonLightDelayMs = 500;
        replayPattern(0, buttonLightDelayMs); // recursively shows pattern
    }

    /** Recursively shows the pattern on the grid*/

    private void replayPattern(final int idx, final int delayMs) {
        currentPatternIdx = 0;
        try {
            int currentId = pattern.get(idx);
            Button mCurrentButton = buttonGrid.get(currentId-1);
            mCurrentButton.setBackgroundResource(R.drawable.pattern_button);
            Handler handler = new Handler();
            final Button finalMCurrentButton = mCurrentButton;
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after time
                    finalMCurrentButton.setBackgroundResource(R.drawable.block_buttons);
                    if (idx >= pattern.size()-1) {
                        mReplayPatternButton.setEnabled(true);
                        enableGrid();
                    } else {
                        replayPattern( idx+1,delayMs);
                    }
                }
            }, delayMs);
        }
        catch (NullPointerException e) {
            mReplayPatternButton.setEnabled(true);
            enableGrid();

            Log.i("GamePlayActivity", "replayPattern(int, int): " + e.getMessage());
        }
    }

    /** Checks if player clicked on the correct button.
     *  If correct, button turns blue, and longer pattern is shown.
     *  If incorrect, button turns red, and score is reset and a new pattern is shown. */

    public void blockButtonClicked(View view) {
        final int delayMs = 500;
        int userClickedButtonId = view.getId();
        int currentPatternButtonId = pattern.get(currentPatternIdx);
        int lastPatternIdx = pattern.size()-1;

        if (currentPatternButtonId-1 == userClickedButtonId){               // Correct
            if (currentPatternIdx == lastPatternIdx){
                pattern.add(getRandomBlock());
                currentScore += 10;
                mScoreCounterTextView.setText(String.valueOf(currentScore));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        replayPatternClicked(mReplayPatternButton);
                    }
                }, delayMs);
            }
            currentPatternIdx++;
        } else {                                                            // Incorrect
            Button mCurrentButton = buttonGrid.get(userClickedButtonId);
            mCurrentButton.setBackgroundResource(R.drawable.block_buttons_red);
            Handler handler = new Handler();
            final Button finalMCurrentButton = mCurrentButton;
            handler.postDelayed(new Runnable() {
                public void run() {
                    finalMCurrentButton.setBackgroundResource(R.drawable.block_buttons);
                }
            }, delayMs);

            saveHighScore();

            // Reset score and pattern
            currentScore=0;
            mScoreCounterTextView.setText(String.valueOf(currentScore));
            currentPatternIdx=0;
            pattern.clear();
            pattern.add(getRandomBlock());
            this.onStart();
        }
    }

    /** Loads high score that was saved on the device. */

    private void loadHighScore() {
        SharedPreferences preferences = getSharedPreferences("HIGH_SCORE_PREF", Context.MODE_PRIVATE);
        String key = "";
        switch (this.dims) {
            case 2:
                key = "highScoreBeginner";
                break;
            case 3:
                key = "highScoreEasy";
                break;
            case 4:
                key = "highScoreNormal";
                break;
            case 5:
                key = "highScoreMedium";
                break;
            case 6:
                key = "highScoreHard";
                break;
            case 7:
                key = "highScoreExpert";
                break;
            default:
                break;
        }

        if (preferences.contains(key)){
            savedHighScore = preferences.getInt(key, 0);
        }

        mHighScoreCounterTextView.setText(String.valueOf(savedHighScore));
    }

    /** Saves high score to device, and updates high score text. */

    private void saveHighScore() {
        if (currentScore > savedHighScore) {
            savedHighScore = currentScore;
            // Save high score in the system
            SharedPreferences preferences = getSharedPreferences("HIGH_SCORE_PREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String key = "";
            switch (this.dims) {
                case 2:
                    key = "highScoreBeginner";
                    break;
                case 3:
                    key = "highScoreEasy";
                    break;
                case 4:
                    key = "highScoreNormal";
                    break;
                case 5:
                    key = "highScoreMedium";
                    break;
                case 6:
                    key = "highScoreHard";
                    break;
                case 7:
                    key = "highScoreExpert";
                    break;
                default:
                    break;
            }

            editor.putInt(key, savedHighScore);
            editor.apply();
            mHighScoreCounterTextView.setText(String.valueOf(savedHighScore));
        }
    }

    /** Calculates the row-major id of the grid*/

    private int calculateId(int row, int col) {
        return  (row * dims) + col;
    }

    /** Disables the memory block buttons */

    private void disableGrid() {
        for (int i = 0; i < totalBlocks; i++) {
            buttonGrid.get(i).setEnabled(false);
        }
    }

    /** Enables the memory block buttons */

    private void enableGrid() {
        for (int i = 0; i < totalBlocks; i++) {
            buttonGrid.get(i).setEnabled(true);
        }
    }


    /** Returns id of random block, that is not the same as the last in the pattern */

    private int getRandomBlock() {
        int returnBlock;
        if (!pattern.isEmpty()){
            int lastBlock = pattern.get(pattern.size()-1);
            if (lastBlock < Math.floor(totalBlocks/2.0))
                returnBlock = generateRandomIntIntRange(lastBlock+1, totalBlocks);
            else
                returnBlock = generateRandomIntIntRange(1, lastBlock-1);
        }
        else {
            returnBlock = generateRandomIntIntRange(1, totalBlocks);
        }

        return returnBlock;
    }

    /** Returns pixel dimensions of the screen */

    private float[] getPxDims() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return new float[]{outMetrics.widthPixels, outMetrics.heightPixels };
    }

    /** Returns random int given a range */

    private int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}
