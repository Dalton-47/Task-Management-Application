package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home_Page extends AppCompatActivity {
 View viewMyTasks,viewNewTask, viewSearchTask, viewMyTrack, viewTaskDialog, viewEventDialog,viewUserProfile;
 View viewWorkCategory,viewFitnessCategory, viewSchoolCategory,viewPersonalCategory,viewFinanceCategory,viewSharedTasksCategory;

 ConstraintLayout constraintLayoutTasks;

 FirebaseAuth firebaseAuth;
 FirebaseUser firebaseUser;
 DatabaseReference userReference;

 ImageView imageViewUserProfileHome;

 TextView textViewUserWelcomeText;

    private Dialog categoryDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //get database reference to read users details
        userReference= FirebaseDatabase.getInstance().getReference("users");

        imageViewUserProfileHome = (ImageView)  this.findViewById(R.id.imageViewUserProfileHome);

        textViewUserWelcomeText =(TextView)  this.findViewById(R.id.textViewUserWelcomeText);

        //set username on dashboard
        readUserName();

        //setting profile picture
        if(firebaseUser!=null)
        {
            // Set User DP (After user has uploaded)
            Uri uri = firebaseUser.getPhotoUrl();

            if(uri!=null)
            {
                // ImageViewer setImageURI() should not be used with regular URIs. So we are using Picasso
              //  view60.setBackground(getResources().getDrawable(R.drawable.white_background_circle));
                Picasso.get().load(uri)
                        .transform(new SquareTransformation() )
                        .into(imageViewUserProfileHome);
            }
            else
            {
               // view60.setBackground(getResources().getDrawable(R.drawable.white_background_circle));

                imageViewUserProfileHome.setBackground(getResources().getDrawable(R.drawable.error_person));

            }


        }
        else
        {
           // view60.setBackground(getResources().getDrawable(R.drawable.white_background_circle));
            imageViewUserProfileHome.setBackground(getResources().getDrawable(R.drawable.error_person));


        }


        viewUserProfile = (View) this.findViewById(R.id.viewHomeUserProfile);
        viewUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Home_Page.this,User_Profile_Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.new_slide_in, R.anim.new_slide_out);
            }
        });

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
                constraintLayoutTasks.setVisibility(View.VISIBLE);
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


        //Finance View Tasks view
        viewFinanceCategory = (View) this.findViewById(R.id.viewFinanceCategory);
        viewFinanceCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(Home_Page.this,view_tasks_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category","Finance");
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                constraintLayoutTasks.setVisibility(View.GONE);
            }
        });

        //Work View Tasks view
        viewWorkCategory = (View) this.findViewById(R.id.viewWorkCategory);
        viewWorkCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(Home_Page.this,view_tasks_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category","Work");
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                constraintLayoutTasks.setVisibility(View.GONE);
            }
        });

        //Fitness View Tasks view
        viewFitnessCategory = (View) this.findViewById(R.id.viewFitnessCategory);
        viewFitnessCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(Home_Page.this,view_tasks_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category","Fitness");
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                constraintLayoutTasks.setVisibility(View.GONE);
            }
        });

        //School View Tasks view
        viewSchoolCategory = (View) this.findViewById(R.id.viewSchoolCategory);
        viewSchoolCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(Home_Page.this,view_tasks_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category","School");
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                constraintLayoutTasks.setVisibility(View.GONE);
            }
        });

        //Personal View Tasks view
        viewPersonalCategory = (View) this.findViewById(R.id.viewPersonalCategory);
        viewPersonalCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(Home_Page.this,view_tasks_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category","Personal");
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                constraintLayoutTasks.setVisibility(View.GONE);
            }
        });

        //Shared Tasks view
        viewSharedTasksCategory = (View) this.findViewById(R.id.viewSharedTaskCategory);
        viewSharedTasksCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // constraintLayoutTasks.setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(Home_Page.this,view_tasks_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("category","Shared");
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                constraintLayoutTasks.setVisibility(View.GONE);
            }
        });


    }

    void readUserName()
    {
        String userId = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        userReference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    DataSnapshot thisDataSnapshot=task.getResult();
                    String userName=String.valueOf(thisDataSnapshot.child("username").getValue());
                    // Toast.makeText(Patient_Main_Page_NEW.this," FIRSTNAME "+firstname ,Toast.LENGTH_SHORT).show();
                    if(userName.equals("null"))
                    {
                        textViewUserWelcomeText.setText("Kindly Register With Us.");
                    }
                    else
                    {
                        textViewUserWelcomeText.setText("Hi, "+userName+".");
                    }


                }
                else
                {
                    textViewUserWelcomeText.setText("Hello User Check Network!");
                }

            }
        });
    }

    private void showEventTaskDialog()
    {
        // Initialize the dialog
        categoryDialog = new Dialog(this);
        categoryDialog.setContentView(R.layout.event_task_dialog_layout);

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