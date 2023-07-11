package com.example.luna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home_Page extends AppCompatActivity {
 View viewMyTasks,viewNewTask, viewSearchTask, viewMyTrack;
 View viewWorkCategory,viewFitnessCategory,viewFamilyCategory,viewPersonalCategory,viewFinanceCategory,viewSharedTasksCategory;

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

        //view My Tasks
        viewMyTasks = (View)  this.findViewById(R.id.viewMyTasks);
        viewMyTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Home_Page.this,Create_Task.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        //create new Task
        viewNewTask = (View)  this.findViewById(R.id.viewNewTask);
        viewNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent myIntent = new Intent(Home_Page.this,Create_Task.class);
               startActivity(myIntent);
               overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        //search for a task
        viewSearchTask = (View)  this.findViewById(R.id.viewSearchTask);
        viewSearchTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
            }
        });

        //check my track
        viewMyTrack = (View)  this.findViewById(R.id.viewMyTrack);
        viewMyTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
            }
        });



    }
}