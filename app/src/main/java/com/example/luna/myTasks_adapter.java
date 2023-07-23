package com.example.luna;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class myTasks_adapter extends RecyclerView.Adapter<myTasks_adapter.DataViewHolder> {

    ArrayList <Task_Class> myTaskArrayList = new ArrayList<Task_Class>();
    public myTasks_adapter(ArrayList<Task_Class> myTaskArrayList) {
        this.myTaskArrayList = myTaskArrayList;
    }

    public void setData(ArrayList<Task_Class> myTaskArrayList) {
        this.myTaskArrayList=myTaskArrayList;
        notifyDataSetChanged();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTaskTitle,textViewTaskDescription,textViewTaskDueDate,textViewTaskDueTime,textViewTaskCategory;

        public DataViewHolder(@NonNull View itemView) {
              super(itemView);
            textViewTaskTitle  = (TextView)  itemView.findViewById(R.id.textViewTaskTitle_SEARCH);
            textViewTaskDescription  = (TextView)  itemView.findViewById(R.id.textViewTaskDescription_SEARCH);
            textViewTaskDueTime  = (TextView)  itemView.findViewById(R.id.textViewTaskDueTime_SEARCH);
            textViewTaskDueDate  = (TextView)  itemView.findViewById(R.id.textViewTaskDueDate_SEARCH);
            textViewTaskCategory  = (TextView)  itemView.findViewById(R.id.textViewTaskCategory_SEARCH);


        }
    }

    @NonNull
    @Override
    public myTasks_adapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_tasks_layout, parent,false);
        return new DataViewHolder(myView) ;

    }

    @Override
    public void onBindViewHolder(@NonNull myTasks_adapter.DataViewHolder holder, int position) {

        Task_Class myTaskObj = myTaskArrayList.get(position);

        holder.textViewTaskTitle.setText(myTaskObj.getTitle());
        holder.textViewTaskDescription.setText(myTaskObj.getDescription());
        holder.textViewTaskDueTime.setText(myTaskObj.getDueTime());
        holder.textViewTaskDueDate.setText(myTaskObj.getDueDate());
        holder.textViewTaskCategory.setText(myTaskObj.getCategory());


    }

    @Override
    public int getItemCount() {
        return myTaskArrayList.size();
    }


}
