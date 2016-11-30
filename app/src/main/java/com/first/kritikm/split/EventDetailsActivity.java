package com.first.kritikm.split;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class EventDetailsActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{

    private Button eventDate;
    public static final String SPLIT_EVENT = "EVENT_NAME";
    public static final String SPLIT_DATE = "EVENT_DATE";
    public static final String SPLIT_LOCATION = "EVENT_LOCATION";

    String eventDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getSupportActionBar().setTitle("Some Essentials First");

        eventDate = (Button) findViewById(R.id.event_date);
    }

    public void showDatePicker(View view)
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog eventDatePicker = DatePickerDialog.newInstance(
                EventDetailsActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        if(Build.VERSION.SDK_INT >= 23)
            eventDatePicker.setAccentColor(getColor(R.color.colorPrimary));
        else
            eventDatePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));

        eventDatePicker.setTitle("It happened on");
        eventDatePicker.show(getFragmentManager(), "Datepickerdialog");


    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        eventDateString = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        eventDate.setText(eventDateString);
    }

    public void getLocation(View view)
    {
        EditText locationText = (EditText)findViewById(R.id.location_text);
        Uri search = Uri.parse("geo:0,0?q=" + Uri.encode(locationText.getText().toString()));
        Intent locationIntent = new Intent(Intent.ACTION_VIEW, search);
        startActivity(locationIntent);
    }

    public void continueSplit(View view)
    {
        String eventName = ((EditText)findViewById(R.id.event_name)).getText().toString();
        String eventLocation = ((EditText)findViewById(R.id.location_text)).getText().toString();

        Intent intent = new Intent(this, BasicSplit.class);
        intent.putExtra(SPLIT_EVENT, eventName);
        intent.putExtra(SPLIT_DATE, eventDateString);
        intent.putExtra(SPLIT_LOCATION, eventLocation);

        startActivity(intent);
        finish();
    }
}
