package com.example.luna;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class TaskReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "TaskReminderChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract the task details from the intent
        String taskTitle = intent.getStringExtra("taskTitle");

        // Create and show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Task Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle("Task Reminder")
                    .setContentText("Task \"" + taskTitle + "\" is due in 5 minutes.")
                    .setSmallIcon(R.drawable.baseline_bookmark_24)
                    .build();
        }

        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}

