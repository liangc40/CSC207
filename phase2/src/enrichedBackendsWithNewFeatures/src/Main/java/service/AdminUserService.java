package service;

import dao.*;
import entity.*;
import logger.UserLogger;
import manager.DailyReportMoneyManager;
import manager.UserMoneyManager;
import table.DailyReportsStoredByTime;
import table.TransitPassExpirationDate;
import manager.TimeManager;
import dao.TransitPassDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

@AllArgsConstructor
public class AdminUserService {
    @Getter private final UserDao userDao;
    private final CardDao cardDao;
    private final AdminUserDao adminUserDao;
    private final TransitPassDao transitPassDao;
    private final DailyReportDao dailyReportDao;
    private final DailyReportMoneyManagerDao dailyReportMoneyManagerDao;
    private final DailyReportsStoredByTimeDao dailyReportsStoredByTimeDao;
    private final TransitPassExpirationDateDao transitPassExpirationDateDao;
    private final ChildrenDiscountDao childrenDiscountDao;
    private final UserMoneyManagerDao userMoneyManagerDao;
    private final TimeManager timeManager;

    public AdminUser createNewAdminUser(User user, String city) {
        AdminUser adminUser = new AdminUser();
        copyInformation(adminUser, user);
        Long time = Instant.now().getEpochSecond();
        String convertedTime = timeManager.convertTimeFromLongToStringFormatOne(time);
        adminUser.setCurrentMonth(timeManager.getMonth(convertedTime));
        adminUser.setCurrentYear(timeManager.getYear(convertedTime));
        adminUser.setCity(city);
        AdminUser savedAdminUser = adminUserDao.save(adminUser);
        createDailyReport(convertedTime, savedAdminUser);
        return savedAdminUser;
    }

    private void copyInformation(AdminUser adminUser, User user) {
        adminUser.setName(user.getName());
        adminUser.setEmail(user.getEmail());
        adminUser.setBirthday(user.getBirthday());
        adminUser.setPassword(user.getPassword());
        adminUser.setUserID(user.getUserID());
        adminUser.setPastTripManagerID(user.getPastTripManagerID());
        adminUser.setCardIDs(user.getCardIDs());
        adminUser.setTrafficStationNearHome(user.getTrafficStationNearHome());
        adminUser.setTrafficStationNearWork(user.getTrafficStationNearWork());
        adminUser.setUserLogger(user.getUserLogger());
        adminUser.setRegistrationTime(user.getRegistrationTime());
    }

    public void createDailyReport(String date, AdminUser adminUser) {
        DailyReportMoneyManager dailyReportMoneyManager =
                new DailyReportMoneyManager(dailyReportMoneyManagerDao);
        DailyReportMoneyManager savedDailyReportMoneyManager =
                dailyReportMoneyManagerDao.save(dailyReportMoneyManager);
        DailyReport dailyReport = new DailyReport();
        dailyReport.setDate(date);
        dailyReport.setDailyReportMoneyManagerID(
                savedDailyReportMoneyManager.getDailyReportMoneyManagerID());
        DailyReport savedDailyReport = dailyReportDao.save(dailyReport);
        adminUser.setTodayDailyReportID(savedDailyReport.getDailyReportID());
        adminUserDao.update(adminUser);
        storeDailyReport(adminUser, savedDailyReport);
    }

    private void storeDailyReport(AdminUser adminUser, DailyReport savedDailyReport) {
        DailyReportsStoredByTime dailyReportsStoredByTime = new DailyReportsStoredByTime();
        dailyReportsStoredByTime.setAdminUserID(adminUser.getAdminUserID());
        dailyReportsStoredByTime.setYear(timeManager.getYear(savedDailyReport.getDate()));
        dailyReportsStoredByTime.setMonth(timeManager.getMonth(savedDailyReport.getDate()));
        dailyReportsStoredByTime.setDay(timeManager.getDate(savedDailyReport.getDate()));
        dailyReportsStoredByTime.setDailyReportID(savedDailyReport.getDailyReportID());
        dailyReportsStoredByTimeDao.save(dailyReportsStoredByTime);
    }

    // 这里是save之后的transit pass
    public void addNewTransitPass(TransitPass transitPass, AdminUser adminUser) {
        String expirationDate = transitPass.getExpirationDate();
        TransitPassExpirationDate transitPassExpirationDate =
                transitPassExpirationDateDao.get(adminUser.getAdminUserID(), expirationDate);
        if (transitPassExpirationDate == null) {
            addNewEntryToTable(transitPass);
        } else {
            ArrayList<Integer> transitPassIDs = transitPassExpirationDate.getTransitPassIDs();
            transitPassIDs.add(transitPass.getTransitPassID());
            transitPassExpirationDate.setTransitPassIDs(transitPassIDs);
            transitPassExpirationDateDao.update(transitPassExpirationDate);
        }
    }

    public void addNewEntryToTable(TransitPass transitPass) {
        TransitPassExpirationDate transitPassExpirationDate = new TransitPassExpirationDate();
        ArrayList<Integer> newList = new ArrayList<>();
        newList.add(transitPass.getTransitPassID());
        transitPassExpirationDate.setTransitPassIDs(newList);
        transitPassExpirationDate.setExpirationDate(transitPass.getExpirationDate());
        transitPassExpirationDateDao.save(transitPassExpirationDate);
    }

