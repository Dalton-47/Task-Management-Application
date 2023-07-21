package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class User_Profile_Activity extends AppCompatActivity {

    DatabaseReference userRef;
    FirebaseUser firebaseUser;
    FirebaseAuth authProfile;
    ProgressBar progressBar;
    ImageView imageView;
    EditText editTextName, editTextEmail, editTextAge;

    Button  btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        //get database reference to read users details
        userRef= FirebaseDatabase.getInstance().getReference("users");

        //get Current User
        authProfile = FirebaseAuth.getInstance();
         firebaseUser = authProfile.getCurrentUser();

        progressBar = (ProgressBar)  this.findViewById(R.id.progressBar);

        editTextName = (EditText)  this.findViewById(R.id.editTextTextNameUserProfile);
        editTextEmail = (EditText) this.findViewById(R.id.editTextTextEmailUserProfile);
        editTextAge = (EditText) this.findViewById(R.id.editTextTextAgeUserProfile);

        imageView = (ImageView)  this.findViewById(R.id.imageViewUserProfile);
        //send user to upload profile
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(User_Profile_Activity.this,UploadPicture_Activity.class));
                overridePendingTransition(R.anim.new_slide_in, R.anim.new_slide_out);
            }
        });

        if (firebaseUser == null){
            Toast.makeText(User_Profile_Activity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        btnBackHome = (Button)  this.findViewById(R.id.buttonBackToDashboard);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_Profile_Activity.this,Home_Page.class));
            }
        });


    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        if(firebaseUser !=null)
        {
            // User is signed in, you can get their details here
            String userId = firebaseUser.getUid();
            String email = firebaseUser.getEmail();

            userRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            progressBar.setVisibility(View.GONE);
                            DataSnapshot thisDataSnapshot=task.getResult();
                            editTextName.setText(String.valueOf(thisDataSnapshot.child("username").getValue()));
                            editTextEmail.setText(String.valueOf(thisDataSnapshot.child("email").getValue()));
                            editTextAge.setText(String.valueOf(thisDataSnapshot.child("age").getValue())+" years");

                            // Set User DP (After user has uploaded)
                            Uri uri = firebaseUser.getPhotoUrl();

                            if(uri!=null)
                            {
                                // ImageViewer setImageURI() should not be used with regular URIs. So we are using Picasso
                                Picasso.get().load(uri)
                                        .transform(new RoundedTransformation())
                                        .into(imageView);

                            }

                            progressBar.setVisibility(View.GONE);

                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(User_Profile_Activity.this, "Error: User Not Found!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(User_Profile_Activity.this, "Error: Check Network Connection!", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
        else {
            progressBar.setVisibility(View.GONE);
            // User is not signed in, you should redirect them to sign in page
            //  you can also perform some other action here
        }
    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            firebaseUser.sendEmailVerification();
            authProfile.signOut(); // Sign out user.
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(User_Profile_Activity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification next time.");

        // Open email Apps if User clicks/taps Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // To email app in new window and not within our app
                startActivity(intent);
            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        //get Current User
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        super.onResume();
        if (firebaseUser == null){
            Toast.makeText(User_Profile_Activity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }
}