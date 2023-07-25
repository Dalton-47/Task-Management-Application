package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Search_Task extends AppCompatActivity {

    RecyclerView searchTaskRecyclerView;
    myTasks_adapter myTasks_adapter;
    ArrayList <Task_Class> myTaskArrayList = new ArrayList<Task_Class>();
    DatabaseReference myTasksRef ;

    Dialog datePickerDialog;
    String selectedDate;
    ConstraintLayout constraintLayoutSearchTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_task_activity);

        constraintLayoutSearchTasks = (ConstraintLayout)  this.findViewById(R.id.constraintLayoutSearchTasks);

        FirebaseAuth myAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=myAuth.getCurrentUser();
        String userId= currentUser.getUid();

        searchTaskRecyclerView = (RecyclerView)  this.findViewById(R.id.recyclerViewSearchTasks);
        myTasks_adapter = new myTasks_adapter(myTaskArrayList);
        searchTaskRecyclerView.setAdapter(myTasks_adapter);

        LinearLayoutManager myLayout=new LinearLayoutManager(this);
        searchTaskRecyclerView.setLayoutManager(myLayout);

        // Add item decoration to the RecyclerView
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Adjust the spacing size as per your requirement
        searchTaskRecyclerView.addItemDecoration(new RecyclerViewItemDecorationClass(this, spacingInPixels) );

        // Get the current date to set as the default selected date in the DatePicker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = this.findViewById(R.id.datePickerNew);
        datePicker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update the selectedDate whenever the user selects a new date
                String myMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                String myDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                selectedDate = year + "-" + myMonth + "-" + myDay;

                myTasksRef = FirebaseDatabase.getInstance().getReference("Tasks").child(userId).child(selectedDate);

                // Now you can use the updated selectedDate as needed
                // For example, you can call the function to fetch user tasks for the selected date:
                myTaskArrayList.clear();
                getUserTasks();
            }
        });





    }




    void getUserTasks()
    {
        myTasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    Task_Class  myTaskObj=childSnapshot.getValue(Task_Class.class);
                    assert myTaskObj != null;
                    if(Objects.equals(myTaskObj.getDueDate(), selectedDate))
                    {
                       //Toast.makeText(Search_Task.this, selectedDate, Toast.LENGTH_SHORT).show();
                        myTaskArrayList.add(myTaskObj);
                    }

                }


                if(myTaskArrayList.isEmpty())
                {
                    searchTaskRecyclerView.setVisibility(View.GONE);
                    myTaskArrayList.clear();
                    constraintLayoutSearchTasks.setVisibility(View.VISIBLE);
                  }
                else {
                    searchTaskRecyclerView.setVisibility(View.VISIBLE);
                    constraintLayoutSearchTasks.setVisibility(View.GONE);
                    myTasks_adapter.setData(myTaskArrayList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}