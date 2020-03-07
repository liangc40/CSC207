package dao;

import java.sql.Array;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/** A SystemInfoDao accessing all the information in the database. */
public class SystemInfoDao extends BaseDao {

    /**
     * Construct the SystemInfoDao.
     *
     * @param connection the Connection that connects the database.
     */
    public SystemInfoDao(Connection connection) {
        super(connection);
    }

//    private String getChargingStrategy(String city) {
//        try (PreparedStatement ps =
//                     this.connection.prepareStatement(
//                             "SELECT * FROM transit_system.city_to_strategy_table WHERE city = ?")) {
//            ps.setString(1, city);
//            ResultSet result = ps.executeQuery();
//            if (!result.next()) {
//                return null;
//            }
//            return result.getString(3);
//        } catch (SQLException ex) {
//            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

    /**
     * Return a BigDecimal, the revenue with information that matches the entry of String city and
     * String trafficMode in the database.
     *
     * @param city the ID to search for in the database
     * @param trafficMode the ID to search for in the database
     * @return a BigDecimal that is the revenue
     */
    public BigDecimal getRevenue(String city, String trafficMode) {
        return new BigDecimal(Objects.requireNonNull(getRevenueAndCost(city, trafficMode))[0]);
    }

    /**
     * Return a BigDecimal, the cost with information that matches the entry of String city and
     * String trafficMode in the database.
     *
     * @param city the ID to search for in the database
     * @param trafficMode the ID to search for in the database
     * @return a BigDecimal that is the cost
     */
    public BigDecimal getCost(String city, String trafficMode) {
        return new BigDecimal(Objects.requireNonNull(getRevenueAndCost(city, trafficMode))[1]);
    }

    /**
     * Return a String array, the revenue and cost with information that matches the entry of String city and
     * String trafficMode in the database.
     *
     * @param city the ID to search for in the database
     * @param trafficMode the ID to search for in the database
     * @return a String array, the revenue and cost
     */
    private String[] getRevenueAndCost(String city, String trafficMode) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.revenue_cost_table WHERE city = ? AND traffic_mode = ?")) {
            ps.setString(1, city);
            ps.setString(2, trafficMode);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            Array datebaseArray = result.getArray(4);
            return (String[]) datebaseArray.getArray();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Return a String, the charging strategy with information that matches the entry of String city in
     * the database.
     *
     * @param city the city to search for in the database
     * @return a String array, the revenue and cost
     */
    public String getChargingStrategy(String city) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.city_to_strategy_table WHERE city = ? ")) {
            ps.setString(1, city);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getString(3);
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /**
     * Return a Boolean which is whether the charging strategy with information that matches the entry
     * of String city in the database considers transfer.
     *
     * @param strategyName the strategy name to search for in the database
     * @return a Boolean, whether the charging strategy considers transfer
     */
    public Boolean isStrategyTransferable(String strategyName) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.strategy_transferable_table WHERE strategy_name = ? ")) {
            ps.setString(1, strategyName);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getBoolean(3);
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BigDecimal getCapAmount(String city) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.cap_amount_table WHERE city = ? ")) {
            ps.setString(1, city);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getBigDecimal(3);
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //    public String getPricingStrategy(String city, String trafficMode, Boolean isRevenue) {
//        try (PreparedStatement ps =
//                     this.connection.prepareStatement(
//                             "SELECT * FROM transit_system.pricing_strategy_table WHERE city = ?, traffic_mode = ?, is_revenue = ? ")) {
//            ps.setString(1, city);
//            ps.setString(2, trafficMode);
//            ps.setBoolean(3, isRevenue);
//            ResultSet result = ps.executeQuery();
//            if (!result.next()) {
//                return null;
//            }
//            return result.getString(5);
//        } catch (SQLException ex) {
//            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    public String getPricingStrategy(String city, String trafficMode, Boolean isRevenue) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.pricing_strategy_table WHERE city = ? AND traffic_mode = ? AND is_revenue = ? ")) {
            ps.setString(1, city);
            ps.setString(2, trafficMode);
            ps.setBoolean(3, isRevenue);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getString(5);
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Boolean getChargingTime(String city, String trafficMode) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.charging_time_table WHERE city = ? AND traffic_mode = ?")) {
            ps.setString(1, city);
            ps.setString(2, trafficMode);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getBoolean(4);
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public BigDecimal getTransitPassPrice(String city, String type) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.transit_pass_table WHERE city = ? AND transit_pass_type = ?")) {
            ps.setString(1, city);
            ps.setString(2, type);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getBigDecimal(4);
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

//    public BigDecimal getTotalFareSpentInOneCity(String city, int userID) {
//        try (PreparedStatement ps =
//                     this.connection.prepareStatement(
//                             "SELECT * FROM transit_system.total_fare_by_city WHERE city = ? AND user_id = ?")) {
//            ps.setString(1, city);
//            ps.setInt(2, userID);
//            ResultSet result = ps.executeQuery();
//            if (!result.next()) {
//                return null;
//            }
//            return result.getBigDecimal(4);
//        } catch (SQLException exception) {
//            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, exception);
//        }
//        return null;
//    }

