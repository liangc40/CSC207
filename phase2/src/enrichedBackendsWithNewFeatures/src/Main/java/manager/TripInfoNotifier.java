package manager;

import java.math.BigDecimal;
import java.util.Observable;

import dao.*;
import entity.AdminUser;
import entity.GeneralTrip;
import entity.TripSegment;
import lombok.Getter;
import lombok.AllArgsConstructor;
import service.PastTripManagerService;
import service.UserMoneyManagerService;

public class TripInfoNotifier extends Observable {
    private TripSegment tripSegment;
    private GeneralTrip newTrip;
    private BigDecimal revenue;
    private BigDecimal cost;

    @Getter
    private SystemInfoDao systemInfoDao;
    private AdminUserDao adminUserDao;
    private DailyReportDao dailyReportDao;
    private DailyReportMoneyManagerDao dailyReportMoneyManagerDao;
    private PastTripManagerDao pastTripManagerDao;
    private CardDao cardDao;
    private CardMoneyManagerDao cardMoneyManagerDao;
    private UserMoneyManagerDao userMoneyManagerDao;
    private UserDao userDao;

    public TripInfoNotifier(
            SystemInfoDao systemInfoDao,
            AdminUserDao adminUserDao,
            DailyReportDao dailyReportDao,
            DailyReportMoneyManagerDao dailyReportMoneyManagerDao,
            PastTripManagerDao pastTripManagerDao,
            CardDao cardDao,
            CardMoneyManagerDao cardMoneyManagerDao,
            UserMoneyManagerDao userMoneyManagerDao,
            UserDao userDao) {
        this.systemInfoDao = systemInfoDao;
        this.adminUserDao = adminUserDao;
        this.dailyReportDao = dailyReportDao;
        this.dailyReportMoneyManagerDao = dailyReportMoneyManagerDao;
        this.pastTripManagerDao = pastTripManagerDao;
        this.cardDao = cardDao;
        this.cardMoneyManagerDao = cardMoneyManagerDao;
        this.userMoneyManagerDao = userMoneyManagerDao;
        this.userDao = userDao;
    }
    // LC...........................................
    /**
     * Update the total revenue and the total cost today's daily report. If the argument is "update
     * total revenue", then update the total today's revenue by adding this trip's charge amount to
     * the original revenue; similarly, if the argument is "update total cost", then update today's
     * total cost by adding this trip's admin cost to the original cost. This method returns nothing.
     *
     * @param updateContent the string showing which part of our daily report need to be updated
     */
    public void notifyBalanceChange(BigDecimal fare, String updateContent) {
        // Get the daily report of this city.
        DailyReportMoneyManager reportMoneyManager =
                dailyReportMoneyManagerDao.get(
                        dailyReportDao
                                .get(adminUserDao.get(tripSegment.getCity()).getTodayDailyReportID())
                                .getDailyReportMoneyManagerID());
        CardMoneyManager cardMoneyManager =
                cardMoneyManagerDao.get(cardDao.get(tripSegment.getCardID()).getCardMoneyManagerID());
        //    CardHolderMoneyManager userMoneyManager =
        //
        // tripSegment.getCard().getCardHolder().getCardHolderService().getCardHolderMoneyManager();
        UserMoneyManager rawUserMoneyManager =
                userMoneyManagerDao.get(
                        userDao.get(cardDao.get(tripSegment.getCardID()).getUserID()).getUserMoneyManagerID());
        UserMoneyManager userMoneyManager = UserMoneyManagerService.packageUserMoneyManager(rawUserMoneyManager);
        switch (updateContent) {
            // If the update content is revenue, then add this trip's charge amount to total revenue.
            case "update revenue":
                this.revenue = fare;
                reportMoneyManager.update(this, "update total revenue");
                cardMoneyManager.update(this, "update card balance");
                userMoneyManager.update(this, "update total fare");
                setChanged();
                notifyObservers();
                break;
            case "update cost":
                this.cost = fare;
                reportMoneyManager.update(this, "update total cost");
                setChanged();
                notifyObservers();
        }
    }

    public void addNewTrip(GeneralTrip newGeneralTrip) {
        newTrip = newGeneralTrip;
        PastTripManager pastTripManager = pastTripManagerDao
                .get(cardDao.get(tripSegment.getCardID()).getPastTripManagerID());
        PastTripManager packedPastTripManager = PastTripManagerService.packagePastTripManager(pastTripManager);
        PastTripManager pastTripManagerTwo = pastTripManagerDao.get(userDao.get(cardDao.get(tripSegment.getCardID()).getUserID()).getPastTripManagerID());
        PastTripManager packedPastTripManagerTwo = PastTripManagerService.packagePastTripManager(pastTripManagerTwo);
        packedPastTripManager.update(this, "add new trip");
        packedPastTripManagerTwo.update(this,"add new trip");
        setChanged();
        notifyObservers();
    }

    public void updateDestination() {
        PastTripManager rawPastTripManagerOne = pastTripManagerDao
                .get(cardDao.get(tripSegment.getCardID()).getPastTripManagerID());
        PastTripManager packedPastTripManagerOne = PastTripManagerService.packagePastTripManager(rawPastTripManagerOne);
        PastTripManager rawPastTripManagerTwo = pastTripManagerDao
                .get(userDao.get(cardDao.get(tripSegment.getCardID()).getUserID()).getPastTripManagerID());
        PastTripManager packedPastTripManagerTwo = PastTripManagerService.packagePastTripManager(rawPastTripManagerTwo);
        packedPastTripManagerOne.update(this, "update trip's destination");
        packedPastTripManagerTwo.update(this, "update trip's destination");
        setChanged();
        notifyObservers();
    }

    public GeneralTrip getNewTrip() {
        return newTrip;
    }

    public TripSegment getTripSegment() {
        return tripSegment;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setTripSegment(TripSegment tripSegment) {
        this.tripSegment = tripSegment;
    }
}
