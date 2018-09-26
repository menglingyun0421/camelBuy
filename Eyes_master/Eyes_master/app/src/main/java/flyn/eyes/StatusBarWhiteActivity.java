package flyn.eyes;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import flyn.Eyes;

public class StatusBarWhiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_white);
        getSupportActionBar().setTitle("StatusBarWhite");

        Eyes.setStatusBarLightMode(this, Color.WHITE);
    }
}
