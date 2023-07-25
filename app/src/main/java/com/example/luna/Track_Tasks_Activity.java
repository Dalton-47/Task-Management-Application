package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Track_Tasks_Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userId;

    DatabaseReference taskReference,myTaskRef;
    int taskCounter=0, completed=0, deferred=0, overdue=0, cancelled=0, pendingTasks=0;
    TextView textViewTasks;
    PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_tasks);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        taskReference = FirebaseDatabase.getInstance().getReference("Created Tasks");
        myTaskRef = FirebaseDatabase.getInstance().getReference("Task Progress");
        textViewTasks = (TextView)  this.findViewById(R.id.textViewTasksCreated);

         pieChart = findViewById(R.id.pieChart);
       // setupChart(pieChart);
       // updateChartData(pieChart);

        checkUserTasks();



    }

    private void setupChart(PieChart chart) {
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setTransparentCircleRadius(61f);
        chart.getDescription().setEnabled(false);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(14f);

        Legend legend = chart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // Set legend orientation to vertical
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // Align the legend to the top
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // Align the legend to the right
        legend.setDrawInside(false); // Ensure the legend does not overlap the chart
        legend.setWordWrapEnabled(true); // Enable word wrapping to display legend entries in a list-like manner
        legend.setTextSize(15f); // Set the legend text size
        legend.setTextColor(Color.BLACK); // Set the legend text color


    }

    private void updateChartData(PieChart chart) {
        List<PieEntry> entries = new ArrayList<>();
       // entries.add(new PieEntry(taskCounter, "Tasks Created"));


        if(completed > 0 )
        {
            entries.add(new PieEntry(completed, "Tasks Completed"));
        }
        if(cancelled > 0)
        {
            entries.add(new PieEntry(cancelled, "Tasks Cancelled"));
        }
        if(deferred > 0)
        {
            entries.add(new PieEntry(deferred, "Tasks Deferred"));
        }
        if(overdue > 0)
        {
            entries.add(new PieEntry(overdue, "Tasks OverDue"));
        }
        if(pendingTasks > 0)
        {
            entries.add(new PieEntry(pendingTasks, "Tasks Pending"));
        }



        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED, Color.parseColor("#800080"));

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true); //set to true if you want numbers and values to be displayed on the pie chart
        pieData.setValueTextSize(18f);
      //  pieData.setValueTextColor(Color.WHITE);

        chart.setData(pieData);
        chart.invalidate();
    }

    void checkUserTasks()
    {

        taskReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    taskCounter++;
                }
                String tasks=Integer.toString(taskCounter);
                textViewTasks.setText("Total Tasks Created = "+taskCounter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        myTaskRef.child(userId).child("Deferred").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    deferred++;
                }
                  //check completed tasks
                myTaskRef.child(userId).child("Completed").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            completed++;
                        }
                        //check overdue tasks
                        myTaskRef.child(userId).child("OverDue").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    overdue++;
                                }
                                //check cancelled tasks
                                myTaskRef.child(userId).child("Cancelled").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                        {
                                            cancelled++;
                                        }

                                        //check pending tasks
                                        pendingTasks = taskCounter - (completed + overdue + deferred + cancelled);
                                        // Update the chart only when all data collection is complete
                                        if (completed + overdue + deferred + cancelled + pendingTasks  == taskCounter) {
                                            setupChart(pieChart);
                                            updateChartData(pieChart);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        pendingTasks = taskCounter - (completed + overdue + deferred + cancelled);






    }


}