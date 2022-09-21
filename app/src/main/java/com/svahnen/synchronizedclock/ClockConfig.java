package com.svahnen.synchronizedclock;

import android.os.Handler;
import android.widget.TextView;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ClockConfig {

    private static NTPUDPClient timeClient = null;
    private static InetAddress inetAddress = null;
    private static TimeInfo timeInfo = null;
    private static long returnTime;
    public static Boolean offline = true;
    private Handler hUpdate;
    private Runnable rUpdate;
    String time = "Loading...";
    int loops = 0;

    public static final String TIME_SERVER = "time-a.nist.gov"; //Set NTP server

    public ClockConfig(TextView clock) throws IOException {

        hUpdate = new Handler(); // Handler to update UI from inside a thread
        rUpdate = () -> { // Runnable to update UI from inside a thread
            System.out.println("Updating UI from handler");
            clock.setText(time); //Set the clock text
        };
        clock.setText(time);

        //Create a new thread to update the clock
        Thread tUpdate = new Thread() {
            public void run() {
                while(true) {
                    loops++;
                    System.out.println("Loop: " + loops);
                    hUpdate.post(rUpdate);
                    try {
                        sleep(5000); //Sleep so the clock doesn't update too fast
                        System.out.println("Going to update time");
                        time = getCurrentNetworkTime().toString();
                        System.out.println("Should have updated time");
                    } catch (InterruptedException e) {
                        System.out.println("Error updating time");
                        e.printStackTrace();
                    }
                }
            }
        };
        tUpdate.start();
    }

    // Function to get the current time from the NTP server
    public Date getCurrentNetworkTime() {
        if (timeClient == null && !offline) {
            timeClient = new NTPUDPClient();
            System.out.println("Created time client");
            try {
                inetAddress = InetAddress.getByName(TIME_SERVER);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            System.out.println("Got inet address");
        }
        int tries = 0;
        while(tries < 5) { //Try to get the time 5 times before using the offline time
            tries++;
            try {
                if (offline) {
                    System.out.println("Offline");
                    return new Date(System.currentTimeMillis());
                }
                timeClient.open();
                timeClient.setSoTimeout(2000);
                System.out.println("Trying to get online time (often gets timed out, will try 5 times)");
                //This sometime gets timed out, current workaround is to just try again
                //TODO: Find a better way to handle this
                timeInfo = timeClient.getTime(inetAddress);
                System.out.println("Got time info");
                returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
                System.out.println("Got return time");
                break;
            } catch (IOException e) {
                System.out.println("Error getting time");
                if (tries == 5) { //If it fails 5 times, use the offline time
                    returnTime = System.currentTimeMillis();
                    System.out.println("Using system time");
                }
                e.printStackTrace();
            }
        }
        return new Date(returnTime);
    }
}
