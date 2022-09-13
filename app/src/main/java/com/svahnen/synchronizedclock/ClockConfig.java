package com.svahnen.synchronizedclock;

import android.widget.TextView;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.time.TimeTCPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ClockConfig  {
    private TextView clock;
    public static final String TIME_SERVER = "time-a.nist.gov";

    public ClockConfig(TextView clock) throws IOException {
        this.clock = clock;
        clock.setText("Some text");
        //System.out.println(getCurrentNetworkTime());

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    System.out.println(getCurrentNetworkTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();



    }

    public static long getCurrentNetworkTime() throws IOException {
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        //long returnTime = timeInfo.getReturnTime();   //local device time
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time

        Date time = new Date(returnTime);

        System.out.println("Time from " + TIME_SERVER + ": " + time);

        return returnTime;
    }
}
