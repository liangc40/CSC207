package entity;

import lombok.Data;

/** An AdminUser of a city. */
@Data
public class AdminUser extends User {
    /** ID of this AdminUser. */
    private int adminUserID;
    /** ID of today's Daily Report. */
    private int todayDailyReportID;
    /** City this AdminUser is in charge of. */
    private String city;
    private int currentYear;
    private int currentMonth;
}