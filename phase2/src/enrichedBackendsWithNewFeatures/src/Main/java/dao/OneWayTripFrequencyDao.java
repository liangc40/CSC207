package dao;

import table.OneWayTripFrequency;
import table.TotalFareByCity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OneWayTripFrequencyDao extends dao.BaseDao<String, OneWayTripFrequency> {

    public OneWayTripFrequencyDao(Connection connection) {
        super(connection);
    }

    public OneWayTripFrequency get(int pastTripManagerID, String routeName) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.one_way_trip_frequency_table WHERE past_trip_manager_id = ? AND route_name = ?")) {
            ps.setInt(1, pastTripManagerID);
            ps.setString(2, routeName);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            OneWayTripFrequency oneWayTripFrequency = new OneWayTripFrequency();
            oneWayTripFrequency.setID(result.getInt("id"));
            oneWayTripFrequency.setPastTripManagerID(result.getInt("past_trip_manager_id"));
            oneWayTripFrequency.setRouteName(result.getString("route_name"));
            oneWayTripFrequency.setTimes(result.getInt("times"));
            return oneWayTripFrequency;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public OneWayTripFrequency save(OneWayTripFrequency oneWayTripFrequency) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.one_way_trip_frequency_table (past_trip_manager_id, route_name, times) VALUES (?, ?, ?) ")) {
            ps.setInt(1, oneWayTripFrequency.getPastTripManagerID());
            ps.setString(2, oneWayTripFrequency.getRouteName());
            ps.setInt(3, oneWayTripFrequency.getTimes());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            OneWayTripFrequency newOneWayTripFrequency = new OneWayTripFrequency();
            newOneWayTripFrequency.setID(result.getInt(1));
            newOneWayTripFrequency.setPastTripManagerID(oneWayTripFrequency.getPastTripManagerID());
            newOneWayTripFrequency.setRouteName(oneWayTripFrequency.getRouteName());
            newOneWayTripFrequency.setTimes(oneWayTripFrequency.getTimes());
            return newOneWayTripFrequency;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    public void update(OneWayTripFrequency oneWayTripFrequency) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "UPDATE transit_system.one_way_trip_frequency_table SET times = ? WHERE id = ?")) {
            ps.setInt(1, oneWayTripFrequency.getTimes());
            ps.setInt(2, oneWayTripFrequency.getID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean checkEmpty() {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.one_way_trip_frequency_table WHERE id = 1")) {
            ResultSet result = ps.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}