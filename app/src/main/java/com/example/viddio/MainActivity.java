package com.example.viddio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.viddio.databinding.ActivityMainBinding;
import com.example.viddio.fragments.AboutFragment;
import com.example.viddio.fragments.HomeFragment;
import com.example.viddio.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch(item.getItemId()){

                case R.id.home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.profile:
                    selectedFragment = new ProfileFragment();
                    break;

                case R.id.about:
                    selectedFragment = new AboutFragment();
                    break;

            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);

        builder.setMessage("Do You Want To Close This App?")

                .setCancelable(false)

                //CODE FOR POSITIVE(YES) BUTTON: -
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ACTION FOR "YES" BUTTON: -
                        finish();
                    }
                })

                //CODE FOR NEGATIVE(NO) BUTTON: -
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ACTION FOR "NO" BUTTON: -
                        dialog.cancel();
                    }
                });

        //CREATING A DIALOG-BOX: -
        AlertDialog alertDialog = builder.create();
        //SET TITLE MAUALLY: -
        alertDialog.setTitle("Exit");
        alertDialog.show();
    }
}