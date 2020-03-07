package dao;

import entity.Card;
import entity.DailyReport;
import table.DailyReportsStoredByTime;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DailyReportsStoredByTimeDao extends BaseDao<Integer, DailyReportsStoredByTime> {
    public DailyReportsStoredByTimeDao(Connection connection) {
        super(connection);
    }

    public Integer getDailyReportID(int adminUserID, int year, int month, int day) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.daily_reports_stored_by_time WHERE admin_user_id = ?, \n" +
                                     "year = ?, month = ?, day = ?")) {
            ps.setInt(1, adminUserID);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ps.setInt(4, day);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getInt(3);
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    public DailyReportsStoredByTime save(DailyReportsStoredByTime dailyReportsStoredByTime) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.daily_reports_stored_by_time (admin_user_id, " +
                                     "daily_report_id, year, month, day) VALUES (?, ?, ?, ?, ?) \n" +
                                     "RETURNING id")) {
            ps.setInt(1, dailyReportsStoredByTime.getAdminUserID());
            ps.setInt(2, dailyReportsStoredByTime.getDailyReportID());
            ps.setInt(3, dailyReportsStoredByTime.getYear());
            ps.setInt(4, dailyReportsStoredByTime.getMonth());
            ps.setInt(5, dailyReportsStoredByTime.getDay());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            DailyReportsStoredByTime newDailyReportStoredByTime = new DailyReportsStoredByTime();
            newDailyReportStoredByTime.setId(result.getInt(1));
            newDailyReportStoredByTime.setDailyReportID(dailyReportsStoredByTime.getDailyReportID());
            newDailyReportStoredByTime.setAdminUserID(dailyReportsStoredByTime.getDailyReportID());
            newDailyReportStoredByTime.setMonth(dailyReportsStoredByTime.getMonth());
            newDailyReportStoredByTime.setYear(dailyReportsStoredByTime.getYear());
            newDailyReportStoredByTime.setDay(dailyReportsStoredByTime.getDay());
            return dailyReportsStoredByTime;

        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    public ArrayList<Integer> findDailyReportOfOneMonth(int month, int year, int adminUserID) {
        ArrayList<Integer> result = new ArrayList<>();
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.daily_reports_stored_by_time WHERE admin_user_id = ?, \n" +
                                     "year = ?, month = ?")) {
            ps.setInt(1, adminUserID);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result.add(resultSet.getInt(3));
                return result;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            Logger.getLogger(DailyReportsStoredByTimeDao.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    public ArrayList<Integer> findDailyReportOfOneYear(int year, int adminUserID) {
        ArrayList<Integer> result = new ArrayList<>();
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.daily_reports_stored_by_time WHERE admin_user_id = ?, \n" +
                                     "year = ?" )) {
            ps.setInt(1, adminUserID);
            ps.setInt(2, year);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result.add(resultSet.getInt(3));
                return result;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            Logger.getLogger(DailyReportsStoredByTimeDao.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }
}
