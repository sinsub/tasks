package com.example.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import com.example.tasks.dataStructure.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting up day/night mode;
        Manager manager = new Manager(this);
        User user = manager.getUser();
        if (user.isDarkModeOn()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        // Start List Activity :
        Intent LVAIntent = new Intent(getApplicationContext(), ListViewActivity.class);
        startActivity(LVAIntent);

        // End this activity :
        finish();
    }
}