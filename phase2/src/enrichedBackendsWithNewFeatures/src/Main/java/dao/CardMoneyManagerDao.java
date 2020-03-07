package dao;
import entity.Card;
import manager.CardMoneyManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A CardMoneyManagerDao accessing the card money managers in the database. */
public class CardMoneyManagerDao extends BaseDao<Integer, CardMoneyManager> {

    /**
     * Construct the CardMoneyManagerDao.
     *
     * @param connection the Connection that connects the database.
     */
    public CardMoneyManagerDao(Connection connection) {super(connection); }

    /**
     * Return the CardMoneyManager with information that matches the entry of Integer cardMoneyManager ID
     * in the database.
     *
     * @param cardMoneyManagerID the ID to search for the card money manager in the database
     * @return a CardMoneyManager with corresponding information
     */
    public CardMoneyManager get(Integer cardMoneyManagerID) {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.card_money_manager WHERE card_money_manager_id = ?")) {
            ps.setInt(1, cardMoneyManagerID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            CardMoneyManager cardMoneyManager = new CardMoneyManager();
            cardMoneyManager.setCardMoneyManagerID(result.getInt("card_money_manager_id"));
            cardMoneyManager.setCardID(result.getInt("card_id"));
            cardMoneyManager.setBalance(result.getBigDecimal("balance"));
            cardMoneyManager.setAutomaticLoadAmount(result.getBigDecimal("automatic_load_amount"));
            cardMoneyManager.setRegistrationCity(result.getString("registration_city"));
            return cardMoneyManager;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, "Cannot get data from card manager table.", exception);
        }
        return null;
    }

    /**
     * Save the CardMoneyManager cardMoneyManager to the database and return the Card.
     *
     * @param cardMoneyManager the CardMoneyManager to be saved
     * @return the CardMoneyManager just saved
     */
    public CardMoneyManager save(CardMoneyManager cardMoneyManager) {
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.card_money_manager (card_id, balance, automatic_load_amount, registration_city) VALUES (?, ?, ?, ?) RETURNING card_money_manager_id")) {
            ps.setInt(1, cardMoneyManager.getCardID());
            ps.setBigDecimal(2, cardMoneyManager.getBalance());
            ps.setBigDecimal(3, cardMoneyManager.getAutomaticLoadAmount());
            ps.setString(4, cardMoneyManager.getRegistrationCity());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            CardMoneyManager newCardMoneyManager = new CardMoneyManager();
            newCardMoneyManager.setCardMoneyManagerID(result.getInt(1));
            newCardMoneyManager.setCardID(cardMoneyManager.getCardID());
            newCardMoneyManager.setBalance(cardMoneyManager.getBalance());
            newCardMoneyManager.setAutomaticLoadAmount(cardMoneyManager.getAutomaticLoadAmount());
            newCardMoneyManager.setRegistrationCity(cardMoneyManager.getRegistrationCity());
            return newCardMoneyManager;

        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the CardMoneyManager in the database which has the same id as the CardMoneyManager bean.
     *
     * @param bean the CardMoneyManager that the update is based on
     */
    public void update(CardMoneyManager bean) {
        //先往数据库里填新的名字，数据库永久保存，不是往JAVA里SET
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.card_money_manager SET balance = ?, automatic_load_amount = ? WHERE card_money_manager_id = ?")) {
//      ps.setDouble(1, bean.getBalance());
            ps.setBigDecimal(1, bean.getBalance());
            ps.setBigDecimal(2, bean.getAutomaticLoadAmount());
            ps.setInt(3, bean.getCardMoneyManagerID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
