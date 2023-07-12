package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Registration_Page extends AppCompatActivity {
EditText editTextUsername, editTextAge,editTextEmail,editTextPassword;
String username,age,email,password;
ProgressBar progressbar;
DatabaseReference usersRef;
FirebaseAuth userAuth;
Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        editTextUsername = (EditText)  this.findViewById(R.id.editTextUserName);
        editTextAge = (EditText)  this.findViewById(R.id.editTextAge);
        editTextPassword = (EditText)  this.findViewById(R.id.editTextPassword);
        editTextEmail = (EditText)  this.findViewById(R.id.editTextEmail);

        progressbar = (ProgressBar) this.findViewById(R.id.progressBarRegistration);
       usersRef = FirebaseDatabase.getInstance().getReference("users");
        userAuth=FirebaseAuth.getInstance();

       btnRegister= (Button)  this.findViewById(R.id.buttonUserRegistration );
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser()
    {
        username=editTextUsername.getText().toString().trim();
        age=editTextAge.getText().toString().trim();
        password=editTextPassword.getText().toString().trim();
        email=editTextEmail.getText().toString().trim();

        String error = "Cannot be blank";
        if(username.isEmpty())
        {
            editTextUsername.setError(error);
            editTextUsername.requestFocus();
        }
        else if(age.isEmpty())
        {
            editTextAge.setError(error);
            editTextAge.requestFocus();

        }
        else if(email.isEmpty())
        {
            editTextEmail.setError(error);
            editTextEmail.requestFocus();
        }
        else if(password.isEmpty())
        {
            editTextPassword.setError(error);
            editTextPassword.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Provide a valid Email!");
            editTextEmail.requestFocus();
        }
        else if(password.length()<8)
        {
            editTextPassword.setError("Minimum Password Characters Should Be 8");
            editTextPassword.requestFocus();
        }
        else
        {

            progressbar.setVisibility(View.VISIBLE);
            userAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   //start
                    Query query = usersRef.orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                //if user email exists
                                progressbar.setVisibility(View.GONE);
                               editTextEmail.setError("Email is already used");
                               editTextEmail.requestFocus();
                            }else {
                                //email does not exist and we proceed with registration

                                if (task.isSuccessful()) {
                                    // User creation is successful
                                    FirebaseUser user = userAuth.getCurrentUser();
                                    String userId = user.getUid();

                                    // Create a new user object with username and age
                                    User newUser = new User(username, age, email);

                                    // Save the user object to the Firebase Realtime Database
                                  //  DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                    usersRef.child(userId).setValue(newUser)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // User data saved successfully
                                                        progressbar.setVisibility(View.GONE);
                                                        Toast.makeText(Registration_Page.this, "User created and data saved successfully", Toast.LENGTH_SHORT).show();
                                                       Intent myIntent = new Intent(Registration_Page.this, MainActivity.class);
                                                       startActivity(myIntent);
                                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                                        finish();

                                                    } else {
                                                        // Failed to save user data
                                                        progressbar.setVisibility(View.GONE);
                                                        Toast.makeText(Registration_Page.this, "Failed to save user data", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                } else {
                                    // User creation failed
                                    progressbar.setVisibility(View.GONE);
                                    Toast.makeText(Registration_Page.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(Registration_Page.this, "Retry!", Toast.LENGTH_SHORT).show();

                        }
                    });

                    //end
                }
            });

        }
    }
}