    public void generateDailyReport(AdminUser adminUser) {
        DailyReport todayDailyReport = dailyReportDao.get(adminUser.getTodayDailyReportID());
        /* Logger Factory object used to log messages by admin user*/
        UserLogger userLogger = new UserLogger();
        userLogger.setLogId(adminUser.getCity() + "'s daily report");
        userLogger
                .getLogger()
                .info(
                        adminUser.getCity()
                                + "-- "
                                + dailyReportMoneyManagerDao
                                .get(todayDailyReport.getDailyReportID())
                                .toString(todayDailyReport.getDate()));
        // eden..............................................
        deleteExpiredTransitPasses(adminUser, timeManager.increaseDate(todayDailyReport.getDate(), 1));
        deleteExpiredChildrenDiscount(
                adminUser, timeManager.increaseDate(todayDailyReport.getDate(), 1));
        createDailyReport(timeManager.increaseDate(todayDailyReport.getDate(), 1), adminUser);
    }

    // eden...
    private void deleteExpiredTransitPasses(AdminUser adminUser, String date) {
        ArrayList<Integer> expiredTransitPassIDs =
                transitPassExpirationDateDao.get(adminUser.getAdminUserID(), date).getTransitPassIDs();
        if (expiredTransitPassIDs != null) {
            for (Integer transitPassID : expiredTransitPassIDs) {
                deleteTransitPass(
                        cardDao.get(transitPassDao.get(transitPassID).getCardID()),
                        transitPassDao.get(transitPassID));
            }
            transitPassExpirationDateDao.delete(adminUser.getAdminUserID(), date);
        }
    }

    public void deleteTransitPass(Card card, TransitPass transitPass) {
        card.getTransitPassesIDs();
        Iterator iterator = card.getTransitPassesIDs().iterator();
        while (iterator.hasNext()) {
            int id = (Integer) iterator.next();
            if (id == transitPass.getTransitPassID()) iterator.remove();
        }
        cardDao.update(card);
        transitPassDao.delete(transitPass.getTransitPassID());
    }

    /**
     * Return a String representing a specific month's revenue and cost. Int year is the year number
     * of the month and int month is the month number.
     *
     * @param year
     * @param month
     * @return
     */
    public String getMonthlyReport(AdminUser adminUser, int year, int month) {
        ArrayList<Integer> specifiedMonthDailyReportIDs =
                dailyReportsStoredByTimeDao.findDailyReportOfOneMonth(
                        month, year, adminUser.getAdminUserID());
        BigDecimal monthlyRevenue = new BigDecimal(0);
        BigDecimal monthlyCost = new BigDecimal(0);
        for (Integer dailyReportID : specifiedMonthDailyReportIDs) {
            monthlyRevenue = getRevenue(dailyReportID);
            monthlyCost = getCost(dailyReportID);
        }
        return "IN "
                + year
                + " "
                + (month + 1)
                + " THE TOTAL REVENUE IS "
                + monthlyRevenue
                + " AND TOTAL COST IS "
                + monthlyCost;
    }

    String getYearlyReport(AdminUser adminUser, int year) {
        ArrayList<Integer> specifiedYearDailyReportIDs =
                dailyReportsStoredByTimeDao.findDailyReportOfOneYear(year, adminUser.getAdminUserID());
        BigDecimal yearlyRevenue = new BigDecimal(0);
        BigDecimal yearlyCost = new BigDecimal(0);
        for (Integer dailyReportID : specifiedYearDailyReportIDs) {
            yearlyRevenue = getRevenue(dailyReportID);
            yearlyCost = getCost(dailyReportID);
        }
        return "IN "
                + year
                + " "
                + " THE TOTAL REVENUE IS "
                + yearlyRevenue
                + " AND TOTAL COST IS "
                + yearlyCost;
    }

    private BigDecimal getRevenue(Integer dailyReportID) {
        DailyReportMoneyManager dailyReportMoneyManager = getDailyReportMoneyManager(dailyReportID);
        return dailyReportMoneyManager.getDailyRevenue();
    }

    private BigDecimal getCost(Integer dailyreportID) {
        DailyReportMoneyManager dailyReportMoneyManager = getDailyReportMoneyManager(dailyreportID);
        return dailyReportMoneyManager.getDailyCost();
    }

    private DailyReportMoneyManager getDailyReportMoneyManager(Integer dailyReportID) {
        return dailyReportMoneyManagerDao.get(
                dailyReportDao.get(dailyReportID).getDailyReportMoneyManagerID());
    }

    private void deleteExpiredChildrenDiscount(AdminUser adminUser, String date) {
        ArrayList<Integer> expiredChildrenDiscountsIDs = childrenDiscountDao.getByDate(date);
        if (expiredChildrenDiscountsIDs != null) {
            for (Integer childrenDiscountID : expiredChildrenDiscountsIDs) {
                deleteChildrenDiscount(userDao.get(childrenDiscountDao.get(childrenDiscountID).getUserID()), childrenDiscountDao.get(childrenDiscountID));
                transitPassExpirationDateDao.delete(adminUser.getAdminUserID(), date);
            }
        }
    }

    private void deleteChildrenDiscount(User user, ChildrenDiscount childrenDiscount) {
        UserMoneyManager userMoneyManager = userMoneyManagerDao.get(user.getUserMoneyManagerID());
        userMoneyManager.deleteChildrenDiscountID();
        childrenDiscountDao.delete(childrenDiscount.getChildrenDiscountID());
    }
}
