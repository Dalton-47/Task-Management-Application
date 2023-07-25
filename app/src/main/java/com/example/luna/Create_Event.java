package com.example.luna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Create_Event extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private MapView mapView;
    private Button showMapButton;
    private Dialog mapDialog;
    String eventLocationName ="";

    com.google.api.services.calendar.Calendar mService;
    EditText editTextTaskTitle, editTextTaskDescription;
    Spinner spinnerTaskCategory;
    TextView textViewDate, textViewStartTime, textViewEndTime, textViewLocation;
    Button buttonSaveTask;
    FirebaseAuth userAuth;
    String userId;
    DatabaseReference eventReference;
    private String[] categoryArray = {
            "Select Category",
            "Work",
            "Fitness",
            "School",
            "Finance",
            "Personal",

    };

    ProgressBar progressBar;

    private String eventTitle = "", eventDescription = "", eventDueDate = "", eventStartTime = "", eventEndTime = "", eventCategory = "";

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize the dialog
        mapDialog = new Dialog(this);
        mapDialog.setContentView(R.layout.map_view_layout);






        textViewLocation = (TextView) this.findViewById(R.id.textViewEventLocation);
        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for location permissions on devices running Android 6.0 and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermissions();
                } else {
                    Toast.makeText(Create_Event.this, "Cannot Get Location!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editTextTaskTitle = (EditText) this.findViewById(R.id.editTextCreateEventTitle);
        editTextTaskDescription = (EditText) this.findViewById(R.id.editTextCreateEventDescription);

        progressBar = (ProgressBar) this.findViewById(R.id.progressBarSaveTask);

        userAuth = FirebaseAuth.getInstance();
        FirebaseUser user = userAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();

        eventReference = FirebaseDatabase.getInstance().getReference("Events");

        textViewDate = (TextView) this.findViewById(R.id.textViewCreateTaskDate);
        textViewStartTime = (TextView) this.findViewById(R.id.textViewCreateTaskStartTime);
        textViewEndTime = (TextView) this.findViewById(R.id.textViewCreateEventEndTime);

        buttonSaveTask = (Button) this.findViewById(R.id.buttonCreateEventSave);
        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call method to save new task
                saveTask();
            }
        });

        spinnerTaskCategory = (Spinner) this.findViewById(R.id.spinnerCreateEventCategory);

        // Assuming you have already initialized your Spinner and categoryArray
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryArray);

