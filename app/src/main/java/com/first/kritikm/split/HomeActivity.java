package com.first.kritikm.split;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.getbase.floatingactionbutton.*;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    UserDataContract.UserEntry db;
    String name;
    Cursor getter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView)findViewById(R.id.split_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db = new UserDataContract.UserEntry(this);

        String username = getIntent().getStringExtra(MainActivity.extra_message);

        if(username == null || username.length() == 0)
            username = SaveSharedPreference.getUsername(getBaseContext());

        getter = db.getRowUsername(username);
        getter.moveToFirst();

        adapter = new SplitAdapter(getBaseContext(), username);
        recyclerView.setAdapter(adapter);


        ((GlobalData)this.getApplication()).setUserId(getter.getInt(0));
        Log.d("SPLIT", "Set global user id to " + getter.getInt(0));
        name = getter.getString(1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);

        FloatingActionButton logoutButton, addSplitButton;
        final FloatingActionsMenu fam = (FloatingActionsMenu)findViewById(R.id.fab_menu);
        logoutButton = (FloatingActionButton) findViewById(R.id.logout);
        addSplitButton = (FloatingActionButton) findViewById(R.id.add_split);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUsername(getApplicationContext());
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addSplitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splitActivity = new Intent(HomeActivity.this, EventDetailsActivity.class);
                startActivity(splitActivity);
                fam.collapse();
            }
        });

    }


}
