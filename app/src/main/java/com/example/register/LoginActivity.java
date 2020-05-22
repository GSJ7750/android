package com.example.register;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kch.proj_manager_v3.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText idText = (EditText)findViewById(R.id.subjectText);
        EditText passwordText = (EditText)findViewById(R.id.asdf);

        Button loginbtn = (Button)findViewById(R.id.loginbtn);
        TextView registerbtn = (TextView)findViewById(R.id.registerbtn);
        TextView lookupbtn = (TextView)findViewById(R.id.lookupbutton);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        lookupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }
}