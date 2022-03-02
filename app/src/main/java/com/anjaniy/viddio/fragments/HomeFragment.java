package com.anjaniy.viddio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anjaniy.viddio.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    private View view;
    private Button join, share;
    private EditText meetingCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);
        try {
            URL serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        widgetSetup();

        //Join:
        join.setOnClickListener(v -> {

            if(meetingCode.getText().toString().isEmpty()){
                meetingCode.setError("Please enter meeting code !");
                meetingCode.requestFocus();
            }
            else{
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(meetingCode.getText().toString())
                        .setWelcomePageEnabled(false)
                        .build();

                JitsiMeetActivity.launch(requireActivity().getApplicationContext(), options);
            }
        });

        //Share:
        share.setOnClickListener(v -> {

            if(meetingCode.getText().toString().isEmpty()){
                meetingCode.setError("There is no meeting code !");
                meetingCode.requestFocus();
            }
            else{
                String subject = "Use this meeting code to join!";
                Intent txtIntent = new Intent(Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(Intent.EXTRA_SUBJECT, subject);
                txtIntent .putExtra(Intent.EXTRA_TEXT, meetingCode.getText().toString());
                startActivity(Intent.createChooser(txtIntent ,"Share this meeting code using:"));
            }


        });
        return view;
    }

    //Setting up Widgets:
    private void widgetSetup() {
        join = view.findViewById(R.id.joinButton);
        share = view.findViewById(R.id.shareButton);
        meetingCode = view.findViewById(R.id.meetingCode);
    }
}
