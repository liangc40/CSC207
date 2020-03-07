package dao;

import entity.TransitPass;
import table.TransitPassExpirationDate;
import table.TripFrequencyBetweenStations;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A TransitPassExpirationDateDao accessing expiration dates of the transit passes in the database. */
public class TransitPassExpirationDateDao extends dao.BaseDao<String, TransitPassExpirationDate> {

    /**
     * Construct the TransitPassExpirationDateDao.
     *
     * @param connection the Connection that connects the database.
     */
    public TransitPassExpirationDateDao(Connection connection) {
        super(connection);
    }

    /**
     * Return a TransitPassExpirationDate with information that matches the entry of int adminUserID and
     * String expirationDate in the database.
     *
     * @param adminUserID the admin user ID to search for in the database
     * @param expirationDate the expiration date to search for in the database
     * @return a corresponding TransitPassExpirationDate
     */
    public TransitPassExpirationDate get(int adminUserID, String expirationDate) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.transit_pass_expiration_date WHERE admin_user_id = ? \n"
                                     + " AND expiration_date = ?")) {
            ps.setInt(1, adminUserID);
            ps.setString(2, expirationDate);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            Array transitPassIDArray = result.getArray("transit_pass_ids");
            TransitPassExpirationDate transitPassExpirationDate = new TransitPassExpirationDate();
            ArrayList<Integer> transitPassIDArrayList =
                    new ArrayList<>(Arrays.asList((Integer[]) transitPassIDArray.getArray()));
            transitPassExpirationDate.setTransitPassIDs(transitPassIDArrayList);
            return transitPassExpirationDate;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Save the TransitPassExpirationDate transitPassExpirationDate to the database and return the
     * TransitPassExpirationDate.
     *
     * @param transitPassExpirationDate the TransitPass to be saved
     * @return the TransitPassExpirationDate just saved
     */
    public TransitPassExpirationDate save(TransitPassExpirationDate transitPassExpirationDate) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.transit_pass_expiration_date (admin_user_id, expiration_date, \n"
                                     + "transit_pass_ids) VALUES (?, ?, ?) RETURNING id")) {
            ps.setInt(1, transitPassExpirationDate.getAdminUserID());
            ps.setString(2, transitPassExpirationDate.getExpirationDate());
            ps.setArray(
                    3,
                    connection.createArrayOf(
                            "integer", transitPassExpirationDate.getTransitPassIDs().toArray()));
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TransitPassExpirationDate newTransitPassExpirationDate = new TransitPassExpirationDate();
            newTransitPassExpirationDate.setID(result.getInt(1));
            newTransitPassExpirationDate.setAdminUserID(transitPassExpirationDate.getAdminUserID());
            newTransitPassExpirationDate.setExpirationDate(transitPassExpirationDate.getExpirationDate());
            newTransitPassExpirationDate.setTransitPassIDs(transitPassExpirationDate.getTransitPassIDs());
            return newTransitPassExpirationDate;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the TransitPassExpirationDate in the database which has the same id as the
     * TransitPassExpirationDate transitPassExpirationDate.
     *
     * @param transitPassExpirationDate the TransitPassExpirationDate that the update is based on
     */
    public void update(TransitPassExpirationDate transitPassExpirationDate) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.transit_pass_expiration_table SET transit_pass_ids = ? WHERE id = ?")) {
            ps.setArray(1, connection.createArrayOf("integer", transitPassExpirationDate.getTransitPassIDs().toArray()));
            ps.setInt(2, transitPassExpirationDate.getID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Integer adminUserID, String date) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement("DELETE FROM transit_system.transit_pass_expiration_date WHERE admin_user_id = ? AND expiration_date = ?")) {
            ps.setInt(1, adminUserID);
            ps.setString(2, date);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
