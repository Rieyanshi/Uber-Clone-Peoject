package com.example.uberclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button phoneButton, googleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneButton = (Button) findViewById(R.id.button);
        googleButton = (Button) findViewById(R.id.button2);
    }
    public void phoneLoginClick(View view)
    {
        Intent intent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
        startActivity(intent);
    }

    public void googleLoginClick(View view)
    {
        Intent intent = new Intent(LoginActivity.this, GoogleLoginActivity.class);
        startActivity(intent);
    }
}