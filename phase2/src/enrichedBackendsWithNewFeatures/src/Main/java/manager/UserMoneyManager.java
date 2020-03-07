package manager;

import java.util.Observable;
import java.util.Observer;
import dao.SystemInfoDao;
import dao.UserMoneyManagerDao;
import lombok.Data;
import dao.TotalFareByCityDao;
import table.TotalFareByCity;
import java.math.BigDecimal;

@Data
public class UserMoneyManager implements Observer {
    private int userMoneyManagerID;
    private int userID;
    private BigDecimal totalTrafficFare = new BigDecimal(0);
    private String status = "Common";
    private BigDecimal membershipDiscountRate;
    private int childrenDiscountID;

    private TotalFareByCityDao totalFareByCityDao;
    private SystemInfoDao systemInfoDao;
    private UserMoneyManagerDao userMoneyManagerDao;

    public UserMoneyManager(){}

    private void saveNewLineInTotalFareByCityTable(String city, BigDecimal revenue) {
        TotalFareByCity newTotalFareByCity = new TotalFareByCity();
        newTotalFareByCity.setCity(city);
        newTotalFareByCity.setUserID(userID);
        newTotalFareByCity.setTotalFareByCity(revenue);
        totalFareByCityDao.save(newTotalFareByCity);
    }

    private void updateTotalFareByCityTable(
            TotalFareByCity totalFareByCity, BigDecimal cityTotalFare) {
        totalFareByCity.setTotalFareByCity(cityTotalFare);
        totalFareByCityDao.update(totalFareByCity);
    }

    private void checkMembershipDiscount(String city, BigDecimal cityTotalFare) {
        if (systemInfoDao.getMembershipApplicability(city)) {
            if ((status.equals("Common"))
                    && (cityTotalFare.compareTo(systemInfoDao.getMembershipDividingFare(city, "Silver"))
                    > 0)) {
                status = "Silver";
                membershipDiscountRate = systemInfoDao.getMembershipDiscountRate(city, "Silver");
            } else if ((status.equals("Silver"))
                    && (cityTotalFare.compareTo(systemInfoDao.getMembershipDividingFare(city, "Gold")) > 0)) {
                status = "Gold";
                membershipDiscountRate = systemInfoDao.getMembershipDiscountRate(city, "Gold");
            } else if ((status.equals("Gold"))
                    && (cityTotalFare.compareTo(systemInfoDao.getMembershipDividingFare(city, "Platinum"))
                    > 0)) {
                status = "Platinum";
                membershipDiscountRate = systemInfoDao.getMembershipDiscountRate(city, "Platinum");
            }
            userMoneyManagerDao.update(this);
        } else {
            System.out.println("We cannot use membership discount in " + city);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        TripInfoNotifier tripInfoNotifier = ((TripInfoNotifier) o);
        BigDecimal revenue = tripInfoNotifier.getRevenue();
        String city = tripInfoNotifier.getTripSegment().getCity();
        totalTrafficFare = totalTrafficFare.add(revenue);
        TotalFareByCity totalFareByCity = totalFareByCityDao.getTotalFareByCity(city, userID);
        BigDecimal cityTotalFare;
        if (totalFareByCity == null) {
            saveNewLineInTotalFareByCityTable(city, revenue);
            cityTotalFare = revenue;
        } else {
            BigDecimal totalFare = totalFareByCity.getTotalFareByCity();
            cityTotalFare = totalFare.add(revenue);
            updateTotalFareByCityTable(totalFareByCity, cityTotalFare);
        }
        checkMembershipDiscount(city, cityTotalFare);
        o.deleteObserver(this);
    }

    public void  deleteChildrenDiscountID() {
        this.childrenDiscountID = 0;
    }
}


