package dao;

import entity.ChildrenDiscount;
import entity.TransitPass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A ChildrenDiscountDao accessing the children discount information in the database. */
public class ChildrenDiscountDao extends BaseDao <Integer, ChildrenDiscount>{

    /**
     * Construct the ChildrenDiscountDao.
     *
     * @param connection the Connection that connects the database.
     */
    public ChildrenDiscountDao(Connection connection) {
        super(connection);
    }

    /**
     * Return the ChildrenDiscount with information that matches the entry of Integer childrenDiscountID
     * in the database.
     *
     * @param childrenDiscountID the ID to search for the children discount information in the database
     * @return a ChildrenDiscount with corresponding information
     */
    public ChildrenDiscount get(Integer childrenDiscountID) {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.children_discount WHERE children_discount_id = ?")) {
            ps.setInt(1, childrenDiscountID);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            ChildrenDiscount childrenDiscount = new ChildrenDiscount();
            childrenDiscount.setChildrenDiscountID(result.getInt("children_discount_id"));
            childrenDiscount.setDiscountRate(result.getBigDecimal("discount_rate"));
            childrenDiscount.setExpirationDate(result.getString("expiration_date"));
            childrenDiscount.setUserID(result.getInt("user_id"));
            return childrenDiscount;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    public ArrayList<Integer> getByDate(String expirationDate) {
        ArrayList<Integer> result = new ArrayList<>();
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.children_discount WHERE expiration_date = ?")) {
            ps.setString(1, expirationDate);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {
                result.add(resultSet.getInt(1));
            }
            return result;
        } catch (SQLException exception) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, "An SQL exception has occurred", exception);
        }
        return null;
    }

    /**
     * Delete the ChildrenDiscount with Integer childrenDiscountID in the database. This method returns nothing.
     *
     * @param childrenDiscountID the ID to search for the ChildrenDiscount to delete
     */
    public void delete(int childrenDiscountID) {
        try (PreparedStatement ps = this.connection.prepareStatement("DELETE FROM transit_system.children_discount WHERE children_discount_id = ?")) {
            ps.setInt(1, childrenDiscountID);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the ChildrenDiscount childrenDiscount to the database and return the ChildrenDiscount.
     *
     * @param childrenDiscount the ChildrenDiscount to be saved
     * @return the ChildrenDiscount just saved
     */
    public ChildrenDiscount save(ChildrenDiscount childrenDiscount) {
        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO transit_system.children_discount (discount_rate, expiration_date, user_id) VALUES (?, ?, ?) RETURNING children_discount_id")) {
            ps.setBigDecimal(1, childrenDiscount.getDiscountRate());
            ps.setString(2, childrenDiscount.getExpirationDate());
            ps.setInt(3, childrenDiscount.getUserID());
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            ChildrenDiscount newChildrenDiscount = new ChildrenDiscount();
            newChildrenDiscount.setChildrenDiscountID(result.getInt(1));
            newChildrenDiscount.setDiscountRate(childrenDiscount.getDiscountRate());
            newChildrenDiscount.setExpirationDate(childrenDiscount.getExpirationDate());
            return newChildrenDiscount;
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "save error", exception);
        }
        return null;
    }

    /**
     * Update the ChildrenDiscount in the database which has the same id as the ChildrenDiscount childrenDiscount.
     *
     * @param childrenDiscount the ChildrenDiscount that the update is based on
     */
    public void update(ChildrenDiscount childrenDiscount) {
        try (PreparedStatement ps = this.connection.prepareStatement("UPDATE transit_system.children_discount SET discount_rate = ?, expiration_date = ? WHERE children_discount_id = ?")) {
            ps.setBigDecimal(1, childrenDiscount.getDiscountRate());
            ps.setString(2, childrenDiscount.getExpirationDate());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
