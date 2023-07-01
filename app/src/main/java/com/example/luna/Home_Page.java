package com.example.luna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;

public class Home_Page extends AppCompatActivity {
 View viewMyTasks;
 ConstraintLayout constraintLayoutTasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        constraintLayoutTasks =(ConstraintLayout)  this.findViewById(R.id.constraintLayoutTasks);
        constraintLayoutTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayoutTasks.setVisibility(View.GONE);
            }
        });
        viewMyTasks = (View)  this.findViewById(R.id.viewMyTasks);
        viewMyTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayoutTasks.setVisibility(View.VISIBLE);
            }
        });

    }
}