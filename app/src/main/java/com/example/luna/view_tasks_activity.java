package com.example.luna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class view_tasks_activity extends AppCompatActivity {
 task_adapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        RecyclerView taskRecyclerView=(RecyclerView)  this.findViewById(R.id.recyclerViewTasks);

        taskAdapter = new task_adapter();
        taskRecyclerView.setAdapter(taskAdapter);

        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        taskRecyclerView.setLayoutManager(linearLayout);


    }
}