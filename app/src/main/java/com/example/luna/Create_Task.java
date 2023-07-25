package com.example.luna;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Create_Task extends AppCompatActivity {

   Spinner spinnerTaskCategory;
   EditText  editTextTaskDescription, editTextTaskTitle;
   TextView textViewTaskDate, textViewStartTime, textViewEndTime;
  ProgressBar progressBar;
   DatabaseReference taskReference,createdTaskRef;
   Button btnSaveTask;
   private DatePickerDialog datePickerDialog;
   private TimePickerDialog timePickerDialog;

   private String taskTitle = "", taskDescription = "", taskDate = "", taskStartTime = "", taskEndTime="" , taskCategory = "";

   FirebaseAuth userAuth;

   private String[] categoryArray = {
           "Select Category",
           "Work",
           "Fitness",
           "Finance",
           "School",
           "Personal",
           "Shared Tasks"

   };
   String userId;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_create_task);

      userAuth = FirebaseAuth.getInstance();
      FirebaseUser user = userAuth.getCurrentUser();
      assert user != null;
      userId = user.getUid();

      taskReference = FirebaseDatabase.getInstance().getReference("Tasks");
      createdTaskRef = FirebaseDatabase.getInstance().getReference("Created Tasks");


      editTextTaskTitle = (EditText)  this.findViewById(R.id.editTextCreateTaskTitle);
      editTextTaskDescription = (EditText)  this.findViewById(R.id.editTextCreateTaskDescription);

      textViewTaskDate = (TextView)  this.findViewById(R.id.textViewCreateTaskDate);
      textViewStartTime = (TextView)  this.findViewById(R.id.textViewCreateTaskStartTime);
      textViewEndTime = (TextView)  this.findViewById(R.id.textViewCreateTaskEndTime);

      progressBar = (ProgressBar)  this.findViewById(R.id.progressBarSaveTaskNew);

      //setting time
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
                          String myHour, myMinute;
                          //if minutes are less than 10
                          if (minute < 10) {
                             myMinute = "0" + Integer.toString(minute);
                          } else {
                             myMinute = Integer.toString(minute);
                          }

                          if (hourOfDay < 10) {
                             myHour = "0" + Integer.toString(hourOfDay);
                          } else {
                             myHour = Integer.toString(hourOfDay);
                          }

                          //datetime format 2023-07-21T09:00:00-07:00
                          textViewStartTime.setText(myHour + ":" + myMinute + " hours");

                          taskStartTime = myHour + ":" + myMinute+":00";

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
                          String myHour, myMinute;
                          //if minutes are less than 10
                          if (minute < 10) {
                             myMinute = "0" + Integer.toString(minute);
                          } else {
                             myMinute = Integer.toString(minute);
                          }

                          if (hourOfDay < 10) {
                             myHour = "0" + Integer.toString(hourOfDay);
                          } else {
                             myHour = Integer.toString(hourOfDay);
                          }

                          //datetime format 2023-07-21T09:00:00-07:00
                          textViewEndTime.setText(myHour + ":" + myMinute + " hours");

                          taskEndTime = myHour + ":" + myMinute+":00";

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


      //setting Date
      textViewTaskDate.setOnClickListener(new View.OnClickListener() {
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


                  String myMonth;
                  if(month<10)
                  {
                     myMonth="0"+Integer.toString(month + 1);
                  }
                  else {
                     myMonth=Integer.toString(month + 1);
                  }

                  String myDay;
                  if(dayOfMonth<10)
                  {
                     myDay="0"+Integer.toString(dayOfMonth);
                  }
                  else {
                     myDay=Integer.toString(dayOfMonth);
                  }

                  //datetime format 2023-07-21T09:00:00-07:00
                  textViewTaskDate.setText(myDay + "-" + myMonth + "-" + year);
                  //taskDueDate = myDay + myMonth + Integer.toString(year);
                  taskDate = Integer.toString(year) +"-"+myMonth+"-"+myDay;


               }
            }, year, month, day);
            datePickerDialog.show();
         }
      });

      spinnerTaskCategory = (Spinner) this.findViewById(R.id.spinnerCreateTaskCategory);

      // Assuming you have already initialized your Spinner and categoryArray
      ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryArray);

      // Set the layout to use when the list of choices appears
      arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

      // Set the adapter to the Spinner
      spinnerTaskCategory.setAdapter(arrayAdapter);

      spinnerTaskCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String myText = spinnerTaskCategory.getSelectedItem().toString().trim();
            if (myText == "Select Category".trim()) {
               //Nothing
            } else {
               Toast.makeText(Create_Task.this, "You have Selected " + myText, Toast.LENGTH_SHORT).show();
               taskCategory = myText;
            }
         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

         }
      });

      btnSaveTask = (Button)  findViewById(R.id.buttonCreateTaskSave);
      btnSaveTask.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            saveTaskDetails();
         }
      });

   }

   private void saveTaskDetails()
   {
      FirebaseAuth mAuth = FirebaseAuth.getInstance();
      FirebaseUser currentUser = mAuth.getCurrentUser();
      String userId = currentUser.getUid();
      String email = currentUser.getEmail();

      taskTitle = editTextTaskTitle.getText().toString().trim();
      taskDescription = editTextTaskDescription.getText().toString().trim();


      if (taskTitle.isEmpty()) {
         editTextTaskTitle.setError("Cannot be blank!");
         editTextTaskTitle.requestFocus();
      } else if (taskDescription.isEmpty()) {
         editTextTaskDescription.setError("Cannot be blank!");
         editTextTaskDescription.requestFocus();
      } else if (taskStartTime.isEmpty()) {
         textViewStartTime.setError("Cannot be blank!");
         textViewStartTime.requestFocus();
      } else if(taskEndTime.isEmpty())
      {
         textViewEndTime.setError("Cannot be blank!");
         textViewEndTime.requestFocus();
      }
      else if (taskDate.isEmpty()) {
         textViewTaskDate.setError("Cannot be blank");
      } else if (taskCategory.isEmpty()) {
         Toast.makeText(this, "Choose Task Category", Toast.LENGTH_SHORT).show();
      } else {
         progressBar.setVisibility(View.VISIBLE);

         //let us set status of the task
         String status="New";
         String dateTimeString= taskDate +"T"+ taskStartTime;
         String dateTimeNew = dateTimeString +taskEndTime;
         //String title, String description, String startTime, String dueDate, String category, String status, String dateTime, String endTime) {

         //String title, String description, String startTimme, String dueDate, String category
         Task_Class taskObj = new Task_Class(taskTitle,taskDescription, taskStartTime,taskDate, taskCategory,status,dateTimeString,taskEndTime);
         //use dueDate+dueTime as the child Ref
         taskReference.child(userId).child(taskDate).child(dateTimeNew).setValue(taskObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful())
               {
                  //save to a node for getting all user tasks created
                    createdTaskRef.child(userId).child(dateTimeNew).setValue(taskObj);

                  //let us save the data to tasks that are categorized in the database
                  DatabaseReference categoryRef;
                  categoryRef = FirebaseDatabase.getInstance().getReference("Categorised Tasks");

                  categoryRef.child(userId).child(taskCategory).child(dateTimeNew).setValue(taskObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                           //let us schedule notifications for users

                           // Schedule the notification 5 minutes before the task's due time
                           //does not work for now...to be updated
                           String dateDueTimeString = taskDate + "T" + taskEndTime;
                           long dueTimeInMillis = getDueTimeInMillis(dateDueTimeString);
                           long reminderTimeInMillis = dueTimeInMillis - (5 * 60 * 1000);

                           if (reminderTimeInMillis > System.currentTimeMillis()) {
                              scheduleTaskReminder(reminderTimeInMillis, taskTitle);
                              Toast.makeText(Create_Task.this, "Task Saved Successfully", Toast.LENGTH_SHORT).show();
                           }
                           //task data will be saved successfully
                           progressBar.setVisibility(View.GONE);

                           Intent myIntent = new Intent(Create_Task.this, Home_Page.class);
                           startActivity(myIntent);
                           overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                           finish();
                        }
                     }
                  });



               }
               else
               {
                  // Failed to save user data
                  progressBar.setVisibility(View.GONE);
                  Toast.makeText(Create_Task.this, "Failed to save new task!", Toast.LENGTH_SHORT).show();

               }
            }
         });

      }
   }

   private void scheduleTaskReminder(long reminderTimeInMillis, String taskTitle) {
      Intent intent = new Intent(this, TaskReminderReceiver.class);
      intent.putExtra("taskTitle", taskTitle);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(
              this,
              0,
              intent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE flag
      );

      AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
      alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent);
   }


   private long getDueTimeInMillis(String dateTimeString) {
      // Define the date-time format to match your input string
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

      try {
         // Parse the date-time string to a Date object
         Date dueDate = sdf.parse(dateTimeString);

         // Get the time in milliseconds from the Date object
        // Toast.makeText(this, , Toast.LENGTH_SHORT).show();
         assert dueDate != null;
         return dueDate.getTime();
      } catch (ParseException e) {
         e.printStackTrace();
         return 0; // Return 0 if there's an error in parsing the date-time string
      }
   }


}