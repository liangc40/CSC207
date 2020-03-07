package dao;

import entity.AdminUser;
import entity.Card;
import entity.TransitPass;
import lombok.Getter;
import manager.DailyReportMoneyManager;

import java.sql.*;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A TransitPassDao accessing the transit passes in the database. */
public class TransitPassDao extends dao.BaseDao<Integer, TransitPass> {
    /**
     * Construct the TransitPassDao.
     *
     * @param connection the Connection that connects the database.
     */
    public TransitPassDao(Connection connection) {
        super(connection);
    }

    @Getter
    private DailyReportMoneyManagerDao dailyReportMoneyManagerDao;
    private DailyReportDao dailyReportDao;
    private AdminUserDao adminUserDao;


    /**
     * Return a TransitPass with information that matches the entry of Integer transitPassID in the
     * database.
     *
     * @param transitPassID the transit pass ID to search for in the database
     * @return a corresponding TransitPass of the id
     */
    public TransitPass get(Integer transitPassID) {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.transit_pass WHERE transit_pass_id = ?")) {
            ps.setInt(1, transitPassID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TransitPass transitPass = new TransitPass();
            transitPass.setTransitPassID(result.getInt("transit_pass_id"));
            transitPass.setCardID(result.getInt("transit_pass_id"));
            transitPass.setCity(result.getString("city"));
            transitPass.setRegistrationDate(result.getLong("registration_date"));
            transitPass.setExpirationDate(result.getString("expiration_date"));
            transitPass.setChargeAmount(result.getBigDecimal("charge_amount"));
            return transitPass;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Delete the TransitPass with int transitPassID in the database. This method returns nothing.
     *
     * @param transitPassID the transit pass ID to search for
     */
    public void delete(int transitPassID) {
        try (PreparedStatement ps = this.connection.prepareStatement("DELETE FROM transit_system.transit_pass WHERE transit_pass_id = ?")) {
            ps.setInt(1, transitPassID);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the TransitPass transitPass to the database and return the TransitPass.
     *
     * @param transitPass the TransitPass to be saved
     * @return the TransitPass just saved
     */
    public TransitPass save(TransitPass transitPass) {
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.transit_pass (card_id, city, registration_date, expiration_date, charge_amount) VALUES (?, ?, ?, ?, ?) RETURNING transit_pass_id, registration_date")) {
            ps.setInt(1, transitPass.getCardID());
            ps.setString(2, transitPass.getCity());
            ps.setLong(3, Instant.now().getEpochSecond());
            ps.setString(4, transitPass.getExpirationDate());
            ps.setBigDecimal(5, transitPass.getChargeAmount());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            TransitPass newTransitPass = new TransitPass();
            newTransitPass.setTransitPassID(result.getInt(1));
            newTransitPass.setCardID(transitPass.getCardID());
            newTransitPass.setRegistrationDate(result.getLong(2));
            newTransitPass.setExpirationDate(transitPass.getExpirationDate());
            newTransitPass.setCity(transitPass.getCity());
            newTransitPass.setChargeAmount(transitPass.getChargeAmount());
            return newTransitPass;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the TransitPass in the database which has the same id as the TransitPass transitPass.
     *
     * @param transitPass the TransitPass that the update is based on
     */
    public void update(TransitPass transitPass) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.transit_pass SET expiration_date = ?, charge_amount = ? WHERE transit_pass_id = ?")) {
            ps.setString(1, transitPass.getExpirationDate());
            ps.setBigDecimal(2, transitPass.getChargeAmount());
            ps.setInt(3, transitPass.getTransitPassID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
