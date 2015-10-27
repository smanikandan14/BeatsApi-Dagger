package mani.beatsalbumsearch.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static String convertSecondsToDisplayTime(int totalSecs) {

        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        String timeString = null;
        if( hours > 0 && minutes > 0 ) {
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeString = String.format("%02d:%02d", minutes, seconds);
        }

        return timeString;
    }

    public static String convertDateInStringToYear(String dateInString) {
        //2014-08-08
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateInString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return Integer.toString(calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
