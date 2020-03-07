package service;

import entity.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.CardMoneyManager;
import manager.DailyReportMoneyManager;
import manager.UserMoneyManager;
import entity.ChildrenDiscount;
import manager.TimeManager;
import entity.User;
import table.TotalFareByCity;
import dao.*;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
public class UserMoneyManagerService {
    private static TotalFareByCityDao totalFareByCityDao;
    private static SystemInfoDao systemInfoDao;
    private static UserMoneyManagerDao userMoneyManagerDao;
    private ChildrenDiscountDao childrenDiscountDao;
    private TimeManager timeManager;
    private UserDao userDao;

    public UserMoneyManager createUserMoneyManager(
            TotalFareByCityDao totalFareByCityDao,
            SystemInfoDao systemInfoDao,
            UserMoneyManagerDao userMoneyManagerDao) {
        UserMoneyManager userMoneyManager = new UserMoneyManager();
        UserMoneyManagerService.totalFareByCityDao = totalFareByCityDao;
        UserMoneyManagerService.systemInfoDao = systemInfoDao;
        UserMoneyManagerService.userMoneyManagerDao = userMoneyManagerDao;
        return userMoneyManagerDao.save(userMoneyManager);
    }

    public static UserMoneyManager packageUserMoneyManager(UserMoneyManager userMoneyManager) {
        userMoneyManager.setTotalFareByCityDao(totalFareByCityDao);
        userMoneyManager.setUserMoneyManagerDao(userMoneyManagerDao);
        userMoneyManager.setSystemInfoDao(systemInfoDao);
        return userMoneyManager;
    }

    public void addChildrenDiscount(String type, String city, User user) {
        User packagedUser;
        if (systemInfoDao.getChildrenPassApplicability(city).equals(true)) {
            if (user.getClass().getSimpleName().equals("AdminUser")) {
                packagedUser = packageAdminUser((AdminUser) user);
            } else {
                packagedUser = user;
            }
            saveChildrenDiscountInTable(type, city, packagedUser);
        } else {
            System.out.println(city + "DOES NOT USE CHILDREN PASS");
        }
    }

    private String findExpirationDate(String type, String city, User user) {
        return timeManager.find_nth_birthday(
                user, systemInfoDao.getChildPassAgeLimit(city, type).intValue());
    }

    private User packageAdminUser(AdminUser adminUser) {
        return userDao.get(adminUser.getUserID());
    }

    private void saveChildrenDiscountInTable(String type, String city, User user) {
        String expirationDate = findExpirationDate(type, city, user);
        if (timeManager.isValid(timeManager.convertTimeFromLongToStringFormatTwo(Instant.now().getEpochSecond()), expirationDate)) {
            ChildrenDiscount childrenDiscount = new ChildrenDiscount();
            childrenDiscount.setExpirationDate(expirationDate);
            childrenDiscount.setDiscountRate(systemInfoDao.getChildPassDiscountRate(city, type));
            childrenDiscount.setUserID(user.getUserID());
            ChildrenDiscount savedChildrenDiscount = childrenDiscountDao.save(childrenDiscount);
            UserMoneyManager userMoneyManager = userMoneyManagerDao.get(user.getUserMoneyManagerID());
            userMoneyManager.setChildrenDiscountID(savedChildrenDiscount.getChildrenDiscountID());
            userMoneyManagerDao.update(userMoneyManager);
        } else {
            System.out.println("Invalid operation: Your age exceeds the discount range.");
        }
    }
}

