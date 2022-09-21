package com.svahnen.synchronizedclock;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout
        TextView clock = findViewById(R.id.clock); //Find the clock textview
        try { //Try to create a new clock config
            new ClockConfig(clock);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button button= findViewById(R.id.offline_button); //Find the button
        button.setOnClickListener(view -> { //Set the button to change the offline status
            if (ClockConfig.offline) {
                if (getInternetConnection()) {
                    ClockConfig.offline = false;
                    button.setText("Online");
                } else {
                    button.setText("No internet");
                }
            } else {
                ClockConfig.offline = true;
                button.setText("Offline");
                System.out.println("Set to offline");
            }
        });

    }
    // Function to check if the device is connected to the internet
    public boolean getInternetConnection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())connected = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())connected = true;
        }
        if (connected) {
            System.out.println("Have connection, device is online");
            return true;
        } else {
            System.out.println("No connection, device is offline");
            return false;
        }
    }

}