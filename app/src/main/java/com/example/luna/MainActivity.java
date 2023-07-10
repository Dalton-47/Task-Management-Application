package com.example.luna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    ConstraintLayout constraintLayoutForgotEmail;
    TextView textViewForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin =   (Button)  this.findViewById(R.id.buttonLogin);
        constraintLayoutForgotEmail = (ConstraintLayout)  this.findViewById(R.id.constraintlayoutForgotEmail);
        textViewForgotPassword = (TextView) this.findViewById(R.id.textViewForgotPaswword);

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
              Intent myIntent=new Intent(MainActivity.this, Home_Page.class);
              startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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


}