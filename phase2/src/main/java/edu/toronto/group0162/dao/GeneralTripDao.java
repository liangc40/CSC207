package edu.toronto.group0162.dao;

import com.sun.tools.javah.Gen;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.GeneralTrip;
import edu.toronto.group0162.entity.TripSegment;
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
 * Set GeneralTripDao classes to connect postgreSQL database GeneralTrip table
 *
 */
public class GeneralTripDao extends BaseDao<Integer, GeneralTripDao> {

  /**
   * Connect to database general trip table
   *
   * @param connection
   */
  public GeneralTripDao(Connection connection) {
    super(connection);
  }

  /**
   * Saves new generalTrip into database generaltrip table
   *
   * @param generalTrip GeneralTrip
   * @return GeneralTrip
   */
  public GeneralTrip save(GeneralTrip generalTrip) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "INSERT INTO transit_system.generaltrip "
                + "(uid, starttime, finished) "
                + "VALUES (?, ?, ?) RETURNING gtid")) {
      ps.setInt(1, generalTrip.getUid());
      ps.setString(2, generalTrip.getStartTime());
      ps.setBoolean(3, generalTrip.isFinished());

      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      GeneralTrip newGeneralTrip = new GeneralTrip();
      newGeneralTrip.setStartTime(newGeneralTrip.getStartTime());
      newGeneralTrip.setGtid(result.getInt(1));
      newGeneralTrip.setUid(newGeneralTrip.getUid());
      newGeneralTrip.setFinished(false);

      return newGeneralTrip;
    } catch (SQLException ex) {
      Logger.getLogger(GeneralTripDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Updates new generalTrip to be finished
   *
   * @param generalTrip GeneralTrip
   */
  public GeneralTrip updateFinished(GeneralTrip generalTrip) {

    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "UPDATE transit_system.generaltrip SET finished = ? WHERE gtid = ?")) {
      ps.setBoolean(1, generalTrip.isFinished());
      ps.setInt(2, generalTrip.getGtid());
      ps.executeUpdate();
      return generalTrip;
    } catch (SQLException ex) {
      Logger.getLogger(GeneralTripDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets generalTrip by userID
   *
   * @param uid Integer
   * @param finished Boolean
   */
  public GeneralTrip get(Integer uid, Boolean finished) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.generaltrip WHERE uid = ? and finished = ?")) {
      ps.setInt(1, uid);
      ps.setBoolean(2, finished);
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }

      GeneralTrip newGeneralTrip = new GeneralTrip();
      newGeneralTrip.setUid(result.getInt("uid"));
      newGeneralTrip.setGtid(result.getInt("gtid"));
      newGeneralTrip.setFinished(result.getBoolean("finished"));
      newGeneralTrip.setStartTime(result.getString("starttime"));
      return newGeneralTrip;

    } catch (SQLException ex) {
      Logger.getLogger(GeneralTripDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

//  public void delete() {
//    try (PreparedStatement ps =
//        this.connection.prepareStatement(
//            "DELETE FROM transit_system.generaltrip WHERE starttime < '2011-09-21 08:21:22'")) {
//      ps.execute();
//    } catch (SQLException ex) {
//      Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
//    }
//  }

  /**
   * Deletes generalTrip by generalTripID
   *
   * @param gtid Integer
   */
  public void delete(Integer gtid){
    try(PreparedStatement ps = this.connection.prepareStatement
            ("DELETE FROM transit_system.generaltrip WHERE gtid =?")){
      ps.setInt(1,gtid);
      ps.execute();
    }catch (SQLException ex){
      Logger.getLogger(GeneralTripDao.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Gets latest generalTrip by userID
   *
   * @param uid Integer
   * @return
   */
  public List<GeneralTrip> getLatest(Integer uid) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.generaltrip WHERE uid = ? ORDER BY starttime DESC limit 3")) {
      ps.setInt(1, uid);
      ResultSet result = ps.executeQuery();
      //            if (!result.next()) {
      //                return null;
      //            }
      List generalTrips = new ArrayList<GeneralTrip>();
      while (result.next()) {

        GeneralTrip newGeneralTrip = new GeneralTrip();
        newGeneralTrip.setUid(result.getInt("uid"));
        newGeneralTrip.setGtid(result.getInt("gtid"));
        newGeneralTrip.setFinished(result.getBoolean("finished"));
        newGeneralTrip.setStartTime(result.getString("starttime"));
        generalTrips.add(newGeneralTrip);
      }

      return generalTrips;

    } catch (SQLException ex) {
      Logger.getLogger(GeneralTripDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets generalTrip before a year
   *
   * @param year Integer
   * @return List of GeneralTrips
   */
  public List<GeneralTrip> getBeforeYear(int year) {

    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.generaltrip where starttime <?")) {
      ps.setString(1, "" + year);
      ResultSet result = ps.executeQuery();
      List generalTrips = new ArrayList<GeneralTrip>();
      while (result.next()) {

        GeneralTrip newGeneralTrip = new GeneralTrip();
        newGeneralTrip.setUid(result.getInt("uid"));
        newGeneralTrip.setGtid(result.getInt("gtid"));
        newGeneralTrip.setFinished(result.getBoolean("finished"));
        newGeneralTrip.setStartTime(result.getString("starttime"));
        generalTrips.add(newGeneralTrip);
      }

      return generalTrips;

    } catch (SQLException ex) {
      Logger.getLogger(GeneralTripDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}
