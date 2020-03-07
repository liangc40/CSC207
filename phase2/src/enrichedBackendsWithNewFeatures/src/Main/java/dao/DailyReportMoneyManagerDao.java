package dao;

import entity.Card;
import entity.DailyReport;
import manager.DailyReportMoneyManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A DailyReportMoneyManagerDao accessing the daily report money managers in the database. */
public class DailyReportMoneyManagerDao extends dao.BaseDao<Integer, DailyReportMoneyManager> {
    public DailyReportMoneyManagerDao(Connection connection) {
        super(connection);
    }

    /**
     * Return the DailyReportMoneyManager with information that matches the entry of Integer
     * dailyReportMoneyManagerID in the database.
     *
     * @param dailyReportMoneyManagerID the ID to search for the daily report money manager in the database
     * @return a DailyReportMoneyManager with corresponding information
     */
    public DailyReportMoneyManager get(Integer dailyReportMoneyManagerID) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.daily_report_money_manager WHERE daily_report_money_manager_id = ?")) {
            ps.setInt(1, dailyReportMoneyManagerID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            DailyReportMoneyManager dailyReportMoneyManager = new DailyReportMoneyManager(this);
            dailyReportMoneyManager.setDailyReportMoneyManagerID(
                    result.getInt("daily_report_money_manager_id"));
            dailyReportMoneyManager.setDailyCost(result.getBigDecimal("daily_cost"));
            dailyReportMoneyManager.setDailyRevenue(result.getBigDecimal("daily_revenue"));
            return dailyReportMoneyManager;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Save the DailyReportMoneyManager dailyReportMoneyManager to the database and return the
     * DailyReportMoneyManager
     *
     * @param dailyReportMoneyManager the DailyReportMoneyManager to be saved
     * @return the DailyReportMoneyManager just saved
     */
    public DailyReportMoneyManager save(DailyReportMoneyManager dailyReportMoneyManager) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.daily_report_money_manager (daily_cost, daily_revenue) VALUES (?, ?) \n"
                                     + "RETURNING daily_report_money_manager_id")) {
            ps.setBigDecimal(1, dailyReportMoneyManager.getDailyCost());
            ps.setBigDecimal(2, dailyReportMoneyManager.getDailyRevenue());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            DailyReportMoneyManager newDailyReportMoneyManager = new DailyReportMoneyManager(this);
            newDailyReportMoneyManager.setDailyReportMoneyManagerID(result.getInt(1));
            newDailyReportMoneyManager.setDailyCost(dailyReportMoneyManager.getDailyCost());
            newDailyReportMoneyManager.setDailyRevenue(dailyReportMoneyManager.getDailyRevenue());
            return newDailyReportMoneyManager;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the DailyReportMoneyManager in the database which has the same id as the DailyReportMoneyManager
     * dailyReportMoneyManager.
     *
     * @param dailyReportMoneyManager the DailyReportMoneyManager that the update is based on
     */
    public void update(DailyReportMoneyManager dailyReportMoneyManager) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "UPDATE transit_system.daily_report_money_manager SET daily_cost = ?, daily_revenue = ? \n" +
                                     "WHERE daily_report_money_manager_id = ?")) {
            ps.setBigDecimal(1, dailyReportMoneyManager.getDailyCost());
            ps.setBigDecimal(2, dailyReportMoneyManager.getDailyRevenue());
            ps.setInt(3, dailyReportMoneyManager.getDailyReportMoneyManagerID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
