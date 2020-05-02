package com.example.memoryblocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void goBackToHomeScreen(View view) {
        finish();
    }
}
