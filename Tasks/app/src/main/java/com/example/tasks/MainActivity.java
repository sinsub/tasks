package com.example.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Activity :
        Intent LVAIntent = new Intent(getApplicationContext(), ListViewActivity.class);
        startActivity(LVAIntent);

        // End this activity :
        finish();
    }
}