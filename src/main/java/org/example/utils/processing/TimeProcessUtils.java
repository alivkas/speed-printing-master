package org.example.utils.processing;

import org.example.commons.Time;

public class TimeProcessUtils {

    public int minutesToMilliseconds(int minute) {
        return minute * Time.MILLISECONDS;
    }
}
