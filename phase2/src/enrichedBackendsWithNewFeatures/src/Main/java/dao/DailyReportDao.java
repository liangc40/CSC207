package dao;

import entity.Card;
import entity.DailyReport;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A DailyReportDao accessing the daily reports in the database. */
public class DailyReportDao extends dao.BaseDao<Integer, DailyReport> {

  /**
   * Construct the DailyReportDao.
   *
   * @param connection the Connection that connects the database.
   */
  public DailyReportDao(Connection connection) {
    super(connection);
  }

  /**
   * Return the DailyReport with information that matches the entry of Integer dailyReportID in the
   * database.
   *
   * @param dailyReportID the ID to search for the daily report in the database
   * @return a DailyReport with corresponding information
   */
  public DailyReport get(Integer dailyReportID) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.daily_report WHERE daily_report_id = ?")) {
      ps.setInt(1, dailyReportID);
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      DailyReport dailyReport = new DailyReport();
      dailyReport.setDailyReportID(result.getInt("daily_report_id"));
      dailyReport.setDate(result.getString("date"));
      dailyReport.setDailyReportMoneyManagerID(result.getInt("daily_report_money_manager_id"));
      return dailyReport;
    } catch (SQLException exception) {
      Logger.getLogger(CardDao.class.getName())
          .log(Level.SEVERE, "An SQL exception has occurred", exception);
    }
    return null;
  }

  /**
   * Save the DailyReport dailyReport to the database and return the DailyReport.
   *
   * @param dailyReport the DailyReport to be saved
   * @return the DailyReport just saved
   */
  public DailyReport save(DailyReport dailyReport) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "INSERT INTO transit_system.daily_report (date, daily_report_money_manager_id) VALUES (?, ?) \n"
                + "RETURNING daily_report_id")) {
      ps.setString(1, dailyReport.getDate());
      ps.setInt(2, dailyReport.getDailyReportMoneyManagerID());
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      DailyReport newDailyReport = new DailyReport();
      newDailyReport.setDailyReportID(result.getInt(1));
      newDailyReport.setDate(dailyReport.getDate());
      newDailyReport.setDailyReportMoneyManagerID(dailyReport.getDailyReportMoneyManagerID());
      return newDailyReport;
    } catch (SQLException exception) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
    }
    return null;
  }
}
