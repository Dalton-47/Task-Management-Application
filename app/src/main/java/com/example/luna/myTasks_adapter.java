package com.example.luna;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        TextView textViewTaskTitle,textViewTaskDescription,textViewTaskDueDate, textViewStartTime,textViewTaskEndTime,textViewTaskCategory;

        public DataViewHolder(@NonNull View itemView) {
              super(itemView);
            textViewTaskTitle  = (TextView)  itemView.findViewById(R.id.textViewTaskTitle_SEARCH);
            textViewTaskDescription  = (TextView)  itemView.findViewById(R.id.textViewTaskDescription_SEARCH);
            textViewStartTime = (TextView)  itemView.findViewById(R.id.textViewTaskStartTime_SEARCH);
            textViewTaskDueDate  = (TextView)  itemView.findViewById(R.id.textViewTaskDueDate_SEARCH);
            textViewTaskCategory  = (TextView)  itemView.findViewById(R.id.textViewTaskCategory_SEARCH);
            textViewTaskEndTime = (TextView)  itemView.findViewById(R.id.textViewTaskEndTime_SEARCH);


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
        holder.textViewStartTime.setText(myTaskObj.getStartTime());
        holder.textViewTaskDueDate.setText(myTaskObj.getDueDate());
        holder.textViewTaskCategory.setText(myTaskObj.getCategory());
        holder.textViewTaskEndTime.setText(myTaskObj.getEndTime());


    }

    @Override
    public int getItemCount() {
        return myTaskArrayList.size();
    }


}
