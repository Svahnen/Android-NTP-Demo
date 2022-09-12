package com.svahnen.synchronizedclock;

import android.widget.TextView;

public class ClockConfig {
    private TextView clock;

    public ClockConfig(TextView clock) {
        this.clock = clock;
        clock.setText("some text");


    }
}
