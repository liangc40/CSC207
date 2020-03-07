import java.text.ParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Represents an admin user which generates daily report for a city's transit system.
 *
 */
class AdminUser {

    /** Daily Report object representing today's daily report*/
    private DailyReport todayDailyReport;
    /** ArrayList storing all daily reports created by admin user*/
    private ArrayList<DailyReport> dailyReports = new ArrayList<>();
    /** String representing city's name */
    private String city;

    /**
     * Initializes an admin user.
     *
     * @param city String representing the city name attached to this admin user
     * @param date String representing the date at which this admin user creates a daily report
     *
     */
    AdminUser(String city, String date) {
        createNewDailyReport(date);
        Main.adminUserMap.put(city, this); //add this admin user to the HashMap of adminUserMap in main class
        this.city = city;
    }

    /**
     * Creates daily report.
     *
     *@param date representing date of a daily report
     */
    private void createNewDailyReport(String date) {
        this.todayDailyReport = new DailyReport(date);
    }

    /**
     * Returns a DailyReport object.
     *
     * @return DailyReport representing today's daily report.
     */
    DailyReport getTodayDailyReport() {
        return todayDailyReport;
    }

    /**
     * Adds today's daily report to the ArrayList of dailyReports.
     * Creates next date's daily report and log its message.
     *
     */
    void generateDailyReport() throws ParseException {
        dailyReports.add(todayDailyReport);
        /* Logger Factory object used to log messages by admin user*/
        UserLogger userLogger = new UserLogger();
        userLogger.setLogId(city + "'s daily report");
        userLogger.getLogger().info(city + "-- " + todayDailyReport.toString());
        createNewDailyReport(increaseDateByOne());
    }

    /**
     * Returns a String that the next date related to the today's daily report.
     *
     * @return String representing the next date
     * @throws ParseException a parseException.
     */
    String increaseDateByOne() throws ParseException {
        String date = todayDailyReport.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        calendar.add(Calendar.DATE, 1);
        date = dateFormat.format(calendar.getTime());
        return date;
    }



}