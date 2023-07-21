package com.example.luna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class view_tasks_activity extends AppCompatActivity {
 task_adapter taskAdapter;
 String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        Intent intent =getIntent();
        category=getIntent().getStringExtra("category");

        RecyclerView taskRecyclerView=(RecyclerView)  this.findViewById(R.id.recyclerViewTasks);

        taskAdapter = new task_adapter();
        taskRecyclerView.setAdapter(taskAdapter);

        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        taskRecyclerView.setLayoutManager(linearLayout);


    }
}