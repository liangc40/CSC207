package manager;

import entity.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

public class TimeManager {

    /**
     * Return the total months in the duration from time1 to time2.
     *
     * @param time1 the starting time
     * @param time2 the ending time
     * @return the total months in between
     * @throws ParseException the exception to be thrown
     */
    public int getMonthDifference(String time1, String time2) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        format.setTimeZone(TimeZone.getTimeZone("EDT"));
//        Date d1 = null;
//        try {
//            d1 = format.parse(time1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date d2 = null;
//        try {
//            d2 = format.parse(time2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Calendar calendar1 = Calendar.getInstance();
//        Calendar calendar2 = Calendar.getInstance();
//        calendar1.setTime(d1);
//        calendar2.setTime(d2);
//        return (calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR)) * 12
//                + (calendar2.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH))
//                + 1;
        return (getYear(time2) - getYear(time1))*12 + (getMonth(time2) - getMonth(time1)) + 1;
    }

    public long getHourDifference(String time1, String time2) {
        // Set new simple date format that input time should stick to.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        // Set the time zone of our format as "EDT".
        format.setTimeZone(TimeZone.getTimeZone("Eastern Daylight Time"));
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
        return (d2.getTime() - d1.getTime()) / (1000 * 60 * 60);
    }

    //  /**
    //   * Returns a String that the next date related to the today's daily report.
    //   *
    //   * @return String representing the next date
    //   */
    //  String increaseDateByOne(DailyReport dailyReport) {
    //    String date = dailyReport.getDate();
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //    Calendar calendar = Calendar.getInstance();
    //    try {
    //      calendar.setTime(dateFormat.parse(date));
    //    } catch (ParseException e) {
    //      e.printStackTrace();
    //    }
    //    calendar.add(Calendar.DATE, 1);
    //    date = dateFormat.format(calendar.getTime());
    //    return date;
    //  }

    // eden.........
    // String increaseDateByOne(DailyReport dailyReport) {
    public String increaseDate(String date, int daysToIncrease) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = setUpCalender(date, dateFormat);
        calendar.add(Calendar.DATE, daysToIncrease);
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    // LC.......................................
    public String findLastDayOfCurrentMonth(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = setUpCalender(date, dateFormat);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    // LC...........................................
    public String findNextSunday(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = setUpCalender(date, dateFormat);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    public String find_nth_birthday(User user, Integer yearToIncrease) {
        String birthday = user.getBirthday();
        if (birthday!= null) {
            return increaseBirthday(birthday, yearToIncrease);
        } else {
            return "Please fill in birthday please.";
        }
    }

    public String increaseBirthday(String date,  int yearToIncrease) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = setUpCalender(date, dateFormat);
        calendar.add(Calendar.YEAR, yearToIncrease);
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    private Calendar setUpCalender(String date, SimpleDateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public Boolean isValid(String chargingTime, String expirationDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne = null;
        Date dateTwo = null;
        try {
            dateOne = dateFormat.parse(chargingTime);
            dateTwo = dateFormat.parse(expirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert (dateOne != null && dateTwo != null);
        return dateOne.compareTo(dateTwo) < 0;
    }

    public Boolean isBirthday(String chargingTime, String birthday) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarOne = Calendar.getInstance();
        Calendar calendarTwo = Calendar.getInstance();
        try {
            calendarOne.setTime(dateFormat.parse(chargingTime));
            calendarTwo.setTime(dateFormat.parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (calendarOne.get(Calendar.MONTH) == calendarTwo.get(Calendar.MONTH))
                && (calendarOne.get(Calendar.DATE) == calendarTwo.get(Calendar.DATE));
    }

    // eden.....
    boolean areInTheSameMonth(String date1, String date2) {
        return getMonthDifference(date1, date2) == 0;
    }

    public String convertTimeFromLongToStringFormatOne(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public String convertTimeFromLongToStringFormatTwo(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        return dateFormat.format(date);
    }

    public int getYear(String time) {
        return setDateInCalendar(time).get(Calendar.YEAR);
    }

    public int getMonth(String time) {
        return  setDateInCalendar(time).get(Calendar.MONTH);
    }

    public int getDate(String time) {
        return setDateInCalendar(time).get(Calendar.DATE);
    }

    private Calendar setDateInCalendar(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("EDT"));
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        assert date != null;
        calendar.setTime(date);
        return calendar;
    }
}
