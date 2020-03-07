package dao;

import entity.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A UserDao accessing the users in the database. */
@Slf4j
public class UserDao extends BaseDao<Integer, User> {

    /**
     * Construct the UserDao.
     *
     * @param connection the Connection that connects the database.
     */
    public UserDao(Connection connection) {
        super(connection);
    }

    /**
     * Return a User with information that matches the entry of Integer userID in the
     * database.
     *
     * @param userID the user ID to search for in the database
     * @return a corresponding user of the id
     */
    public User get(Integer userID) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement("SELECT * FROM transit_system.user WHERE user_id = ?")) {
            ps.setInt(1, userID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            User user = new User();
            return packageUser(user, result);
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Return a User with information matching the entry of String email in the database.
     *
     * @param email the user ID to search for in the database
     * @return a corresponding user of the email
     */
    public User getByEmail(String email) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement("SELECT * FROM transit_system.user WHERE user_id = ?")) {
            ps.setString(1, email);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            User user = new User();
            packageUser(user, result);
            return user;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Return a User with information matching the entry of ResultSet result in the database.
     *
     * @param user the User ID to search for in the database
     * @param result the ResultSet to package the User
     * @return a corresponding user
     */
    private User packageUser(User user, ResultSet result) throws SQLException {
        user.setUserID(result.getInt("user_id"));
        user.setName(result.getString("name"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
        user.setBirthday(result.getString("birthday"));
        user.setTrafficStationNearHome(result.getString("traffic_station_near_home"));
        user.setTrafficStationNearWork(result.getString("traffic_station_near_work"));
        user.setRegistrationTime(result.getLong("registration_time"));
        user.setPastTripManagerID(result.getInt("past_trip_manager_id"));
        user.setUserMoneyManagerID(result.getInt("user_money_manager_id"));
        Array cardIDArray = result.getArray("card_ids");
        if (cardIDArray != null) {
            ArrayList<Integer> cardIDArrayList =
                    new ArrayList<>(Arrays.asList((Integer[]) cardIDArray.getArray()));
            user.setCardIDs(cardIDArrayList);
        }
        return user;
    }

    /**
     * Save the User to the database and return the User.
     *
     * @param user the User to be saved
     * @return the User just saved
     */
    public User save(User user) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.user \n"
                                     + "(name, email, password, registration_time, birthday, traffic_station_near_home, \n"
                                     + "traffic_station_near_work, past_trip_manager_id, user_money_manager_id) \n"
                                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING user_id, registration_time")) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setLong(4, Instant.now().getEpochSecond());
            ps.setString(5, user.getBirthday());
            ps.setString(6, user.getTrafficStationNearHome());
            ps.setString(7, user.getTrafficStationNearWork());
            ps.setInt(8, user.getPastTripManagerID());
            ps.setInt(9, user.getUserMoneyManagerID());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            User newUser = new User();
            newUser.setUserID(result.getInt(1));
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setRegistrationTime(result.getLong(2));
            return newUser;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    public boolean authenticate(String email, String password) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.user WHERE email = ? and password = ?")) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet result = ps.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Update the User in the database which has the same id as the
     * User user.
     *
     * @param user the user that the update is based on
     */
    public void update(User user) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "UPDATE transit_system.user SET card_ids = ?, name = ?, traffic_station_near_home = ? , traffic_station_near_work = ? WHERE user_id = ?")) {
            ps.setArray(1, connection.createArrayOf("integer", user.getCardIDs().toArray()));
            ps.setString(2, user.getName());
            ps.setString(3, user.getTrafficStationNearHome());
            ps.setString(4, user.getTrafficStationNearWork());
            ps.setInt(5, user.getUserID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
