package com.example.viddio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.viddio.databinding.ActivityMainBinding;
import com.example.viddio.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}