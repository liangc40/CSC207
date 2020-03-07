package edu.toronto.group0162.dao;

import edu.toronto.group0162.entity.*;
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
 * Set TripSegmentDao classes to connect postgreSQL database TripSegmentDao table
 *
 */

@Slf4j
public class TripSegmentDao extends BaseDao<Integer, TripSegment> {

  /**
   * Connect to database trip segment table
   *
   * @param connection
   */
  public TripSegmentDao(Connection connection) {
    super(connection);
  }

  //  @Override
  /**
   * Saves new tripSegment into database tripSegment table
   *
   * @param tripSegment tripSegment
   * @return TripSegment
   */
  public TripSegment save(TripSegment tripSegment) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "INSERT INTO transit_system.tripsegment "
                + "(uid, gtid, cid, starttime, endtime, start, stop, transit_line, fare, cap) "
                + "VALUES (?, ?, ?, ?,?,?,?,?,?,?) RETURNING tsid")) {
      ps.setInt(1, tripSegment.getUid());
      ps.setInt(2, tripSegment.getGtid());
      ps.setInt(3, tripSegment.getCid());
      ps.setString(4, tripSegment.getEnterTime());
      ps.setString(5, tripSegment.getExitTime());
      ps.setString(6, tripSegment.getEntrance());
      ps.setString(7, tripSegment.getExit());
      ps.setString(8, tripSegment.getTransitLine());
      ps.setDouble(9, tripSegment.getFare());
      ps.setDouble(10, tripSegment.getCap());
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      TripSegment newTripSegment = new TripSegment();
      newTripSegment.setEnterTime(tripSegment.getEnterTime());
      newTripSegment.setExitTime(tripSegment.getExitTime());
      newTripSegment.setEntrance(tripSegment.getEntrance());
      newTripSegment.setExit(tripSegment.getExit());
      newTripSegment.setTsid(result.getInt(1));
      newTripSegment.setUid(tripSegment.getUid());
      newTripSegment.setGtid(tripSegment.getGtid());
      newTripSegment.setTransitLine(tripSegment.getTransitLine());
      newTripSegment.setFare(tripSegment.getFare());
      newTripSegment.setCap(tripSegment.getCap());

      return newTripSegment;
    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Updates tripSegment by enter from database tripSegment table
   *
   * @param tripSegment tripSegment
   * @return TripSegment
   */
  public TripSegment updateEnterBus(TripSegment tripSegment) {

    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "UPDATE transit_system.tripsegment SET fare=?, cap=? WHERE tsid = ?")) {
      ps.setDouble(1, tripSegment.getFare());
      ps.setDouble(2, tripSegment.getCap());
      ps.setInt(3, tripSegment.getTsid());
      ps.executeUpdate();
      return tripSegment;
    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Updates tripSegment by exit from database tripSegment table
   *
   * @param tripSegment tripSegment
   * @return TripSegment
   */
  public TripSegment updateExitSubway(TripSegment tripSegment) {

    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "UPDATE transit_system.tripsegment "
                + "SET stop = ?, endtime = ?, fare = ?, cap =? WHERE tsid = ?")) {
      ps.setString(1, tripSegment.getExit());
      ps.setString(2, tripSegment.getExitTime());
      ps.setDouble(3, tripSegment.getFare());
      ps.setDouble(4, tripSegment.getCap());
      ps.setInt(5, tripSegment.getTsid());
      ps.executeUpdate();
      return tripSegment;
    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Updates tripSegment by bus exit from database tripSegment table
   *
   * @param tripSegment tripSegment
   * @return TripSegment
   */
  public TripSegment updateExitBus(TripSegment tripSegment) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "UPDATE transit_system.tripsegment " + "SET stop = ?, endtime = ? WHERE tsid = ?")) {
      ps.setString(1, tripSegment.getExit());
      ps.setString(2, tripSegment.getExitTime());
      ps.setInt(3, tripSegment.getTsid());
      ps.executeUpdate();
      return tripSegment;
    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets latest TripSegment by userID
   *
   * @param uid Integer
   * @return TripSegment
   */
  public TripSegment getLatest(Integer uid) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.tripsegment WHERE uid = ? ORDER BY endtime desc limit 1")) {
      ps.setInt(1, uid);
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      TripSegment latestTripSegment = new TripSegment();
      latestTripSegment.setUid(result.getInt("uid"));
      latestTripSegment.setGtid(result.getInt("gtid"));
      latestTripSegment.setExit(result.getString("stop"));
      return latestTripSegment;

    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets latest TripSegment by userID
   *
   * @param uid Integer
   * @return TripSegment
   */
  public List<TripSegment> getTripSegmentsByUser(Integer uid) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.tripsegment WHERE uid = ?")) {
      ps.setInt(1, uid);
      ResultSet result = ps.executeQuery();

      List tripSegments = new ArrayList<TripSegment>();
      while (result.next()) {
        TripSegment tripSegment = new TripSegment();
        tripSegment.setCid(result.getInt("cid"));
        tripSegment.setExitTime(result.getString("endtime"));
        tripSegment.setFare(result.getDouble("fare"));
        tripSegments.add(tripSegment);
      }
      return tripSegments;

    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets List TripSegments by userID and general trip ID
   *
   * @param uid Integer
   * @param gtid Integer
   * @return List of TripSegment
   */
  public List<TripSegment> getTripSegments(Integer uid, Integer gtid) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.tripsegment WHERE uid = ? and gtid =?")) {
      ps.setInt(1, uid);
      ps.setInt(2, gtid);
      ResultSet result = ps.executeQuery();

      List tripSegments = new ArrayList<TripSegment>();
      while (result.next()) {

        TripSegment newTripSegment = new TripSegment();
        newTripSegment.setEnterTime(result.getString("starttime"));
        newTripSegment.setExitTime(result.getString("endtime"));
        newTripSegment.setEntrance(result.getString("start"));
        newTripSegment.setExit(result.getString("stop"));
        newTripSegment.setTsid(result.getInt("tsid"));
        newTripSegment.setCid(result.getInt("cid"));
        newTripSegment.setUid(result.getInt("uid"));
        newTripSegment.setGtid(result.getInt("gtid"));
        newTripSegment.setTransitLine(result.getString("transit_line"));
        newTripSegment.setFare(result.getDouble("fare"));
        newTripSegment.setCap(result.getDouble("cap"));
        tripSegments.add(newTripSegment);
      }

      return tripSegments;

    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets List TripSegments by endtime order and userID
   *
   * @param uid Integer
   * @return List of TripSegments
   */
  public List<TripSegment> order(Integer uid) {
    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.tripsegment  WHERE uid = ? ORDER BY endtime")) {
      ps.setInt(1, uid);
      ResultSet result = ps.executeQuery();
      List tripSegments = new ArrayList<TripSegment>();
      while (result.next()) {
        TripSegment newTripSegment = new TripSegment();
        newTripSegment.setExitTime(result.getString("endtime"));
        newTripSegment.setFare(result.getDouble("fare"));
        tripSegments.add(newTripSegment);
      }

      return tripSegments;

    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets List TripSegments by endtime order
   *
   * @return List of TripSegments
   */
  public List<TripSegment> order() {
    try (PreparedStatement ps =
                 this.connection.prepareStatement(
                         "SELECT * FROM transit_system.tripsegment ORDER BY endtime")) {
      ResultSet result = ps.executeQuery();
      List tripSegments = new ArrayList<TripSegment>();
      while (result.next()) {
        TripSegment newTripSegment = new TripSegment();
        newTripSegment.setExitTime(result.getString("endtime"));
        newTripSegment.setFare(result.getDouble("fare"));
        tripSegments.add(newTripSegment);
      }

      return tripSegments;

    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  //    public double getRevenueByDay() {
  //        try (PreparedStatement ps = this.connection.prepareStatement
  //                ("SELECT SUM(fare) as fare FROM transit_system.tripsegment " )) {
  //            ResultSet result = ps.executeQuery();
  //            if(result.next()){
  //                return result.getDouble("fare");
  //                }
  //            return 0;
  //        } catch (SQLException ex) {
  //            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
  //        }
  //        return 0;
  //    }

  /**
   * Gets List TripSegments before year
   *
   * @return List of TripSegments
   */
  public List<TripSegment> getBeforeYear(int year) {

    try (PreparedStatement ps =
        this.connection.prepareStatement(
            "SELECT * FROM transit_system.tripsegment where endtime <?")) {
      ps.setString(1, "" + year);
      ResultSet result = ps.executeQuery();
      List tripSegments = new ArrayList<TripSegment>();
      while (result.next()) {
        TripSegment newTripSegment = new TripSegment();
        newTripSegment.setEnterTime(result.getString("starttime"));
        newTripSegment.setExitTime(result.getString("endtime"));
        newTripSegment.setEntrance(result.getString("start"));
        newTripSegment.setExit(result.getString("stop"));
        newTripSegment.setTsid(result.getInt("tsid"));
        newTripSegment.setCid(result.getInt("cid"));
        newTripSegment.setUid(result.getInt("uid"));
        newTripSegment.setGtid(result.getInt("gtid"));
        newTripSegment.setTransitLine(result.getString("transit_line"));
        newTripSegment.setFare(result.getDouble("fare"));
        newTripSegment.setCap(result.getDouble("cap"));
        tripSegments.add(newTripSegment);
      }
      return tripSegments;

    } catch (SQLException ex) {
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Delete TripSegment by its primary key
   *
   * @return List of TripSegments
   */
  public void delete(Integer tsid){
    try(PreparedStatement ps = this.connection.prepareStatement
            ("DELETE FROM transit_system.tripsegment WHERE tsid =?")){
      ps.setInt(1,tsid);
      ps.execute();
    }catch (SQLException ex){
      Logger.getLogger(TripSegmentDao.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
