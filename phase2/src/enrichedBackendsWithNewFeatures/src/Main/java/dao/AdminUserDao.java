package dao;

import entity.AdminUser;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** An AdminUserDao accessing the admin users in the database. */
public class AdminUserDao extends BaseDao<Integer, AdminUser> {
    /**
     * Construct the AdminUserDao.
     *
     * @param connection the Connection that connects the database.
     */
    public AdminUserDao(Connection connection) {
        super(connection);
    }

    /**
     * Return the AdminUser of the String city in the database.
     *
     * @param city the city the admin user is in charge of
     * @return the AdminUser of the city
     */
    public AdminUser get(String city) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.admin_user WHERE city = ?")) {
            ps.setString(1, city);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            AdminUser adminUser = new AdminUser();
            adminUser.setAdminUserID(result.getInt("admin_user_id"));
            adminUser.setUserID(result.getInt("user_id"));
            adminUser.setCity(result.getString("city"));
            adminUser.setTodayDailyReportID(result.getInt("today_daily_report_id"));
            adminUser.setCurrentYear(result.getInt("current_year"));
            adminUser.setCurrentYear(result.getInt("current_month"));
            return adminUser;
        } catch (SQLException exception) {
            Logger.getLogger(AdminUser.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Save the AdminUser adminUser to database and return the AdminUser.
     *
     * @param adminUser the AdminUser to be saved
     * @return the AdminUser just saved
     */
    public AdminUser save(AdminUser adminUser) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.admin_user (user_id,today_daily_report_id, city, current_year,  current_month) VALUES (?, ?, ?, ?, ?) RETURNING admin_user_id")) {
            ps.setInt(1, adminUser.getUserID());
            ps.setInt(2, adminUser.getTodayDailyReportID());
            ps.setString(3, adminUser.getCity());
            ps.setInt(4, adminUser.getCurrentYear());
            ps.setInt(5, adminUser.getCurrentMonth());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            AdminUser newAdminUser = new AdminUser();
            newAdminUser.setAdminUserID(result.getInt(1));
            newAdminUser.setUserID(adminUser.getUserID());
            newAdminUser.setTodayDailyReportID(adminUser.getTodayDailyReportID());
            newAdminUser.setCity(adminUser.getCity());
            newAdminUser.setCurrentYear(adminUser.getCurrentYear());
            newAdminUser.setCurrentMonth(adminUser.getCurrentMonth());
            copyInfoFromUser(newAdminUser, adminUser);
            return newAdminUser;
        } catch (SQLException exception) {
            Logger.getLogger(AdminUserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Copy the information of AdminUser adminUser to AdminUser newAdminUser. This method returns
     * nothing.
     *
     * @param newAdminUser the AdminUser to copy to
     * @param adminUser the AdminUser to copy from
     */
    private void copyInfoFromUser(AdminUser newAdminUser, AdminUser adminUser) {
        newAdminUser.setName(adminUser.getName());
        newAdminUser.setEmail(adminUser.getEmail());
        newAdminUser.setRegistrationTime(adminUser.getRegistrationTime());
        newAdminUser.setPassword(adminUser.getPassword());
        newAdminUser.setBirthday(adminUser.getBirthday());
        newAdminUser.setTrafficStationNearWork(adminUser.getTrafficStationNearWork());
        newAdminUser.setTrafficStationNearHome(adminUser.getTrafficStationNearHome());
        newAdminUser.setCardIDs(adminUser.getCardIDs());
        newAdminUser.setUserLogger(adminUser.getUserLogger());
    }

    /**
     * Update the AdminUser in the database which has the same id as the AdminUser adminUser.
     *
     * @param adminUser the AdminUser that the update is based on
     */
    public void update(AdminUser adminUser) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.admin_user SET today_daily_report_id  = ?, current_year = ?,  current_month = ? WHERE admin_user_id = ?")) {
            ps.setInt(1, adminUser.getTodayDailyReportID());
            ps.setInt(2, adminUser.getCurrentYear());
            ps.setInt(3, adminUser.getCurrentMonth());
            ps.setInt(4, adminUser.getAdminUserID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AdminUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
