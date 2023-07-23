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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.DataViewHolder> {

    //define an interface
    public interface OnSaveButtonClickListener {
        void onSaveButtonClicked();
    }

    //member variable for the interface
    private OnSaveButtonClickListener onSaveButtonClickListener;

    ArrayList<Task_Class> myTasksArrayList=new ArrayList<>();
    Context myContext;
    TextView textViewTaskTitleDialog;
    Dialog myDialog;
    RadioGroup myRadioGroup;
    Button btnSaveTaskStatusDialog;
    String newStatus="";


    public Task_Adapter(view_tasks_activity view_tasks_activity, TextView textViewTaskTitleDialog, Dialog myTaskDialog, RadioGroup radioGroupOptions, Button btnSaveTaskStatusDialog, OnSaveButtonClickListener onSaveButtonClickListener) {
        myContext = view_tasks_activity;
       this.textViewTaskTitleDialog = textViewTaskTitleDialog;
       this.myDialog = myTaskDialog;
       this.myRadioGroup=radioGroupOptions;
       this.btnSaveTaskStatusDialog = btnSaveTaskStatusDialog;

       //initializing the interface
        this.onSaveButtonClickListener = onSaveButtonClickListener;

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

              //button to update task status
              btnSaveTaskStatusDialog.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                      FirebaseAuth mAuth = FirebaseAuth.getInstance();
                      FirebaseUser currentUser = mAuth.getCurrentUser();
                      String userId = currentUser.getUid();
                      DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("Categorised Tasks").child(userId).child(taskObj.getCategory()).child(taskObj.getDateTime());


                      if(newStatus!="")
                      {
                          //update the new status to database
                          taskRef.child("status").setValue(newStatus);
                          Toast.makeText(myContext, "Updated status successfully", Toast.LENGTH_SHORT).show();
                          holder.textViewTaskProgress.setText(newStatus);


                          // Notify the main activity about the save button click
                          if (onSaveButtonClickListener != null) {
                              onSaveButtonClickListener.onSaveButtonClicked();
                          }


                         // notifyDataSetChanged();
                          myDialog.dismiss();

                      }


                  }
              });

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
                        newStatus = "In-Progress";
                        Toast.makeText(myContext, "Status : In-Progress", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonOnHold:
                         newStatus="On-Hold";
                        Toast.makeText(myContext, "Status : On-Hold", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonCompleted:
                        newStatus="Completed";
                        Toast.makeText(myContext, "Status : Completed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonOverdue:
                         newStatus="OverDue";
                        Toast.makeText(myContext, "Status : OverDue", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonDeferred:
                         newStatus="Deferred";
                        Toast.makeText(myContext, "Status : Deferred", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonCancelled:
                         newStatus="Cancelled";
                        Toast.makeText(myContext, "Status : Cancelled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //button to update task status
        btnSaveTaskStatusDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return myTasksArrayList.size();
    }
}
