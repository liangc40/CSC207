package table;

import lombok.Data;

@Data
public class DailyReportsStoredByTime {
    private int id;
    private int adminUserID;
    private int dailyReportID;
    private int year;
    private int month;
    private int day;
}
