package dao;

import entity.Card;
import entity.TripSegment;

import java.sql.*;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A TripSegmentDao accessing the trip segments in the database. */
public class TripSegmentDao extends dao.BaseDao<Integer, TripSegment> {
    public TripSegmentDao(Connection connection) {
        super(connection);
    }

    /**
     * Construct the TripSegmentDao.
     *
     * @param connection the Connection that connects the database.
     */
    public TripSegment get(Integer tripSegmentID) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.trip_segment WHERE trip_segment_id = ?")) {
            ps.setInt(1, tripSegmentID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TripSegment tripSegment = new TripSegment();
            tripSegment.setTripSegmentID(result.getInt("trip_segment_id"));
            tripSegment.setCardID(result.getInt("card_id"));
            tripSegment.setCity(result.getString("city"));
            tripSegment.setEnterStation(result.getString("enter_station"));
            tripSegment.setEnterTime(result.getLong("enter_time"));
            tripSegment.setExitStation(result.getString("exit_station"));
            tripSegment.setExitTime(result.getLong("exit_time"));
            tripSegment.setPayAtEntrance(result.getBoolean("pay_at_entrance"));
            tripSegment.setStopsTraveled(result.getBigDecimal("stops_traveled"));
            tripSegment.setDistanceTraveled(result.getBigDecimal("distance_traveled"));
            return tripSegment;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Save the TripSegment to the database and return the TripSegment.
     *
     * @return the TripSegment just saved
     */
    public TripSegment save() {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.trip_segment (card_id) VALUES (0) RETURNING trip_segment_id")) {
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TripSegment newTripSegment = new TripSegment();
            newTripSegment.setTripSegmentID(result.getInt(1));
            return newTripSegment;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Return a TripSegment which has the same id as the TripSegment tripSegment and update the TripSegment in the
     * database at entrance.
     *
     * @param tripSegment the TripSegment that the update is based on
     */
    public TripSegment updateInformationAtEntrance(TripSegment tripSegment) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "UPDATE transit_system.trip_segment \n"
                                     + " SET enter_time = ?, enter_station = ?, card_id = ?, city = ?, traffic_mode = ?, pay_at_entrance = ? \n"
                                     + " WHERE trip_segment_id = ?")) {
            ps.setLong(1, Instant.now().getEpochSecond());
            ps.setString(2, tripSegment.getEnterStation());
            ps.setInt(3, tripSegment.getCardID());
            ps.setString(4, tripSegment.getCity());
            ps.setString(5, tripSegment.getTrafficMode());
            ps.setBoolean(6, tripSegment.getPayAtEntrance());
            ps.setInt(7, tripSegment.getTripSegmentID());
            ps.executeUpdate();
            tripSegment.setEnterTime(this.get(tripSegment.getTripSegmentID()).getEnterTime());
            return tripSegment;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Update the TripSegment in the database which has the same id as the TripSegment tripSegment at exit.
     *
     * @param tripSegment the TripSegment that the update is based on
     */
    public void updateInformationAtExit(TripSegment tripSegment) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "UPDATE transit_system.trip_segment \n"
                                     + " SET exit_time = ?, exit_station = ?, stops_traveled = ?, distance_traveled = ? \n" +
                                     " WHERE trip_segment_id = ?")) {
            ps.setLong(1, Instant.now().getEpochSecond());
            ps.setString(2, tripSegment.getExitStation());
            ps.setBigDecimal(3, tripSegment.getStopsTraveled());
            ps.setBigDecimal(4, tripSegment.getDistanceTraveled());
            ps.setInt(5, tripSegment.getTripSegmentID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