    public String getCityOfCorrespondingStation(String stationName) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.station_to_city_table WHERE station = ?")) {
            ps.setString(1, stationName);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            return result.getString(3);
        } catch (SQLException exception) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    public Boolean getChildrenPassApplicability(String city) {
        return Objects.requireNonNull(getDiscountApplicability(city))[0];
    }

    public Boolean getMembershipApplicability(String city) {
        return Objects.requireNonNull(getDiscountApplicability(city))[1];
    }

    public Boolean getBirthdayApplicability(String city) {
        return Objects.requireNonNull(getDiscountApplicability(city))[2];
    }

    public Boolean getTransitPassApplicability(String city) {
        return Objects.requireNonNull(getDiscountApplicability(city))[3];
    }

    private Boolean[] getDiscountApplicability(String city) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.discount_applicable_table WHERE city = ?")) {
            ps.setString(1, city);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            Array datebaseArray = result.getArray(3);
            return (Boolean[]) datebaseArray.getArray();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BigDecimal getChildPassAgeLimit(String city, String ageGroup) {
        return new BigDecimal(Objects.requireNonNull(getChildPassAgeLimitAndDiscount(city, ageGroup))[0]);
    }

    public BigDecimal getChildPassDiscountRate(String city, String ageGroup) {
        return new BigDecimal(Objects.requireNonNull(getChildPassAgeLimitAndDiscount(city, ageGroup))[1]);
    }

    private String[] getChildPassAgeLimitAndDiscount(String city, String ageGroup) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.child_discount_table WHERE city = ? AND age_group = ?")) {
            ps.setString(1, city);
            ps.setString(2, ageGroup);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            Array datebaseArray = result.getArray(4);
            return (String[]) datebaseArray.getArray();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BigDecimal getMembershipDividingFare(String city, String membershipLevel) {
        return new BigDecimal(Objects.requireNonNull(getMembershipDividingFareAndDiscount(city, membershipLevel))[0]);
    }

    public BigDecimal getMembershipDiscountRate(String city, String membershipLevel) {
        return new BigDecimal(Objects.requireNonNull(getMembershipDividingFareAndDiscount(city, membershipLevel))[1]);
    }

    private String[] getMembershipDividingFareAndDiscount(String city, String membershipLevel) {
        try (PreparedStatement ps =
                     this.connection.prepareStatement(
                             "SELECT * FROM transit_system.membership_discount_table WHERE city = ? AND membership_group = ?")) {
            ps.setString(1, city);
            ps.setString(2, membershipLevel);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            Array datebaseArray = result.getArray(4);
            return (String[]) datebaseArray.getArray();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
