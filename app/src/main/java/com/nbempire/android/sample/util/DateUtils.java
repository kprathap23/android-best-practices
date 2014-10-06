package com.nbempire.android.sample.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nbarrios on 06/10/14.
 */
public abstract class DateUtils {

    public static boolean nearTo(Date stopTime, int proximity) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, proximity);

        Calendar stopTimeCalendar = Calendar.getInstance();
        stopTimeCalendar.setTime(stopTime);

        return now.after(stopTimeCalendar);
    }

    public static boolean isToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTime(date);

        return calendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) == otherCalendar.get(Calendar.DATE);
    }
}