// Set the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

  // Set the adapter to the Spinner
        spinnerTaskCategory.setAdapter(arrayAdapter);

        spinnerTaskCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myText = spinnerTaskCategory.getSelectedItem().toString().trim();
                if (myText == "Select Category".trim()) {
                    //Nothing
                } else {
                    Toast.makeText(Create_Event.this, "You have Selected " + myText, Toast.LENGTH_SHORT).show();
                    eventCategory = myText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //setting date
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                datePickerDialog = new DatePickerDialog(Create_Event.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        String myMonth;
                        if(month<10)
                        {
                            myMonth="0"+Integer.toString(month + 1);
                        }
                        else {
                            myMonth=Integer.toString(month + 1);
                        }

                        String myDay;
                        if(dayOfMonth<10)
                        {
                            myDay="0"+Integer.toString(dayOfMonth);
                        }
                        else {
                            myDay=Integer.toString(dayOfMonth);
                        }

                        textViewDate.setText(year +"-"+ myMonth +"-"+ myDay);

                        eventDueDate = year +"-"+ myMonth +"-"+ myDay;
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        //setting start time
        textViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Calendar instance to get the current time
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog and set the initial time
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Create_Event.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Handle the selected time (hourOfDay and minute)
                                // This method will be called when the user sets the time
                                // You can perform any action with the selected time here
                                String myHour, myMinute;
                                //if minutes are less than 10
                                if (minute < 10) {
                                    myMinute = "0" + Integer.toString(minute);
                                } else {
                                    myMinute = Integer.toString(minute);
                                }

                                if (hourOfDay < 10) {
                                    myHour = "0" + Integer.toString(hourOfDay);
                                } else {
                                    myHour = Integer.toString(hourOfDay);
                                }

                                textViewStartTime.setText(myHour + ":" + myMinute + " hours");
                                eventStartTime = myHour + ":" + myMinute;

                            }
                        },
                        currentHour,
                        currentMinute,
                        true // true if you want to use the 24-hour format, false for 12-hour format
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        //end time
        textViewEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Calendar instance to get the current time
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog and set the initial time
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Create_Event.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Handle the selected time (hourOfDay and minute)
                                // This method will be called when the user sets the time
                                // You can perform any action with the selected time here
                                String myHour, myMinute;
                                //if minutes are less than 10
                                if (minute < 10) {
                                    myMinute = "0" + Integer.toString(minute);
                                } else {
                                    myMinute = Integer.toString(minute);
                                }

                                if (hourOfDay < 10) {
                                    myHour = "0" + Integer.toString(hourOfDay);
                                } else {
                                    myHour = Integer.toString(hourOfDay);
                                }

                                textViewEndTime.setText(myHour + ":" + myMinute + " hours");
                                eventEndTime = myHour + ":" + myMinute;

                            }
                        },
                        currentHour,
                        currentMinute,
                        true // true if you want to use the 24-hour format, false for 12-hour format
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });


    }

    // Check if location permissions are granted, and request them if not
    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted, request the permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions already granted, proceed with your location-based logic
            // Your code to work with location goes here
            showMapDialog();
        }
    }


    // Method to show the map dialog
    private void showMapDialog() {
        // Initialize the dialog
        mapDialog = new Dialog(this);
        mapDialog.setContentView(R.layout.map_view_layout);


        // Initialize the MapView
        mapView = mapDialog.findViewById(R.id.mapViewMain);
        mapView.onCreate(null);
        mapView.getMapAsync(this);


        // Show the dialog
        mapDialog.show();

        // Force the map to refresh (this will trigger onResume)
        if (mapView != null) {
            mapView.onResume();
        }

    }

    //start to handle mapview lifecycle activities
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
        if (mapDialog != null) {
            mapDialog.dismiss();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    //end
    private void saveTask() {
        eventTitle = editTextTaskTitle.getText().toString().trim();
        eventDescription = editTextTaskDescription.getText().toString().trim();


        if (eventTitle.isEmpty()) {
            editTextTaskTitle.setError("Cannot be blank!");
            editTextTaskTitle.requestFocus();
        } else if (eventDescription.isEmpty()) {
            editTextTaskDescription.setError("Cannot be blank!");
            editTextTaskDescription.requestFocus();
        } else if (eventStartTime.isEmpty()) {
            textViewStartTime.setError("Cannot be blank!");
            textViewStartTime.requestFocus();
        } else if (eventEndTime.isEmpty()) {
            textViewEndTime.setError("Cannot be blank");
        } else if (eventDueDate.isEmpty()) {
            textViewDate.setError("Cannot be blank");
        } else if (eventCategory.isEmpty()) {
            Toast.makeText(this, "Choose Task Category", Toast.LENGTH_SHORT).show();
        } else if (eventLocationName.isEmpty()) {
            textViewLocation.setError("Cannot be blank");
        } else {

            progressBar.setVisibility(View.VISIBLE);
            Event_Class newEvent = new Event_Class(eventTitle, eventDescription, eventDueDate, eventStartTime, eventEndTime, eventCategory, eventLocationName);
            eventReference.child(userId).child(eventDueDate).child(eventTitle).setValue(newEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //event data will be saved successfully
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(Create_Event.this, "Event Saved Successfully", Toast.LENGTH_SHORT).show();

                        Intent myIntent = new Intent(Create_Event.this, APIMainActivity.class);
                        myIntent.putExtra("data_object", newEvent);
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();


                    } else {
                        // Failed to save user data
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Create_Event.this, "Failed to save new event!", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }


    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Handle the click event and get the latitude and longitude of the clicked location
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        // Get the name of the location using reverse geocoding (optional)
         eventLocationName = getLocationNameFromLatLng(latLng).toString();
        textViewLocation.setText(eventLocationName);
        // Do something with the latitude, longitude, and locationName, such as displaying in a TextView or creating a marker
        // Example:
        Toast.makeText(this, "Clicked at: " + latitude + ", " + longitude + ", " + eventLocationName, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            mMap.setMyLocationEnabled(true);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // Set a click listener for the map
        mMap.setOnMapClickListener(this);
    }

    // Helper method to get the name of the location from latitude and longitude using reverse geocoding
    private String getLocationNameFromLatLng(LatLng latLng) {
        // You can use the Geocoder class to get the location name from latitude and longitude
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getAddressLine(0); // Use a specific address line if you need a more concise name
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown location";
    }
}