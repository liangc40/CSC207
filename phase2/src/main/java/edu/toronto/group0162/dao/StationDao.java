package edu.toronto.group0162.dao;

import edu.toronto.group0162.entity.Node;
import edu.toronto.group0162.entity.Station;
import edu.toronto.group0162.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Set StationDao classes to connect postgreSQL database station table
 *
 */

public class StationDao extends BaseDao<Integer, Station> {

    /**
     * Connect to database station table
     *
     * @param connection
     */
    public StationDao(Connection connection) {
        super(connection);
    }

    /**
     * Gets all stations from database station table
     *
     * @return List of stations
     */
    public List<Station> getStations(String line) {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.station WHERE line = ?")) {
            ps.setString(1, line);
            ResultSet result = ps.executeQuery();
            List s = new ArrayList<Node>();
            while (result.next()) {
                Station station = new Station();
                station.setSid(result.getInt("sid"));
                station.setPoint(result.getInt("point"));
                station.setName(result.getString("stationname"));
                station.setNext_distance(result.getInt("next_distance"));
                station.setLine(result.getString("line"));

                s.add(station);
            }
            return s;

        } catch (SQLException ex) {
            Logger.getLogger(StationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Gets station from database station table by transit name and station name
     *
     * @param transitName String
     * @param stationName String
     * @return Station
     */
    public Station getStation(String transitName,String stationName) {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.station WHERE line = ? and stationname =?")) {
            ps.setString(1, transitName);
            ps.setString(2,stationName);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }

            Station station = new Station();
            station.setSid(result.getInt("sid"));
            station.setPoint(result.getInt("point"));
            station.setName(result.getString("stationname"));
            station.setNext_distance(result.getDouble("next_distance"));
            station.setLine(result.getString("line"));

            return station;

        } catch (SQLException ex) {
            Logger.getLogger(StationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
