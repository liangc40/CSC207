package entity;

import lombok.Data;

/** A DailyReport of a city. */
@Data
public class DailyReport {
    /** Id in database. */
    private int DailyReportID;
    /** Date of this DailyReport. */
    private String date;
    /** Id of the MoneyManager of this DailyReport. */
    private int DailyReportMoneyManagerID;
}