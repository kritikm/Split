package com.first.kritikm.split;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    UserDataContract.UserEntry db;

    public static final String extra_message = "username";
    Button showhidebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new UserDataContract.UserEntry(this);

        showhidebutton = (Button)findViewById(R.id.showhide);
        showhidebutton.setOnTouchListener(new Touch());
    }

    public void signUp(View view)
    {
        //TODO: Add an animation to this dialog
        final Dialog signup = new Dialog(this);
        signup.setContentView(R.layout.sign_up);
        signup.show();

        Button regButton = (Button) signup.findViewById(R.id.signup_reg);
        Button cancelReg = (Button) signup.findViewById(R.id.cancel_reg);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) signup.findViewById(R.id.name_reg);
                EditText username = (EditText) signup.findViewById(R.id.username_reg);
                EditText email = (EditText)signup.findViewById(R.id.email_reg);
                EditText password = (EditText)signup.findViewById(R.id.password_reg);
                EditText re_enter_pass = (EditText)signup.findViewById(R.id.re_enter_reg);

                if(name.getText().toString().length() == 0 || username.getText().toString().length() == 0
                        || password.getText().toString().length() == 0
                        || re_enter_pass.getText().toString().length() == 0
                        || email.getText().toString().length() == 0)
                {
                    Toast.makeText(getBaseContext(), "No fields can be left empty!", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().equals(re_enter_pass.getText().toString()) == false) {
                    Toast.makeText(getBaseContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    re_enter_pass.setText("");
                }
                else {
                    //Register to database

                    //TODO All this should be in an Async Function

                    if ((db.getRowUsername(username.getText().toString())).moveToFirst()) {
                        Toast.makeText(getBaseContext(), "Username taken!", Toast.LENGTH_SHORT).show();
                        username.setText("");
                    } else if ((db.getRowEmail(email.getText().toString())).moveToFirst()) {
                        Toast.makeText(getBaseContext(), "Account with that email already exists!", Toast.LENGTH_SHORT).show();
                        email.setText("");
                    } else {
                        if (!db.insert(name.getText().toString(),
                                username.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString())) {
                            Snackbar.make(new CoordinatorLayout(MainActivity.this), "Error updating. Try again!", Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            SaveSharedPreference.setUsername(getApplicationContext(), username.getText().toString());
                            Intent send_username = new Intent(getBaseContext(), HomeActivity.class);
                            sendIntent(send_username, username.getText().toString());
                            signup.dismiss();
                        }
                    }
                }

                }
            });

        cancelReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.dismiss();
            }
        });
    }

    public void login(View view)
    {
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);
        CheckBox rememberMe = (CheckBox)findViewById(R.id.rememberme);

        boolean go = true;

        if(username.getText().toString().length() == 0) {
            username.setHint("don't leave me blank :(");
            username.setHintTextColor(Color.parseColor("#ff0000"));
            go = false;
        }
        if(password.getText().toString().length() == 0)
        {
            password.setHint("don't leave me blank :(");
            password.setHintTextColor(Color.parseColor("#ff0000"));
        }

        if(go) {

            Cursor c;
            c = db.getRowUsername(username.getText().toString());
            if (c.moveToFirst()) {
                if (password.getText().toString().equals(c.getString(4))) {
                    if (rememberMe.isChecked())
                        SaveSharedPreference.setUsername(getApplicationContext(), username.getText().toString());
                    Intent send_username = new Intent(this, HomeActivity.class);
                    sendIntent(send_username, c.getString(2));
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Incorrect Password :/", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    password.setHint("password");
                }
            }
            else
            {
                c = db.getRowEmail(username.getText().toString());
                if(c.moveToFirst())
                {
                    if (password.getText().toString().equals(c.getString(4))) {
                        if (rememberMe.isChecked())
                            SaveSharedPreference.setUsername(getApplicationContext(), c.getString(2));
                        Intent send_username = new Intent(this, HomeActivity.class);
                        sendIntent(send_username, c.getString(2));
                    }
                    else
                    {
                        Snackbar.make(findViewById(R.id.main_layout), "Incorrect Password :/", Snackbar.LENGTH_SHORT).show();
                        password.setText("");
                    }

                }
                else
                {
                    Snackbar.make(findViewById(R.id.main_layout), "User not recognized :/", Snackbar.LENGTH_SHORT).show();
                    username.setText("");
                    username.setHint("username");
                }
            }
        }
    }

    public void sendIntent(Intent intent, String username)
    {
        intent.putExtra(extra_message, username);
        startActivity(intent);
        finish();
    }

    class Touch implements View.OnTouchListener
    {
        public boolean onTouch(View view, MotionEvent event)
        {
            EditText password = (EditText)findViewById(R.id.password);
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    break;

                case MotionEvent.ACTION_UP:
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    break;
            }

            return true;
        }
    }

}
