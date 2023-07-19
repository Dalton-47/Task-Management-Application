package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Create_Task extends AppCompatActivity {

    com.google.api.services.calendar.Calendar mService;
    EditText editTextTaskTitle,editTextTaskDescription;
    Spinner spinnerTaskCategory;
    TextView textViewDate, textViewStartTime, textViewEndTime;
    Button buttonSaveTask;
    FirebaseAuth userAuth;
    String userId;
    DatabaseReference taskRef;
    public String[] categoryArray = {
            "Select Category",
            "Work",
            "Fitness",
            "Finance",
            "Personal",
            "Shared Tasks"

    };

    ProgressBar progressBar;

    public String taskTitle="",taskDescription="",taskDueDate="",taskStartTime="",taskEndTime="",taskCategory="";

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        editTextTaskTitle = (EditText)  this.findViewById(R.id.editTextCreateTaskTitle);
        editTextTaskDescription = (EditText)  this.findViewById(R.id.editTextCreateTaskDescription);

        progressBar = (ProgressBar)  this.findViewById(R.id.progressBarSaveTask);

        userAuth=FirebaseAuth.getInstance();
        FirebaseUser user = userAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();

        taskRef = FirebaseDatabase.getInstance().getReference("Tasks");

       textViewDate = (TextView)  this.findViewById(R.id.textViewCreateTaskDate);
       textViewStartTime  = (TextView)  this.findViewById(R.id.textViewCreateTaskStartTime);
       textViewEndTime  = (TextView)  this.findViewById(R.id.textViewCreateTaskEndTime);

       buttonSaveTask = (Button)  this.findViewById(R.id.buttonCreateTaskSave);
       buttonSaveTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //call method to save new task
                   saveTask();
           }
       });

        spinnerTaskCategory = (Spinner)  this.findViewById(R.id.spinnerCreateTaskCategory);

        //assigning categories to the spinner
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,categoryArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskCategory.setAdapter(arrayAdapter);
        spinnerTaskCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myText=spinnerTaskCategory.getSelectedItem().toString().trim();
                if (myText=="Select Category".trim())
                {
                    //Nothing
                }
                else
                {
                    Toast.makeText(Create_Task.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    taskCategory=myText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //setting date
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                datePickerDialog = new DatePickerDialog(Create_Task.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        textViewDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        taskDueDate=Integer.toString(dayOfMonth)+Integer.toString(month+1)+Integer.toString(year);

                    }
                },year, month, day);
                datePickerDialog.show();

            }
        });

        //setting start time
        textViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Calendar instance to get the current time
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog and set the initial time
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Create_Task.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Handle the selected time (hourOfDay and minute)
                                // This method will be called when the user sets the time
                                // You can perform any action with the selected time here
                                String myHour,myMinute;
                                //if minutes are less than 10
                                if(minute<10)
                                {
                                    myMinute = "0"+Integer.toString(minute);
                                }
                                else {
                                    myMinute=Integer.toString(minute);
                                }

                                if(hourOfDay<10)
                                {
                                    myHour="0"+Integer.toString(hourOfDay);
                                }
                                else
                                {
                                    myHour=Integer.toString(hourOfDay);
                                }

                                    textViewStartTime.setText(myHour+":"+myMinute+ " hours");
                                    taskStartTime=myHour+":"+myMinute;

                            }
                        },
                        currentHour,
                        currentMinute,
                        true // true if you want to use the 24-hour format, false for 12-hour format
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        //end time
        textViewEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Calendar instance to get the current time
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog and set the initial time
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Create_Task.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Handle the selected time (hourOfDay and minute)
                                // This method will be called when the user sets the time
                                // You can perform any action with the selected time here
                                String myHour,myMinute;
                                //if minutes are less than 10
                                if(minute<10)
                                {
                                    myMinute = "0"+Integer.toString(minute);
                                }
                                else {
                                    myMinute=Integer.toString(minute);
                                }

                                if(hourOfDay<10)
                                {
                                    myHour="0"+Integer.toString(hourOfDay);
                                }
                                else
                                {
                                    myHour=Integer.toString(hourOfDay);
                                }

                                textViewEndTime.setText(myHour+":"+myMinute+ " hours");
                                taskEndTime=myHour+":"+myMinute;

                            }
                        },
                        currentHour,
                        currentMinute,
                        true // true if you want to use the 24-hour format, false for 12-hour format
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });


    }

    private void saveTask()
    {
       taskTitle = editTextTaskTitle.getText().toString().trim();
       taskDescription = editTextTaskDescription.getText().toString().trim();


       if(taskTitle.isEmpty())
       {
           editTextTaskTitle.setError("Cannot be blank!");
       }
       else if(taskDescription.isEmpty())
       {
           editTextTaskDescription.setError("Cannot be blank!");
       }
       else if(taskStartTime.isEmpty())
       {
           textViewStartTime.setError("Cannot be blank!");
       }
       else if(taskEndTime.isEmpty())
       {
           textViewEndTime.setError("Cannot be blank");
       }
       else if(taskDueDate.isEmpty())
       {
           textViewDate.setError("Cannot be blank");
       }
       else if(taskCategory.isEmpty())
       {
           Toast.makeText(this, "Choose Task Category", Toast.LENGTH_SHORT).show();
       }
       else {

           progressBar.setVisibility(View.VISIBLE);
           Query query = taskRef.orderByChild("Date").equalTo(taskDueDate);
           query.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   /*
                   if(snapshot.exists())
                   {
                      //if the selected date has a pre existing scheduled task
                      // Toast.makeText(Create_Task.this, "The Date is already Booked", Toast.LENGTH_SHORT).show();
                      progressBar.setVisibility(View.GONE);
                       textViewDate.setError("Date is already Scheduled!");
                   }
                   else
                   {
                   }
                       */

                       //date was not saved with any task
                       Tasks newTask = new Tasks(taskTitle,taskDescription,taskDueDate,taskStartTime,taskEndTime,taskCategory);
                         taskRef.child(taskDueDate).child(taskTitle).setValue(newTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if(task.isSuccessful())
                                 {
                                     //task data will be saved successfully
                                     progressBar.setVisibility(View.GONE);
                                     Toast.makeText(Create_Task.this, "Event Saved Successfully", Toast.LENGTH_SHORT).show();


                                 }
                                 else {
                                     // Failed to save user data
                                     progressBar.setVisibility(View.GONE);
                                     Toast.makeText(Create_Task.this, "Failed to save new task!", Toast.LENGTH_SHORT).show();

                                 }
                             }
                         });


               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {
                   progressBar.setVisibility(View.GONE);
                   Toast.makeText(Create_Task.this, "Retry!", Toast.LENGTH_SHORT).show();

               }
           });

       }

    }

}