package com.svahnen.synchronizedclock;

import android.widget.TextView;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ClockConfig {
    private TextView clock;

    public ClockConfig(TextView clock) {
        this.clock = clock;
        clock.setText("some text");

        String host = "ntp02.oal.ul.pt";

        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 5 seconds
        client.setDefaultTimeout(5000);


        InetAddress hostAddr = null;
        try {
            hostAddr = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (hostAddr != null) {
            System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
        }
        TimeInfo info = null;
        try {
            info = client.getTime(hostAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (info != null) {
            Date date = new Date(info.getReturnTime());
            System.out.println(date);
            clock.setText(date.toString());
        }

        client.close();

    }
}
