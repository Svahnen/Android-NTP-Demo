package com.svahnen.synchronizedclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView clock = findViewById(R.id.clock);
        try {
            ClockConfig clockConfig = new ClockConfig(clock);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}