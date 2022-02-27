package com.example.viddio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.viddio.databinding.ActivityMainBinding;
import com.example.viddio.databinding.ActivitySignupBinding;
import com.example.viddio.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        binding.signupButton.setOnClickListener(v -> {

            //Validations:
            if(binding.fullName.getText().toString().isEmpty()){
                binding.fullName.setError("Enter your full name");
                binding.fullName.requestFocus();
                return;
            }

            if(binding.emailForSignup.getText().toString().isEmpty()){
                binding.emailForSignup.setError("Enter your email address");
                binding.emailForSignup.requestFocus();
                return;
            }

            if(binding.passwordForSignup.getText().toString().isEmpty()){
                binding.passwordForSignup.setError("Enter your password");
                binding.passwordForSignup.requestFocus();
                return;
            }

            user = new User();
            user.setFullName(binding.fullName.getText().toString());
            user.setEmailAddress(binding.emailForSignup.getText().toString());
            user.setPassword(binding.passwordForSignup.getText().toString());

            auth.createUserWithEmailAndPassword(binding.emailForSignup.getText().toString(), binding.passwordForSignup.getText().toString()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    database.collection("Users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).set(user).addOnSuccessListener(unused -> {
                        Toast.makeText(SignupActivity.this, "Account is created!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finishAffinity();
                    });
                }
                else{
                    Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.backToLoginButton.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

    }
}