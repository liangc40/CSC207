package dao;

import entity.Card;
import entity.GeneralTrip;
import entity.Node;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A GeneralTripDao accessing the general trips in the database. */
public class GeneralTripDao extends BaseDao<Integer, GeneralTrip> {

    /**
     * Construct the GeneralTripDao.
     *
     * @param connection the Connection that connects the database.
     */
    public GeneralTripDao(Connection connection) {
        super(connection);
    }

    /**
     * Return the GeneralTrip with information that matches the entry of Integer generalTripID in the database.
     *
     * @param generalTripID the ID to search for the general trip in the database
     * @return a GeneralTrip with corresponding information
     */
    public GeneralTrip get(Integer generalTripID) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.general_trip WHERE general_trip_id = ?")) {
            ps.setInt(1, generalTripID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            GeneralTrip generalTrip = new GeneralTrip();
            //            card.setCardID(result.getInt("card_id"));
            //            card.setUserID(result.getInt("user_id"));
            generalTrip.setEnterStation(result.getString("enter_station"));
            generalTrip.setExitStation(result.getString("exit_station"));
            Array tripSegmentIds = result.getArray("trip_segment_ids");
//      generalTrip.setTripSegmentsIDs((Object[]) tripSegmentIds.getArray());
            ArrayList<Integer> transitPassIDArrayList = new ArrayList<>(Arrays.asList((Integer[]) tripSegmentIds.getArray()));
            generalTrip.setTripSegmentsIDs(transitPassIDArrayList);
            return generalTrip;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Save the GeneralTrip generalTrip to the database and return the GeneralTrip.
     *
     * @param generalTrip the GeneralTrip to be saved
     * @return the generalTrip just saved
     */
    public GeneralTrip save(GeneralTrip generalTrip) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.general_trip (enter_station, exit_station, trip_segment_ids) VALUES (?, ?, ?) RETURNING general_trip_id")) {
            ps.setString(1, generalTrip.getEnterStation());
            ps.setString(2, generalTrip.getExitStation());
//      ps.setArray(3, connection.createArrayOf("integer", generalTrip.getTripSegmentsIDs()));
            ps.setArray(3, connection.createArrayOf("integer", generalTrip.getTripSegmentsIDs().toArray()));
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            GeneralTrip newGeneralTrip = new GeneralTrip();
            newGeneralTrip.setGeneralTripID(result.getInt(1));
            newGeneralTrip.setEnterStation(generalTrip.getEnterStation());
            newGeneralTrip.setExitStation(generalTrip.getExitStation());
            newGeneralTrip.setTripSegmentsIDs(generalTrip.getTripSegmentsIDs());
            return newGeneralTrip;

        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the GeneralTrip in the database which has the same id as the GeneralTrip generalTrip.
     *
     * @param generalTrip the GeneralTrip that the update is based on
     */
    public void update(GeneralTrip generalTrip) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "UPDATE transit_system.general_trip SET enter_station = ?, exit_station = ?, trip_segment_ids = ? WHERE general_trip_id = ?")) {
            ps.setString(1, generalTrip.getEnterStation());
            ps.setString(2, generalTrip.getExitStation());
            ps.setArray(3, connection.createArrayOf("integer", generalTrip.getTripSegmentsIDs().toArray()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
