package com.example.viddio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
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
//    private URL serverURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();



//        try {
//            serverURL = new URL("https://meet.jit.si");
//            JitsiMeetConferenceOptions defaultOptions =
//                    new JitsiMeetConferenceOptions.Builder()
//                            .setServerURL(serverURL)
//                            .setWelcomePageEnabled(false)
//                            .build();
//            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//
//        binding.joinButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                        .setRoom(binding.meetingCode.getText().toString())
//                        .setWelcomePageEnabled(false)
//                        .build();
//
//                JitsiMeetActivity.launch(MainActivity.this, options);
//
//            }
//        });
//
//        binding.shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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
}