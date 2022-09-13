package com.svahnen.synchronizedclock;

import android.os.Handler;
import android.widget.TextView;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ClockConfig  {

    private static NTPUDPClient timeClient = null;
    private static InetAddress inetAddress = null;
    private static TimeInfo timeInfo = null;
    private Handler hUpdate;
    private Runnable rUpdate;
    String time = "Loading...";
    int loops = 0;

    public static final String TIME_SERVER = "time-a.nist.gov";

    public ClockConfig(TextView clock) throws IOException {

        hUpdate = new Handler();
        rUpdate = () -> clock.setText(time);
        clock.setText(time);

        Thread tUpdate = new Thread() {
            public void run() {
                while(true) {
                    loops++;
                    System.out.println("Loop: " + loops);
                    hUpdate.post(rUpdate);
                    try {
                        sleep(5000);
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


    public static Date getCurrentNetworkTime() {
        if (timeClient == null) {
            timeClient = new NTPUDPClient();
            System.out.println("Created time client");
            try {
                inetAddress = InetAddress.getByName(TIME_SERVER);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            System.out.println("Got inet address");
        }
        while(true){
            try {
                timeClient.open();
                timeClient.setSoTimeout(2000);
                System.out.println("Trying to get time (often breaks here)");
                timeInfo = timeClient.getTime(inetAddress);
                break;
            } catch (IOException e) {
                System.out.println("Error getting time");
                e.printStackTrace();
            }
        }
        System.out.println("Got time info");
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
        System.out.println("Got return time");
        timeClient.close();
        return new Date(returnTime);
    }
}
