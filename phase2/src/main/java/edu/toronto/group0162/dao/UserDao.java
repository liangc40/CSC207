package edu.toronto.group0162.dao;

import edu.toronto.group0162.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Set UserDao classes to connect postgreSQL database user table
 *
 */

@Slf4j
public class UserDao extends BaseDao<Integer, User> {

  /**
   * Connect to database user table
   *
   * @param connection
   */
  public UserDao(Connection connection) {
    super(connection);
  }


//  @Override
  /**
   * Gets user from database user table by userID
   *
   * @param uid userID
   * @return User
   */
  public User get(Integer uid) {
    try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.user WHERE uid = ?")) {
      ps.setInt(1, uid);
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      User user = new User();
      user.setUid(result.getInt("uid"));
      user.setAdmin(result.getBoolean("isadmin"));
      user.setEmail(result.getString("email"));
      user.setName(result.getString("name"));
      user.setPassword(result.getString("password"));
      user.setCreateAt(result.getLong("createat"));

      return user;
    } catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

//  @Override
  /**
   * Saves new user into database user table
   *
   * @param user User
   * @return User
   */
  public User save(User user) {

    try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.user (name, email, password, createat,isadmin) VALUES (?, ?, ?, ?,?) RETURNING uid")) {

      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getPassword());
      ps.setLong(4, user.getCreateAt());
      ps.setBoolean(5,user.isAdmin());

      ResultSet result = ps.executeQuery();

      if (!result.next()) {
        return null;
      }

      User newUser = new User();
      newUser.setUid(result.getInt(1));
      newUser.setAdmin(user.isAdmin());
      newUser.setEmail(user.getEmail());
      newUser.setName(user.getName());
      newUser.setCreateAt(user.getCreateAt());
      return newUser;

    } catch (SQLException ex) {
    log.error("save error");
    }
    return null;

  }

  /**
   * Updates user name into database user table
   *
   * @param user User
   * @return User
   */
  public User updateName(User user) {
    //先往数据库里填新的名字，数据库永久保存，不是往JAVA里SET
    try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.user SET name = ? WHERE uid = ?")) {
      ps.setString(1, user.getName());
      ps.setInt(2, user.getUid());
      ps.executeUpdate();
      return user;
    } catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Updates user password into database user table
   *
   * @param user User
   * @return User
   */
  public User updatePassword(User user) {
    try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.user SET password = ? WHERE uid = ?")) {
      ps.setString(1, user.getPassword());
      ps.setInt(2, user.getUid());
      ps.executeUpdate();
      return user;
    } catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Updates user to be admin into database user table
   *
   * @param user User
   * @return User
   */
  public User updateAdmin(User user) {

    try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.user SET isadmin = ? WHERE uid = ?")) {
      ps.setBoolean(1, user.isAdmin());
      ps.setInt(2, user.getUid());
      ps.executeUpdate();
      return user;
    } catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets user by email in database user table
   *
   * @param email Email
   * @return User
   */
  public User get(String email) {
    try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.user WHERE email = ?")) {
      ps.setString(1, email);
      ResultSet result = ps.executeQuery();

      if (!result.next()) {
        return null;
      }
      User user = new User();

      user.setUid(result.getInt("uid"));
      user.setAdmin(result.getBoolean("isadmin"));
      user.setEmail(result.getString("email"));
      user.setName(result.getString("name"));
      user.setCreateAt(result.getLong("createat"));

      return user;
    } catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Authenticate user login's email and password correct
   *
   * @param email Email
   * @param password Password
   * @return boolean
   */
  public boolean authenticate(String email, String password) {
    try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.user WHERE email = ? and password = ?")) {

      ps.setString(1, email);
      ps.setString(2,password);

      ResultSet result = ps.executeQuery();

      return result.next();
    } catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }

    return false;
  }

  /**
   * Get all user from database user table
   *
   * @return List of User
   */
  public List<User> getUsers() {
    try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.user")) {
      ResultSet result = ps.executeQuery();
      List users = new ArrayList<User>();
      while (result.next()) {
        User user = new User();
        user.setAdmin(result.getBoolean("isadmin"));
        users.add(user);}

      return users;

    } catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Get admin user from database user table
   *
   * @return User
   */
  public User getAdmin(){

    try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.user WHERE isadmin = true")){
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      User user = new User();
      user.setUid(result.getInt("uid"));
      user.setAdmin(result.getBoolean("isadmin"));
      user.setEmail(result.getString("email"));
      user.setName(result.getString("name"));
      user.setCreateAt(result.getLong("createat"));

      return user;
    }
    catch (SQLException ex) {
      Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;



  }
}
