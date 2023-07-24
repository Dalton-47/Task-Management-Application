package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class view_tasks_activity extends AppCompatActivity implements Task_Adapter.OnSaveButtonClickListener,Task_Adapter.interface_adapter{

    //initialize interface with method to redirect user to home page
    @Override
    public  void onSaveInterface() {
        // Implement the refresh logic here
        exitActivity();
    }

    private void exitActivity()
    {
        Intent myIntent = new Intent(view_tasks_activity.this,Home_Page.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onSaveButtonClicked() {
        // Implement the refresh logic here
        refreshActivity();
    }

    private void refreshActivity() {
        // Implement your refresh logic here, e.g., updating the data and notifying the adapter
        tasksAppointmentsList.clear();
        taskAdapter.notifyDataSetChanged();

    }


 Task_Adapter taskAdapter;
 String category;
 ArrayList<Task_Class> tasksAppointmentsList =new ArrayList<Task_Class>();
TextView textViewTaskHeading,textViewTaskTitleDialog,textViewTaskNotFound;
ConstraintLayout constraintLayoutTaskNotFound;
 DatabaseReference categoryRef;
    RadioGroup radioGroupOptions;

    Dialog taskStatusDialog,myTaskDialog;
    Button btnSaveTaskStatusDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        // Initialize the dialog
        myTaskDialog = new Dialog(this);
        myTaskDialog.setContentView(R.layout.dialog_task_status_layout);
        textViewTaskTitleDialog = (TextView)  myTaskDialog.findViewById(R.id.textViewDialogTaskTitle);
        radioGroupOptions = (RadioGroup) myTaskDialog.findViewById(R.id.radioGroupOptions);
        btnSaveTaskStatusDialog =(Button) myTaskDialog.findViewById(R.id.buttonDialogSaveStatus);


        textViewTaskNotFound = (TextView)  this.findViewById(R.id.textViewTaskNotFound);
        constraintLayoutTaskNotFound = (ConstraintLayout)  this.findViewById(R.id.constraintLayouttasksNotFound);

        Intent intent =getIntent();
        category=getIntent().getStringExtra("category");

        textViewTaskHeading = (TextView) this.findViewById(R.id.textViewViewTasks);
        textViewTaskHeading.setText("My "+category+" Tasks");

        RecyclerView taskRecyclerView=(RecyclerView)  this.findViewById(R.id.recyclerViewTasks);

        //Initializing the Task_Adapter with parameters that will be passed to the class
        taskAdapter = new Task_Adapter(this,textViewTaskTitleDialog,myTaskDialog,radioGroupOptions,btnSaveTaskStatusDialog,this,this);
        taskRecyclerView.setAdapter(taskAdapter);

        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        taskRecyclerView.setLayoutManager(linearLayout);

        // Add item decoration to the RecyclerView
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Adjust the spacing size as per your requirement
        taskRecyclerView.addItemDecoration(new RecyclerViewItemDecorationClass(this, spacingInPixels) );


       // Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        //call method to get Tasks
        getTaskData();

    }


    private void showEventTaskDialog()
    {
        // Initialize the dialog
        taskStatusDialog = new Dialog(this);
        taskStatusDialog.setContentView(R.layout.dialog_task_status_layout);




        // Show the dialog
        taskStatusDialog.show();

    }

    private void getTaskData()
    {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        String email = currentUser.getEmail();


        categoryRef = FirebaseDatabase.getInstance().getReference("Categorised Tasks").child(userId).child(category);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    Task_Class myTasks=childSnapshot.getValue(Task_Class.class);
                    tasksAppointmentsList.add(myTasks);

                //    Toast.makeText(view_tasks_activity.this, tasksAppointmentsList.get(0).toString(), Toast.LENGTH_SHORT).show();
                }
                taskAdapter.setData(tasksAppointmentsList);

                //if arraylist has no tasks
                if(tasksAppointmentsList.isEmpty())
                {

                    constraintLayoutTaskNotFound.setVisibility(View.VISIBLE);
                    textViewTaskNotFound.setText("No "+category+" tasks found!");
                 //   relativeLayoutNoAppointments.setVisibility(View.VISIBLE);
                    //Toast.makeText(Doctor_View_Appointments_NEW.this, "No New Appointments Today", Toast.LENGTH_SHORT).show();
                }
                else
                {
                //    constraintLayoutTaskNotFound.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}