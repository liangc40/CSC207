package edu.toronto.group0162.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.Math.abs;

/**
A TimeService
 */
public class TimeService {

    /**
     * Return a long which is the hour difference in the duration from time1 to time2.
     *
     * @param time1 the starting time
     * @param time2 the ending time
     * @return the hour difference in between
     */
    public long getHourDifference(String time1, String time2) {
        // Set new simple date format that input time should stick to.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Set the time zone of our format as "EDT".
        //format.setTimeZone(TimeZone.getTimeZone("Eastern Daylight Time"));
        // Parse time1 and time2 to simple date format.
        Date d1 = null;
        try {
            d1 = format.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Return the time difference rounded down closest hour.
        return abs((d2.getTime() - d1.getTime()) / (1000 * 60 * 60));
    }

    public double getHourDifferenceWithoutAbs(String time1, String time2) {
        // Set new simple date format that input time should stick to.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Parse time1 and time2 to simple date format.
        Date d1 = null;
        try {
            d1 = format.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Return the time difference rounded down closest hour.
        return (d2.getTime() - d1.getTime() / (1000 * 60 * 60));
    }

    /**
     * Increase String date by int daysToIncrease. Return the date after operation.
     *
     * @param date starting date
     * @param daysToIncrease days to add
     * @return the date after operation
     */
    // String increaseDateByOne(DailyReport dailyReport) {
    public String increaseDate(String date, int daysToIncrease) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = setUpCalender(date, dateFormat);
        calendar.add(Calendar.DATE, daysToIncrease);
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    /**
     * Set up a Calendar.
     *
     * @param date date of the created Calendar
     * @param dateFormat dateFormat of the created Calendar
     * @return the Calendar
     */
    private Calendar setUpCalender(String date, SimpleDateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
