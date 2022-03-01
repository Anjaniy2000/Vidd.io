package com.example.viddio.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class ProfileFragment extends Fragment {

    private View view;
    private CardView signoutCard;
    private CardView deleteCard;
    private CardView forgotPasswordCard;
    private CardView updateUsername;
    private TextView name;
    private TextView email;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        widgetSetup();

        //Displaying Name & Email:
        initialTask();

        //Update Username:
        updateUsername.setOnClickListener(v -> {
            final EditText newUsername = new EditText(getActivity());
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogStyle);

            builder.setMessage("Enter your new username")

                    .setCancelable(false)

                    //CODE FOR POSITIVE(YES) BUTTON: -
                    .setPositiveButton("Save", (dialog, which) -> {
                        //ACTION FOR "YES" BUTTON: -
                        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        Map<String, Object> map = new HashMap<>();
                        map.put("fullName",newUsername.getText().toString());

                        reference.update(map).addOnSuccessListener(unused -> {
                            initialTask();
                            Toast.makeText(getActivity(), "Username has been updated!", Toast.LENGTH_LONG).show();
                        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    })

                    //CODE FOR NEGATIVE(NO) BUTTON: -
                    .setNegativeButton("Back", (dialog, which) -> {
                        //ACTION FOR "NO" BUTTON: -
                        dialog.cancel();
                    });

            //CREATING A DIALOG-BOX: -
            AlertDialog alertDialog = builder.create();
            //SET TITLE MAUALLY: -
            alertDialog.setView(newUsername);
            alertDialog.setTitle("Update username");
            alertDialog.show();
        });

        //Forgot / Reset Password:
        forgotPasswordCard.setOnClickListener(v -> {

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogStyle);

            builder.setMessage("Are you sure you forgot your password?")

                    .setCancelable(false)

                    //CODE FOR POSITIVE(YES) BUTTON: -
                    .setPositiveButton("Yes", (dialog, which) -> {
                        showProgressDialog();
                        //ACTION FOR "YES" BUTTON: -
                        FirebaseAuth.getInstance().sendPasswordResetEmail(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())).addOnSuccessListener(unused -> {
                            dismissDialog();
                            Toast.makeText(getActivity(), "Password reset link has been sent to your email.", Toast.LENGTH_LONG).show();
                        }).addOnFailureListener(e -> {
                            dismissDialog();
                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
            alertDialog.setTitle("Forgot password");
            alertDialog.show();
        });

        //Sign Out:
        signoutCard.setOnClickListener(v -> {

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogStyle);

            builder.setMessage("Do you want to sign out?")

                    .setCancelable(false)

                    //CODE FOR POSITIVE(YES) BUTTON: -
                    .setPositiveButton("Yes", (dialog, which) -> {
                        showProgressDialog();
                        //ACTION FOR "YES" BUTTON: -
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getActivity(), "Sign out has been successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        dismissDialog();
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

        //Delete Account:
        deleteCard.setOnClickListener(v -> {

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogStyle);

            builder.setMessage("Do you want to delete this account?")

                    .setCancelable(false)

                    //CODE FOR POSITIVE(YES) BUTTON: -
                    .setPositiveButton("Yes", (dialog, which) -> {
                        showProgressDialog();
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
                                            Toast.makeText(getActivity(), "Account has been deleted successfully", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getActivity(), SplashScreen.class));
                                            dismissDialog();
                                        }
                                        else{
                                            dismissDialog();
                                            Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    })).addOnFailureListener(e -> {
                                        dismissDialog();
                                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    });
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

    //Setting up Widgets:
    private void widgetSetup() {
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        updateUsername = view.findViewById(R.id.updateUsername);
        forgotPasswordCard = view.findViewById(R.id.forgot_password_card);
        signoutCard = view.findViewById(R.id.sign_out_card);
        deleteCard = view.findViewById(R.id.delete_account_card);
    }

    //Initial Task:
    private void initialTask() {
        showProgressDialog();
        email.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        DocumentReference reference2 = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference2.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();
                name.setText(snapshot.getString("fullName"));
                dismissDialog();
            }
            else{
                dismissDialog();
                Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.show();
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
