package com.example.luna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home_Page extends AppCompatActivity {
 View viewMyTasks,viewNewTask, viewSearchTask, viewMyTrack, viewTaskDialog, viewEventDialog;
 View viewWorkCategory,viewFitnessCategory,viewFamilyCategory,viewPersonalCategory,viewFinanceCategory,viewSharedTasksCategory;

 ConstraintLayout constraintLayoutTasks;

    private Dialog categoryDialog;
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

        //view My Event_Class
        viewMyTasks = (View)  this.findViewById(R.id.viewMyTasks);
        viewMyTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Home_Page.this, Create_Event.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        //create new Task
        viewNewTask = (View)  this.findViewById(R.id.viewNewTask);
        viewNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showEventTaskDialog();
            }
        });

        //search for a task
        viewSearchTask = (View)  this.findViewById(R.id.viewSearchTask);
        viewSearchTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Home_Page.this,Search_Task.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        //check my track
        viewMyTrack = (View)  this.findViewById(R.id.viewMyTrack);
        viewMyTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(Home_Page.this,APIMainActivity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });



    }

    private void showEventTaskDialog()
    {
        // Initialize the dialog
        categoryDialog = new Dialog(this);
        categoryDialog.setContentView(R.layout.event_task_dialog_layout);


        // Initialize the MapView
      //  mapView = mapDialog.findViewById(R.id.mapViewMain);

        viewTaskDialog = categoryDialog.findViewById(R.id.viewTaskDialog);
        viewEventDialog = categoryDialog.findViewById(R.id.viewEventDialog);

        viewEventDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Home_Page.this, Create_Event.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                //close the dialog
                categoryDialog.dismiss();
            }
        });

        viewTaskDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Home_Page.this, Create_Task.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                //close the dialog
                categoryDialog.dismiss();
            }
        });



        // Show the dialog
        categoryDialog.show();

    }
}