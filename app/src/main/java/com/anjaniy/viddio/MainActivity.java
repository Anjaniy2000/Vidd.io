package com.anjaniy.viddio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.anjaniy.viddio.fragments.AboutFragment;
import com.anjaniy.viddio.fragments.HomeFragment;
import com.anjaniy.viddio.fragments.ProfileFragment;
import com.anjaniy.viddio.R;
import com.anjaniy.viddio.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    //Bottom Navigation View:
    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch(item.getItemId()){

                case R.id.home:
                    binding.mainToolbar.setTitle(R.string.homeOption);
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.profile:
                    binding.mainToolbar.setTitle(R.string.profile);
                    selectedFragment = new ProfileFragment();
                    break;

                case R.id.about:
                    binding.mainToolbar.setTitle(R.string.about_app);
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
                .setPositiveButton("Yes", (dialog, which) -> {
                    //ACTION FOR "YES" BUTTON: -
                    finish();
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