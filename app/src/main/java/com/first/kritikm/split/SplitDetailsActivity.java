package com.first.kritikm.split;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class SplitDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView eventName;
    private TextView eventLocation;
    private TextView eventDate;
    private TextView totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_details);

        getSupportActionBar().hide();

        recyclerView = (RecyclerView)findViewById(R.id.split_details_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        eventName = (TextView)findViewById(R.id.event_name_details);
        eventLocation = (TextView)findViewById(R.id.event_location_details);
        eventDate = (TextView)findViewById(R.id.event_date_details);
        totalAmount = (TextView)findViewById(R.id.total_amount_details);

        eventName.setText(getIntent().getStringExtra("EVENT_NAME"));
        eventLocation.setText(getIntent().getStringExtra("EVENT_LOCATION"));
        eventDate.setText(getIntent().getStringExtra("EVENT_DATE"));
        totalAmount.setText(getIntent().getStringExtra("CURRENCY"));
        totalAmount.append(String.valueOf(getIntent().getDoubleExtra("TOTAL_AMOUNT", 0)));

        int eventId = getIntent().getIntExtra("EVENT_ID", 0);
        String currency = getIntent().getStringExtra("CURRENCY");

        if(eventId != 0) {
            adapter = new EaterAdapter(getBaseContext(), eventId, currency);
            recyclerView.setAdapter(adapter);
        }

    }
}
