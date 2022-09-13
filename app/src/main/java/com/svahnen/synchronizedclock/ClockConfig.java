package com.svahnen.synchronizedclock;


import android.widget.TextView;

import java.util.Date;
import java.util.TimeZone;

public class ClockConfig {
    private TextView clock;


    public ClockConfig(TextView clock) {
        this.clock = clock;
        clock.setText("some text");

        SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), new SNTPClient.Listener() {

            @Override
            public void onTimeResponse(String rawDate, Date date, Exception ex) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                clock.setText(rawDate);
            }

            @Override
            public void onTimeReceived(String rawDate) {

            }

            @Override
            public void onError(Exception ex) {

            }
        });

    }
}




