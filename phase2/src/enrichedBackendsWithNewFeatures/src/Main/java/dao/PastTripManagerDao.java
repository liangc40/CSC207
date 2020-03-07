package dao;

import entity.Card;
import manager.CardMoneyManager;
import manager.PastTripManager;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A PastTripManagerDao accessing the past trip managers in the database. */
public class PastTripManagerDao extends BaseDao<String, PastTripManager> {

    /**
     * Construct the OneWayTripFrequencyDao.
     *
     * @param connection the Connection that connects the database.
     */
    public PastTripManagerDao(Connection connection) {super(connection); }

    /**
     * Return the PastTripManager with information that matches the entry of Integer pastTripManagerID in
     * the database.
     *
     * @param pastTripManagerID the ID to search for the past trip in the database
     * @return a PastTripManager with corresponding information
     */
    public PastTripManager get(Integer pastTripManagerID) {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.past_trip_manager WHERE past_trip_manager_id = ?")) {
            ps.setInt(1, pastTripManagerID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            PastTripManager pastTripManager = new PastTripManager();
            pastTripManager.setPastTripManagerID(result.getInt("past_trip_manager_id"));
            Array pastTripIDs = result.getArray("past_trip_ids");
            ArrayList<Integer> transitPassIDArrayList = new ArrayList<>(Arrays.asList((Integer[]) pastTripIDs.getArray()));
            pastTripManager.setPastTripIDs(transitPassIDArrayList);
            return pastTripManager;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Save the PastTripManager pastTripManager to the database and return the PastTripManager.
     *
     * @param pastTripManager the PastTripManager to be saved
     * @return the PastTripManager just saved
     */
    public PastTripManager save(PastTripManager pastTripManager) {
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.past_trip_manager (past_trip_ids) VALUES (?) RETURNING past_trip_manager_id")) {
//            ps.setArray(1, connection.createArrayOf("integer", pastTripManager.getPastTripIDs()));
            ps.setArray(1, connection.createArrayOf("integer", pastTripManager.getPastTripIDs().toArray()));
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            PastTripManager newPastTripManager = new PastTripManager();
            newPastTripManager.setPastTripManagerID(result.getInt(1));
            return newPastTripManager;

        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the PastTripManager in the database which has the same id as the PastTripManager
     * pastTripManager.
     *
     * @param pastTripManager the PastTripManager that the update is based on
     */
    public void update(PastTripManager pastTripManager) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.past_trip_manager SET past_trip_ids = ? WHERE past_trip_manager_id = ?")) {
            ps.setArray(1, connection.createArrayOf("integer", pastTripManager.getPastTripIDs().toArray()));
            ps.setInt(2, pastTripManager.getPastTripManagerID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
