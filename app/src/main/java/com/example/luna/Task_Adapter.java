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

import java.util.ArrayList;

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.DataViewHolder> {

    ArrayList<Task_Class> myTasksArrayList=new ArrayList<>();
    Context myContext;
    TextView textViewTaskTitleDialog;
    Dialog myDialog;
    RadioGroup myRadioGroup;

    public Task_Adapter(view_tasks_activity view_tasks_activity, TextView textViewTaskTitleDialog, Dialog myTaskDialog, RadioGroup radioGroupOptions) {
        myContext = view_tasks_activity;
       this.textViewTaskTitleDialog = textViewTaskTitleDialog;
       this.myDialog = myTaskDialog;
       this.myRadioGroup=radioGroupOptions;
    }

    public void setData(ArrayList<Task_Class> tasksAppointmentsList) {
        this.myTasksArrayList = tasksAppointmentsList;
        notifyDataSetChanged();

    }



    public static class DataViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTaskProgress, textViewTaskTitle, textViewTaskDescription, textViewTaskDueTime, textViewTaskDueDate;
        Button btnUpdateTask;



        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTaskProgress = (TextView)  itemView.findViewById(R.id.textViewTaskStatus_REC);
            textViewTaskTitle =(TextView)  itemView.findViewById(R.id.textViewTaskTitle_REC);
            textViewTaskDescription = (TextView)  itemView.findViewById(R.id.textViewTaskDescription_REC);
            textViewTaskDueDate = (TextView)  itemView.findViewById(R.id.textViewTaskDueDate_REC);
            textViewTaskDueTime = (TextView)  itemView.findViewById(R.id.textViewTaskDueTime_REC);




            btnUpdateTask = (Button)  itemView.findViewById(R.id.buttonUpdateStatus_REC);
        }
    }
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tasks_layout,parent, false);
        return new Task_Adapter.DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        Task_Class taskObj = myTasksArrayList.get(position);

        holder.textViewTaskProgress.setText(taskObj.getStatus());
        holder.textViewTaskTitle.setText(taskObj.getTitle());
        holder.textViewTaskDescription.setText(taskObj.getDescription());
        holder.textViewTaskDueDate.setText(taskObj.getDueDate());
        holder.textViewTaskDueTime.setText(taskObj.getDueTime());




      holder.btnUpdateTask.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //set Task Title to the Dialog
              textViewTaskTitleDialog.setText("Task Title : "+taskObj.getTitle());
              //call method to display the dialog
              showEventTaskDialog();
          }
      });

    }

    private void showEventTaskDialog()
    {

        //set Task Title to the Dialog
       // textViewTaskTitleDialog.setText("taskObj.getTitle()");
        // Show the dialog
        myDialog.show();

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                // Check which radio button was selected
                switch (checkedId) {
                    case R.id.radioButtonInProgress:
                        Toast.makeText(myContext, "Status : In-Progress", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonOnHold:
                        Toast.makeText(myContext, "Status : On-Hold", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonCompleted:
                        Toast.makeText(myContext, "Status : Completed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonOverdue:
                        Toast.makeText(myContext, "Status : OverDue", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonDeferred:
                        Toast.makeText(myContext, "Status : Deferred", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonCancelled:
                        Toast.makeText(myContext, "Status : Cancelled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return myTasksArrayList.size();
    }
}
