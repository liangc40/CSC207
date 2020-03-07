package edu.toronto.group0162.dao;

import edu.toronto.group0162.entity.Card;

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
 * Set CardDao classes to connect postgreSQL database card table
 *
 */

public class CardDao extends edu.toronto.group0162.dao.BaseDao<Integer, Card> {

  /**
   * Connect to database card table
   *
   * @param connection
   */
  public CardDao(Connection connection) {
    super(connection);
  }

//  @Override
  /**
   * Gets card from database card table by cardID
   *
   * @param cid cardID
   * @return Card
   */
  public Card get(Integer cid) {
    try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.card WHERE cid = ? and deleted = ?")) {
      ps.setInt(1, cid);
      ps.setBoolean(2,false);
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      Card card = new Card();
      card.setActive(result.getBoolean("isactive"));
      card.setBalance(result.getDouble("balance"));
      card.setCid(result.getInt("cid"));
      card.setUid(result.getInt("uid"));
      card.setDeleted(result.getBoolean("deleted"));
      card.setCreateAt(result.getLong("createat"));

      return card;
    } catch (SQLException ex) {
      Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Gets undeleted cards from database
   *
   * @param uid userID
   * @return List of Cards
   */
  public List<Card> getByUid(Integer uid) {
    try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.card WHERE uid = ? and deleted=?")) {
      ps.setInt(1, uid);
      ps.setBoolean(2,false);
      ResultSet result = ps.executeQuery();

      List s = new ArrayList<Card>();
      while (result.next()) {
        Card card = new Card();
        card.setActive(result.getBoolean("isactive"));
        card.setBalance(result.getDouble("balance"));
        card.setCid(result.getInt("cid"));
        card.setUid(result.getInt("uid"));
        card.setDeleted(result.getBoolean("deleted"));
        s.add(card);
      }

      return s;

    } catch (SQLException ex) {
      Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }


//  @Override
    /**
   * Saves new card into database card table
   *
   * @param card cardID
   * @return Card
   */
  public Card save(Card card) {
    try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.card (uid, isActive, balance, createat,deleted) VALUES (?, ?, ?, ?,?) RETURNING cid")) {
      ps.setInt(1, card.getUid());
      ps.setBoolean(2, card.isActive());
      ps.setDouble(3, card.getBalance());
      ps.setLong(4, card.getCreateAt());
      ps.setBoolean(5,card.isDeleted());
      ResultSet result = ps.executeQuery();
      if (!result.next()) {
        return null;
      }
      Card newCard = new Card();
      newCard.setActive(card.isActive());
      newCard.setBalance(card.getBalance());
      newCard.setCid(result.getInt(1));
      newCard.setUid(card.getUid());
      newCard.setDeleted(card.isDeleted());
      newCard.setCreateAt(card.getCreateAt());

      return newCard;
    } catch (SQLException ex) {
      Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Updates card information into database card table
   *
   * @param card card
   * @return Card
   */
  public Card update(Card card) {
    try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.card SET balance = ?, isactive = ?, deleted=? WHERE cid = ?")) {
      ps.setDouble(1, card.getBalance());
      ps.setBoolean(2, card.isActive());
      ps.setBoolean(3,card.isDeleted());
      ps.setInt(4, card.getCid());
      ps.executeUpdate();
      return card;
    } catch (SQLException ex) {
      Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  /**
   * Sets card deleted in database card table
   * @param card card
   * @return Card
   */
  public Card delete(Card card) {
    try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.card SET deleted=? WHERE cid = ?")) {
      ps.setBoolean(1, true);
      ps.setInt(2, card.getCid());
      ps.executeUpdate();
      return card;
    } catch (SQLException ex) {
      Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }


}
