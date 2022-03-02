package com.anjaniy.viddio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anjaniy.viddio.R;
import com.anjaniy.viddio.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        //Login:
        binding.loginButton.setOnClickListener(v -> {

            //Validations:
            if(binding.emailForLogin.getText().toString().isEmpty()){
                binding.emailForLogin.setError("Enter your email address");
                binding.emailForLogin.requestFocus();
                return;
            }

            if(binding.passwordForLogin.getText().toString().isEmpty()){
                binding.passwordForLogin.setError("Enter your password");
                binding.passwordForLogin.requestFocus();
                return;
            }

            showProgressDialog();
            auth.signInWithEmailAndPassword(binding.emailForLogin.getText().toString(), binding.passwordForLogin.getText().toString()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    dismissDialog();
                    finishAffinity();
                }
                else{
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    dismissDialog();
                }
            });
        });

        //Create Account:
        binding.createAccountButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);

        builder.setMessage("Do You Want To Close This App?")

                .setCancelable(false)

                //CODE FOR POSITIVE(YES) BUTTON: -
                .setPositiveButton("Yes", (dialog, which) -> {
                    //ACTION FOR "YES" BUTTON: -
                    finishAffinity();
                })

                //CODE FOR NEGATIVE(NO) BUTTON: -
                .setNegativeButton("No", (dialog, which) -> {
                    //ACTION FOR "NO" BUTTON: -
                    dialog.cancel();
                });

        //CREATING A DIALOG-BOX: -
        AlertDialog alertDialog = builder.create();
        //SET TITLE MAUALLY: -
        alertDialog.setTitle("Exit");
        alertDialog.show();
    }
}