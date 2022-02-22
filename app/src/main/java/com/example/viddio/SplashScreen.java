package com.example.viddio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashScreen_Task();
    }

    /* Working Of Splash Screen: */
    private void splashScreen_Task() {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, LoginActivity.class));

            }
        },2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}