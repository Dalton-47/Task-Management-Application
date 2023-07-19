
package com.example.luna;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.Arrays;

public class CreateEventTask extends AsyncTask<Void, Void, Void> {
    Calendar mService;
    Activity myAPI;

    CreateEventTask(Calendar mService, APIMainActivity apiMainActivity) {
        this.mService = mService;
        this.myAPI=apiMainActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        addCalendarEvent();

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);


        // Task completed successfully
        // You can perform any necessary actions here, such as displaying a success message or updating the UI
    }


    public void addCalendarEvent() {
        Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        DateTime startDateTime = new DateTime("2023-07-21T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2023-07-22T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail("daltonleyian@gmail.com"),
             //   new EventAttendee().setEmail("sbrin@example.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        try {
           mService.events().insert(calendarId, event).execute();
         //   Toast.makeText(myAPI, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Log the response to get more information about the error
            System.err.println("Error Response: " + e.getMessage());
        }

    }
}