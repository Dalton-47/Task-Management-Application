package com.example.luna;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    ConstraintLayout constraintLayoutForgotEmail;
    TextView textViewForgotPassword;
    EditText editTextLoginEmail, editTextLoginPassword;
    FirebaseAuth userAuth;
    ProgressBar progressbar;
    FirebaseUser firebaseUser;
    Button btnRegisterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user is already logged in
        if (isLoggedIn()) {
            Intent myIntent = new Intent(MainActivity.this, Home_Page.class);
            startActivity(myIntent);
            finish(); // Close LoginActivity
        }

        progressbar = (ProgressBar)  this.findViewById(R.id.progressBarLogin);

        btnLogin =   (Button)  this.findViewById(R.id.buttonLogin);
        constraintLayoutForgotEmail = (ConstraintLayout)  this.findViewById(R.id.constraintlayoutForgotEmail);
        textViewForgotPassword = (TextView) this.findViewById(R.id.textViewForgotPaswword);

        editTextLoginEmail = (EditText)  this.findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = (EditText)  this.findViewById(R.id.editTextLoginPassword);

        btnRegisterUser = (Button)  this.findViewById(R.id.buttonRegisterUser);
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Registration_Page.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        userAuth= FirebaseAuth.getInstance();
        // Get instance of the current user
        firebaseUser = userAuth.getCurrentUser();

        final Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        final Animation slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);


        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (constraintLayoutForgotEmail.getVisibility() == View.VISIBLE) {
                    constraintLayoutForgotEmail.startAnimation(slideOutAnimation);
                    constraintLayoutForgotEmail.setVisibility(View.GONE);
                } else {
                    constraintLayoutForgotEmail.setVisibility(View.VISIBLE);
                    constraintLayoutForgotEmail.startAnimation(slideInAnimation);
                }

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              loginUser();
            }
        });

        constraintLayoutForgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (constraintLayoutForgotEmail.getVisibility() == View.VISIBLE) {
                    constraintLayoutForgotEmail.startAnimation(slideOutAnimation);
                    constraintLayoutForgotEmail.setVisibility(View.GONE);
                } else {
                    constraintLayoutForgotEmail.setVisibility(View.VISIBLE);
                    constraintLayoutForgotEmail.startAnimation(slideInAnimation);
                }
            }
        });

    }

    public void loginUser() {
        String email = editTextLoginEmail.getText().toString();
        String password = editTextLoginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            // Your existing code for handling empty email field
        } else if (password.isEmpty()) {
            // Your existing code for handling empty password field
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Your existing code for handling invalid email format
        } else {
            progressbar.setVisibility(View.VISIBLE);
            userAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Get instance of the current user
                        FirebaseUser firebaseUser = userAuth.getCurrentUser();

                        // Check if the email is verified before the user can access their profile
                        assert firebaseUser != null;
                        if (firebaseUser.isEmailVerified()) {
                            // Open user profile
                            // Start the main homepage.
                            Toast.makeText(MainActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                            // Save the login status to SharedPreferences
                            saveLoginStatus(true);

                            Intent myIntent = new Intent(MainActivity.this, Home_Page.class);
                            startActivity(myIntent); // start next Activity
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            progressbar.setVisibility(View.GONE);
                            finish(); // Close LoginActivity
                        } else {
                            progressbar.setVisibility(View.GONE);
                            firebaseUser.sendEmailVerification();
                            userAuth.signOut(); // Sign out user.
                            showAlertDialogue();
                        }
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            editTextLoginEmail.setError("User does not exist or is no longer valid. Please register again.");
                            editTextLoginEmail.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            editTextLoginEmail.setError("Invalid credentials. Kindly check and re-enter.");
                            editTextLoginEmail.requestFocus();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Check Network Connection!", Toast.LENGTH_SHORT).show();
                            // Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressbar.setVisibility(View.GONE);
                }
            });
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }


    private void showAlertDialogue() {
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification.");

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


}