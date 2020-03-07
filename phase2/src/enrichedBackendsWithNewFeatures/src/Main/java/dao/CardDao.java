package dao;

import entity.Card;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A CardDao accessing the cards in the database. */
public class CardDao extends dao.BaseDao<Integer, Card> {

    /**
     * Construct the CardDao.
     *
     * @param connection the Connection that connects the database.
     */
    public CardDao(Connection connection) {
        super(connection);
    }

    /**
     * Return the Card with information that matches the entry of Integer cardID in the database.
     *
     * @param cardID the ID to search for the card in the database
     * @return a Card with corresponding information
     */
    public Card get(Integer cardID) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement("SELECT * FROM transit_system.card WHERE card_id = ?")) {
            ps.setInt(1, cardID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            Card card = new Card();
            card.setCardID(result.getInt("card_id"));
            card.setUserID(result.getInt("user_id"));
            card.setPastTripManagerID(result.getInt("past_trip_manager_id"));
            card.setCardMoneyManagerID(result.getInt("card_money_manager_id"));
            Array transitPassIDArray = result.getArray("transit_pass_ids");
            ArrayList<Integer> transitPassIDArrayList =
                    new ArrayList<>(Arrays.asList((Integer[]) transitPassIDArray.getArray()));
            card.setTransitPassesIDs(transitPassIDArrayList);
            card.setActive(result.getBoolean("is_active"));
            return card;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName())
                    .log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Delete the card with Integer cardID in the database. This method returns nothing.
     *
     * @param cardID the ID to search for the card to delete
     */
    public void delete(Integer cardID) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement("DELETE FROM transit_system.card WHERE card_id = ?")) {
            ps.setInt(1, cardID);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the Card card to the database and return the Card.
     *
     * @param card the Card to be saved
     * @return the Card just saved
     */
    public Card save(Card card) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "INSERT INTO transit_system.card (user_id, card_money_manager_id, past_trip_manager_id,\n" +
                                     " transit_pass_ids, is_active, registration_time) VALUES (?, ?, ?, ?, ?, ?) RETURNING card_id,\n" +
                                     " registration_time")) {
            ps.setInt(1, card.getUserID());
            ps.setInt(2, card.getCardMoneyManagerID());
            ps.setInt(3, card.getPastTripManagerID());
            ps.setArray(4, connection.createArrayOf("integer", card.getTransitPassesIDs().toArray()));
            ps.setBoolean(5, card.isActive());
            ps.setLong(6, Instant.now().getEpochSecond());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            Card newCard = new Card();
            newCard.setCardID(result.getInt(1));
            newCard.setUserID(card.getUserID());
            newCard.setCardMoneyManagerID(card.getCardMoneyManagerID());
            newCard.setPastTripManagerID(card.getPastTripManagerID());
            newCard.setTransitPassesIDs(card.getTransitPassesIDs());
            newCard.setActive(card.isActive());
            newCard.setRegistrationTime(result.getLong(2));
            return newCard;

        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the Card in the database which has the same id as the Card bean.
     *
     * @param bean the Card that the update is based on
     */
    public void update(Card bean) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "UPDATE transit_system.card SET is_active = ?, transit_pass_ids = ? WHERE card_id = ?")) {
            ps.setBoolean(1, bean.isActive());
            ps.setArray(2, connection.createArrayOf("integer", bean.getTransitPassesIDs().toArray()));
            ps.setInt(3, bean.getCardID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
