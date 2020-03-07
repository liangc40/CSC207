package dao;
import entity.Card;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.AllArgsConstructor;
import manager.CardMoneyManager;
import manager.UserMoneyManager;
import table.TotalFareByCity;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A UserMoneyManagerDao accessing the user money managers in the database. */
public class UserMoneyManagerDao extends dao.BaseDao<Integer, UserMoneyManager>{
    private TotalFareByCityDao totalFareByCityDao;
    private SystemInfoDao systemInfoDao;
    private UserMoneyManagerDao userMoneyManagerDao;

    /**
     * Construct the UserMoneyManagerDao.
     *
     * @param connection the Connection that connects the database.
     */
    public UserMoneyManagerDao(Connection connection) {super(connection); }

    /**
     * Return a UserMoneyManager with information that matches the entry of Integer userMoneyManagerID in the
     * database.
     *
     * @param userMoneyManagerID the user money manager ID to search for in the database
     * @return a corresponding UserMoneyManager of the id
     */
    public UserMoneyManager get(Integer userMoneyManagerID) {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.user_money_manager WHERE user_money_manager_id = ?")) {
            ps.setInt(1, userMoneyManagerID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            UserMoneyManager userMoneyManager = new UserMoneyManager();
            userMoneyManager.setUserMoneyManagerID(result.getInt("user_money_manager_id"));
            userMoneyManager.setUserID(result.getInt("user_id"));
            userMoneyManager.setTotalTrafficFare(result.getBigDecimal("total_traffic_fare"));
            userMoneyManager.setStatus(result.getString("status"));
            userMoneyManager.setMembershipDiscountRate(result.getBigDecimal("membership_discount_rate"));
            userMoneyManager.setChildrenDiscountID(result.getInt("child_discount_id"));
            return userMoneyManager;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, "Cannot get data from card manager table.", exception);
        }
        return null;
    }

    /**
     * Save the UserMoneyManager to the database and return the UserMoneyManager.
     *
     * @param userMoneyManager the UserMoneyManager to be saved
     * @return the UserMoneyManager just saved
     */
    public UserMoneyManager save(UserMoneyManager userMoneyManager) {
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.user_money_manager (total_traffic_fare, user_id, status, membership_discount_rate, child_discount_id) VALUES (?, ?, ?, ?, ?) RETURNING user_money_manager_id")) {
            ps.setBigDecimal(1, userMoneyManager.getTotalTrafficFare());
            ps.setInt(2, userMoneyManager.getUserID());
            ps.setString(3, userMoneyManager.getStatus());
            ps.setBigDecimal(4, userMoneyManager.getMembershipDiscountRate());
            ps.setInt(5, userMoneyManager.getChildrenDiscountID());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            UserMoneyManager newUserMoneyManager = new UserMoneyManager();
            newUserMoneyManager.setUserMoneyManagerID(result.getInt(1));
            newUserMoneyManager.setTotalTrafficFare(userMoneyManager.getTotalTrafficFare());
            newUserMoneyManager.setStatus(userMoneyManager.getStatus());
            newUserMoneyManager.setMembershipDiscountRate(userMoneyManager.getMembershipDiscountRate());
            newUserMoneyManager.setChildrenDiscountID(userMoneyManager.getChildrenDiscountID());
            return newUserMoneyManager;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    public void update(UserMoneyManager bean) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.user_money_manager SET total_traffic_fare = ?, status = ?, membership_discount_rate = ?, child_discount_id = ? WHERE user_money_manager_id = ?")) {
            ps.setBigDecimal(1, bean.getTotalTrafficFare());
            ps.setString(2, bean.getStatus());
            ps.setBigDecimal(3, bean.getMembershipDiscountRate());
            ps.setInt(4, bean.getChildrenDiscountID());
            ps.setInt(5, bean.getUserMoneyManagerID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
