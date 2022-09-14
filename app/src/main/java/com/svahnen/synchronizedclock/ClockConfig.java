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

    public static final String TIME_SERVER = "time-a.nist.gov";

    public ClockConfig(TextView clock) throws IOException {

        hUpdate = new Handler();
        rUpdate = () -> {
            System.out.println("From handler");
            clock.setText(time);
        };
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
        while(tries < 5) {
            tries++;
            try {
                if (offline) {
                    System.out.println("Offline");
                    return new Date(System.currentTimeMillis());
                }
                timeClient.open();
                timeClient.setSoTimeout(2000);
                System.out.println("Trying to get online time (often gets timed out, will try 5 times)");
                // This sometime gets timed out, current workaround is to just try again
                // TODO: Find a better way to handle this
                timeInfo = timeClient.getTime(inetAddress);
                System.out.println("Got time info");
                returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
                System.out.println("Got return time");
                break;
            } catch (IOException e) {
                System.out.println("Error getting time");
                if (tries == 5) {
                    returnTime = System.currentTimeMillis();
                    System.out.println("Using system time");
                }
                e.printStackTrace();
            }
        }
        return new Date(returnTime);
    }

    public void goOffline() {
        offline = true;
    }

    public void goOnline() {
        //TODO: Add a way to check if the device cant go online to solve issue #2
        offline = false;
    }
}
