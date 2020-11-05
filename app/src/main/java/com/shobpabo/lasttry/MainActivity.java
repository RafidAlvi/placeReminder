package com.shobpabo.lasttry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
   private TextView tvTime;
    private TextView tvDate;
    Calendar c,c24;
    Button button,cnclbutton,buttonSET;
    EditText edAdrs1,ltlng1,locallity1,notes1,searchED;
    String edAdrs1S,ltlng1S,locallity1S,notes1S,timeBTS;

    int counter1 = 0;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences1 = getSharedPreferences("reminder1", Context.MODE_PRIVATE);
        if (sharedPreferences1.contains("address1")&&sharedPreferences1.contains("local1")&&sharedPreferences1.contains("counter1")){

            String adrs1 = sharedPreferences1.getString("address1","");
            String notes = sharedPreferences1.getString("notes1","");
            String latlng = sharedPreferences1.getString("latlang1","");
            String locals = sharedPreferences1.getString("local1","");
            String timedate = sharedPreferences1.getString("timer1","");
            int count = sharedPreferences1.getInt("counter1", 0);

            edAdrs1.setText(adrs1);
            notes1.setText(notes);
            ltlng1.setText(latlng);
            locallity1.setText(locals);
            button.setText(timedate);

            if (count==1){
                buttonSET.setVisibility(View.GONE);
                cnclbutton.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                edAdrs1.setEnabled(false);
                notes1.setEnabled(false);
                ltlng1.setEnabled(false);
                locallity1.setEnabled(false);
            }


        }
        else {
            Toast.makeText(MainActivity.this, "No previous data",
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSET = findViewById(R.id.buttonSETreminder);
        cnclbutton = findViewById(R.id.buttonCancelTdate);
        button = findViewById(R.id.buttonSETtime);
        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);

        searchED = findViewById(R.id.addressEDnotwork);

        edAdrs1 = findViewById(R.id.addressED1);
        ltlng1 = findViewById(R.id.latlng1);
        locallity1 = findViewById(R.id.locality1);
        notes1 = findViewById(R.id.notesED1);

        Places.initialize(getApplicationContext(),"Add your API key here and enable Geolocating and Geocoding ");


        searchED.setFocusable(false);
        searchED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);


                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(MainActivity.this);

                startActivityForResult(intent,100);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                c24 = Calendar.getInstance();


                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"Time picker");
                DialogFragment Datepick = new DatePickerFragment();
                Datepick.show(getSupportFragmentManager(),"Date picker");
            }
        });
        buttonSET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edAdrs1S = edAdrs1.getText().toString();
                locallity1S = edAdrs1.getText().toString();
                ltlng1S = edAdrs1.getText().toString();
                timeBTS = button.getText().toString();


                if (edAdrs1S.length()>0 || locallity1S.length()>0 || ltlng1S.length()>0 ) {



                    Toast.makeText(MainActivity.this, "Reminder Set for: "+timeBTS + "\n Also will be reminded before 24hrs",
                            Toast.LENGTH_LONG).show();
                    // DialogFragment Datepick = new DatePickerFragment();
                    // Datepick.show(getSupportFragmentManager(),"Date picker");
                    startAlarm(c);
                    startAlarm(c24);
                    buttonSET.setVisibility(View.GONE);
                    cnclbutton.setVisibility(View.VISIBLE);
                    button.setEnabled(false);


                    edAdrs1S = edAdrs1.getText().toString();
                    notes1S = notes1.getText().toString();
                    locallity1S = edAdrs1.getText().toString();
                    ltlng1S = ltlng1.getText().toString();
                    timeBTS = button.getText().toString();


                    SharedPreferences sharedPreferences1 = getSharedPreferences("reminder1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("address1", edAdrs1S);
                    editor1.putString("notes1", notes1S);
                    editor1.putString("local1", locallity1S);
                    editor1.putString("latlang1", ltlng1S);
                    editor1.putString("timer1", timeBTS);
                    editor1.putInt("counter1", 1);
                    editor1.commit();
                    edAdrs1.setEnabled(false);
                    notes1.setEnabled(false);
                    ltlng1.setEnabled(false);
                    locallity1.setEnabled(false);




                } else {
                    Toast.makeText(MainActivity.this, "Enter All data please",
                            Toast.LENGTH_SHORT).show();

                }


            }
        });
        cnclbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Reminder Cancelled!",
                        Toast.LENGTH_SHORT).show();
                cancelAlarm();
                buttonSET.setVisibility(View.VISIBLE);
                cnclbutton.setVisibility(View.GONE);
                button.setEnabled(true);
                SharedPreferences sharedPreferences1 = getSharedPreferences("reminder1", Context.MODE_PRIVATE);
                sharedPreferences1.edit().clear().apply();
                edAdrs1.setText("");
                notes1.setText("");
                ltlng1.setText("");
                locallity1.setText("");
                button.setText("Set time and Date");
                edAdrs1.setEnabled(true);
                notes1.setEnabled(true);
                ltlng1.setEnabled(true);
                locallity1.setEnabled(true);

            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(R.id.tvTime);
        button.append(" "+DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
        String timeText = "";

         //c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        c24.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c24.set(Calendar.MINUTE, minute);
        c24.set(Calendar.SECOND, 0);
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        button.setText(timeText+timeBTS);
       // startAlarm(c);


    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
          alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);



    }
    private void startAlarm24(Calendar c24) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,2,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c24.getTimeInMillis(),pendingIntent);



    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);

        alarmManager.cancel(pendingIntent);



    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView textView = findViewById(R.id.tvDate);

        button.setText(dayOfMonth+"/"+month+"/"+ year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        c24.set(Calendar.MONTH,month);
        c24.set(Calendar.YEAR, year);
        c24.set(Calendar.DAY_OF_MONTH, dayOfMonth-1);
        month=month+1;
        timeBTS=" "+dayOfMonth+"/"+month+"/"+ year;




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){

            Place place = Autocomplete.getPlaceFromIntent(data);

            searchED.setText(place.getAddress());

            locallity1.setText(String.format("Localitty name : %s",place.getName()));

            ltlng1.setText(String.valueOf(place.getLatLng()));


        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR){

            Status status = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
