package com.example.andreeat.logintest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference database;
    EditText username;
    EditText password;
    TextView username_message;
    TextView password_message;
    TextView button_message;
    Button signInBtn ;
    TextView nameLabel;
    TextView passwordLabel;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance().getReference("users");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        username_message = (TextView) findViewById(R.id.username_error_message);
        password_message = (TextView) findViewById(R.id.password_error_message);
        button_message = (TextView) findViewById(R.id.login_error_message);
        signInBtn = (Button) findViewById(R.id.login_button);
        nameLabel  = (TextView)findViewById(R.id.username_label);
        passwordLabel  = (TextView)findViewById(R.id.password_label);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_login);
    }

    public void SignInClick(View view){
        progressBar.setVisibility(View.GONE);
        boolean correct = true;
        username_message.setText("");
        password_message.setText("");
        button_message.setText("");

        if (username.getText().toString().isEmpty()){
            username_message.setText("Username cannot be empty");
            username_message.setTextColor(Color.RED);
            correct = false;
        }
        else{
            if (username.getText().toString().length() < 3){
                username_message.setText("Username is too short");
                username_message.setTextColor(Color.RED);
                correct = false;
            }
        }

        if (password.getText().toString().isEmpty()){
            password_message.setText("Password cannot be empty");
            password_message.setTextColor(Color.RED);
            correct = false;
        }
        else{
            if (password.getText().toString().length() < 6){
                password_message.setText("Password is too short");
                password_message.setTextColor(Color.RED);
                correct = false;
            }
        }

        if (correct) {
            database.orderByChild("username").equalTo(username.getText().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = null;
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        user = child.getValue(User.class);
                    }
                    String database_username = "";
                    String database_password = "";
                    if (user!= null) {
                        database_username = user.getUsername();
                        database_password = user.getPassword();
                    }

                    if (user != null && username.getText().toString().equals(database_username) && password.getText().toString().equals(database_password)) {
                        progressBar.setVisibility(View.VISIBLE);
                        username.setVisibility(View.GONE);
                        password.setVisibility(View.GONE);
                        username_message.setVisibility(View.GONE);
                        password_message.setVisibility(View.GONE);
                        button_message.setVisibility(View.GONE);
                        signInBtn.setVisibility(View.GONE);
                        nameLabel.setVisibility(View.GONE);
                        passwordLabel.setVisibility(View.GONE);
                        button_message.setVisibility(View.GONE);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                username.setVisibility(View.VISIBLE);
                                password.setVisibility(View.VISIBLE);
                                username_message.setVisibility(View.VISIBLE);
                                password_message.setVisibility(View.VISIBLE);
                                button_message.setVisibility(View.VISIBLE);
                                signInBtn.setVisibility(View.VISIBLE);
                                nameLabel.setVisibility(View.VISIBLE);
                                passwordLabel.setVisibility(View.VISIBLE);
                                button_message.setVisibility(View.VISIBLE);
                                button_message.setText("Login successfull!");
                                button_message.setTextColor(Color.GREEN);
                            }
                        }, 500);
                    } else
                    {
                        button_message.setText("Login failed. Username or password are inccorect!");
                        button_message.setTextColor(Color.RED);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Database error on login operation.");
                }
            });
        }
    }
}
