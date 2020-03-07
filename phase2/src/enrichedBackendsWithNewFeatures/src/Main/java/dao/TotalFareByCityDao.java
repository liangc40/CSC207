package dao;

import entity.Card;
import table.TotalFareByCity;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A TotalFareByCityDao accessing total fares of cities in the database. */
public class TotalFareByCityDao extends dao.BaseDao<String, TotalFareByCity> {

    /**
     * Construct the TotalFareByCityDao.
     *
     * @param connection the Connection that connects the database.
     */
    public TotalFareByCityDao(Connection connection) {
        super(connection);
    }

    /**
     * Return TotalFareByCity with information that matches the entry of
     * String city and int userID in the database.
     *
     * @param city the city to search for in the database
     * @param userID the userID to search for in the database
     * @return TotalFareByCity
     */
    public TotalFareByCity getTotalFareByCity(String city, int userID) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.total_fare_by_city_table WHERE city = ? AND user_id = ?")) {
            ps.setString(1, city);
            ps.setInt(2, userID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TotalFareByCity totalFareByCity = new TotalFareByCity();
            totalFareByCity.setID(result.getInt("id"));
            totalFareByCity.setCity(result.getString("city"));
            totalFareByCity.setUserID(result.getInt("user_id"));
            totalFareByCity.setTotalFareByCity(result.getBigDecimal("total_fare_by_city"));
            return totalFareByCity;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Save the TotalFareByCity totalFareByCity to the database and return the TotalFareByCity.
     *
     * @param totalFareByCity the TotalFareByCity to be saved
     * @return the TotalFareByCity just saved
     */
    public TotalFareByCity save(TotalFareByCity totalFareByCity) {
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.total_fare_by_city_table (user_id, city, total_fare_by_city) VALUES (?, ?, ?) RETURNING id")) {
            ps.setInt(1, totalFareByCity.getUserID());
            ps.setString(2, totalFareByCity.getCity());
            ps.setBigDecimal(3, totalFareByCity.getTotalFareByCity());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TotalFareByCity newTotalFareByCity = new TotalFareByCity();
            newTotalFareByCity.setID(result.getInt(1));
            newTotalFareByCity.setUserID(totalFareByCity.getUserID());
            newTotalFareByCity.setCity(totalFareByCity.getCity());
            newTotalFareByCity.setTotalFareByCity(totalFareByCity.getTotalFareByCity());
            return totalFareByCity;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the TotalFareByCity in the database which has the same id as the TotalFareByCity
     * totalFareByCity.
     *
     * @param totalFareByCity the TotalFareByCity that the update is based on
     */
    public void update(TotalFareByCity totalFareByCity) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.total_fare_by_city_table SET total_fare = ? WHERE id = ?")) {
            ps.setBigDecimal(1, totalFareByCity.getTotalFareByCity());
            ps.setInt(2, totalFareByCity.getID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
