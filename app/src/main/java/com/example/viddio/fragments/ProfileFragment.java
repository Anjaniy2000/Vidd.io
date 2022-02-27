package com.example.viddio.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.viddio.LoginActivity;
import com.example.viddio.R;
import com.example.viddio.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private View view;
    private CardView signoutCard;
    private CardView deleteCard;
    private CardView forgotPasswordCard;
    private String getName;
    private TextView name;
    private TextView email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        widgetSetup();

        forgotPasswordCard.setOnClickListener(v -> {

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogStyle);

            builder.setMessage("Are you sure you forgot your password?")

                    .setCancelable(false)

                    //CODE FOR POSITIVE(YES) BUTTON: -
                    .setPositiveButton("Yes", (dialog, which) -> {
                        //ACTION FOR "YES" BUTTON: -
                        FirebaseAuth.getInstance().sendPasswordResetEmail(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())).addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Password reset link has been sent to your Email.", Toast.LENGTH_LONG).show()).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
                    })

                    //CODE FOR NEGATIVE(NO) BUTTON: -
                    .setNegativeButton("No", (dialog, which) -> {
                        //ACTION FOR "NO" BUTTON: -
                        dialog.cancel();
                    });

            //CREATING A DIALOG-BOX: -
            AlertDialog alertDialog = builder.create();
            //SET TITLE MAUALLY: -
            alertDialog.setTitle("Forgot password");
            alertDialog.show();
        });

        signoutCard.setOnClickListener(v -> {

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogStyle);

            builder.setMessage("Do you want to sign out?")

                    .setCancelable(false)

                    //CODE FOR POSITIVE(YES) BUTTON: -
                    .setPositiveButton("Yes", (dialog, which) -> {
                        //ACTION FOR "YES" BUTTON: -
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getActivity(), "Sign out successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    })

                    //CODE FOR NEGATIVE(NO) BUTTON: -
                    .setNegativeButton("No", (dialog, which) -> {
                        //ACTION FOR "NO" BUTTON: -
                        dialog.cancel();
                    });

            //CREATING A DIALOG-BOX: -
            AlertDialog alertDialog = builder.create();
            //SET TITLE MAUALLY: -
            alertDialog.setTitle("Sign out");
            alertDialog.show();

        });

        deleteCard.setOnClickListener(v -> {

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogStyle);

            builder.setMessage("Do you want to delete this account?")

                    .setCancelable(false)

                    //CODE FOR POSITIVE(YES) BUTTON: -
                    .setPositiveButton("Yes", (dialog, which) -> {
                        //ACTION FOR "YES" BUTTON: -
                        FirebaseFirestore.getInstance().collection("Users")
                                .whereEqualTo("emailAddress", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                                    WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
                                    List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();

                                    for(DocumentSnapshot snapshot: snapshots){
                                            writeBatch.delete(snapshot.getReference());
                                    }

                                    writeBatch.commit().addOnSuccessListener(unused -> Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete().addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getActivity(), SplashScreen.class));
                                        }
                                        else{
                                            Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    })).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
                                });
                    })

                    //CODE FOR NEGATIVE(NO) BUTTON: -
                    .setNegativeButton("No", (dialog, which) -> {
                        //ACTION FOR "NO" BUTTON: -
                        dialog.cancel();
                    });

            //CREATING A DIALOG-BOX: -
            AlertDialog alertDialog = builder.create();
            //SET TITLE MAUALLY: -
            alertDialog.setTitle("Delete account");
            alertDialog.show();
        });

        return view;
    }

    private void widgetSetup() {
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        forgotPasswordCard = view.findViewById(R.id.forgot_password_card);
        signoutCard = view.findViewById(R.id.sign_out_card);
        deleteCard = view.findViewById(R.id.delete_account_card);
    }
}
