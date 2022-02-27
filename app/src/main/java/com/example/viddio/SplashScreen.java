package com.example.viddio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
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
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser() != null){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
                else{
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                }

            }
        },2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}