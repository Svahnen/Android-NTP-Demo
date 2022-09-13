package com.svahnen.synchronizedclock;

import android.widget.TextView;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

public class ClockConfig  {

    public static final String TIME_SERVER = "time-a.nist.gov";

    public ClockConfig(TextView clock) throws IOException {

        clock.setText("Loading...");

        Thread thread = new Thread(() -> {
            try  {
                //System.out.println("Time is: " + getCurrentNetworkTime());
                clock.setText(getCurrentNetworkTime().toString());
                //Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

    }

    public static Date getCurrentNetworkTime() throws IOException {
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
        //long returnTime = timeInfo.getReturnTime();   //local device time

        return new Date(returnTime);
    }
}
