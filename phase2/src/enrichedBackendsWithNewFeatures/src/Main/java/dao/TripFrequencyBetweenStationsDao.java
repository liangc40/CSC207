package dao;

import table.OneWayTripFrequency;
import table.TotalFareByCity;
import table.TripFrequencyBetweenStations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A TripFrequencyBetweenStationsDao accessing the trip frequency between stations in the database. */
public class TripFrequencyBetweenStationsDao extends dao.BaseDao<String, TripFrequencyBetweenStations> {

    /**
     * Construct the TripFrequencyBetweenStationsDao.
     *
     * @param connection the Connection that connects the database.
     */
    public TripFrequencyBetweenStationsDao(Connection connection) {
        super(connection);
    }

    /**
     * Return TripFrequencyBetweenStations with information that matches the entry of int pastTripManagerID and
     * String routeName in the database.
     *
     * @param pastTripManagerID the pastTripManager ID to search for in the database
     * @param routeName the route name to search for in the database
     * @return TripFrequencyBetweenStations
     */
    public TripFrequencyBetweenStations get(int pastTripManagerID, String routeName) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.trip_frequency_between_stations_table WHERE past_trip_manager_id = ? AND route_name = ?")) {
            ps.setInt(1, pastTripManagerID);
            ps.setString(2, routeName);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TripFrequencyBetweenStations tripFrequencyBetweenStations = new TripFrequencyBetweenStations();
            tripFrequencyBetweenStations.setID(result.getInt(result.getInt("id")));
            tripFrequencyBetweenStations.setPastTripManagerID(result.getInt("past_trip_manager_id"));
            tripFrequencyBetweenStations.setRouteName(result.getString("route_name"));
            tripFrequencyBetweenStations.setTimes(result.getInt("times"));
            return tripFrequencyBetweenStations;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Save the TripFrequencyBetweenStations tripFrequencyBetweenStations to the database and return
     * the TripFrequencyBetweenStations.
     *
     * @param tripFrequencyBetweenStations the TripFrequencyBetweenStations to be saved
     * @return the TripFrequencyBetweenStations just saved
     */
    public TripFrequencyBetweenStations save(TripFrequencyBetweenStations tripFrequencyBetweenStations) {
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.trip_frequency_between_stations_table (past_trip_manager_id, route_name, times) VALUES (?, ?, ?) RETURNING id")) {
            ps.setInt(1, tripFrequencyBetweenStations.getPastTripManagerID());
            ps.setString(2, tripFrequencyBetweenStations.getRouteName());
            ps.setInt(3, tripFrequencyBetweenStations.getTimes());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TripFrequencyBetweenStations newTripFrequencyBetweenStations = new TripFrequencyBetweenStations();
            newTripFrequencyBetweenStations.setID(result.getInt(1));
            newTripFrequencyBetweenStations.setPastTripManagerID(tripFrequencyBetweenStations.getPastTripManagerID());
            newTripFrequencyBetweenStations.setRouteName(tripFrequencyBetweenStations.getRouteName());
            newTripFrequencyBetweenStations.setTimes(tripFrequencyBetweenStations.getTimes());
            return newTripFrequencyBetweenStations;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the TripFrequencyBetweenStations in the database which has the same id as the
     * TripFrequencyBetweenStations tripFrequencyBetweenStations.
     *
     * @param tripFrequencyBetweenStations the TripFrequencyBetweenStations that the update is based on
     */
    public void update(TripFrequencyBetweenStations tripFrequencyBetweenStations) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.trip_frequency_between_stations_table SET times = ? WHERE id = ?")) {
            ps.setInt(1, tripFrequencyBetweenStations.getTimes());
            ps.setInt(2, tripFrequencyBetweenStations.getID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return true if and only if trip_frequency_between_stations has an entry whose id is 1, false otherwise.
     *
     * @return whether trip_frequency_between_stations has an entry whose id is 1
     */
    public Boolean checkEmpty() {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.trip_frequency_between_stations WHERE id = 1")) {
            ResultSet result = ps.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
