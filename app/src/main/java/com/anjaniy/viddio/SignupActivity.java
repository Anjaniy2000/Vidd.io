package com.anjaniy.viddio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anjaniy.viddio.models.User;
import com.anjaniy.viddio.R;
import com.anjaniy.viddio.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private User user;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        //Sign Up:
        binding.signupButton.setOnClickListener(v -> {

            //Validations:
            if(binding.fullName.getText().toString().isEmpty()){
                binding.fullName.setError("Enter your full name");
                binding.fullName.requestFocus();
                return;
            }

            if(binding.fullName.getText().toString().length() <= 6){
                binding.fullName.setError("Please enter your full name");
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

            showProgressDialog();
            auth.createUserWithEmailAndPassword(binding.emailForSignup.getText().toString(), binding.passwordForSignup.getText().toString()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    database.collection("Users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).set(user).addOnSuccessListener(unused -> {
                        Toast.makeText(SignupActivity.this, "Account is created!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        dismissDialog();
                        finishAffinity();
                    });
                }
                else{
                    Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    dismissDialog();
                }
            });
        });

        //Back To Login:
        binding.backToLoginButton.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(SignupActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void dismissDialog() {
        dialog.dismiss();
    }
}