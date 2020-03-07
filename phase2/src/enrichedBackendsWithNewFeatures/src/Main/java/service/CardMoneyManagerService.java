
package service;

import dao.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.CardMoneyManager;
import manager.DailyReportMoneyManager;
import manager.TimeManager;

import java.math.BigDecimal;
import java.util.Observable;

@AllArgsConstructor
public class CardMoneyManagerService {

    @Getter
    private static CardMoneyManagerDao cardMoneyManagerDao;
    private static AdminUserDao adminUserDao;
    private static DailyReportDao dailyReportDao;
    private static DailyReportMoneyManagerDao dailyReportMoneyManagerDao;
    private static CardDao cardDao;
    private static UserDao userDao;
    private static TimeManager timeManager;

    public CardMoneyManager createNewCardMoneyManager (BigDecimal initialLoadAmount, BigDecimal automaticLoadAmount, String registrationCity, TimeManager timeManager, CardDao cardDao, UserDao userDao, CardMoneyManagerDao cardMoneyManagerDao, AdminUserDao adminUserDao, DailyReportDao dailyReportDao, DailyReportMoneyManagerDao dailyReportMoneyManagerDao) {
        CardMoneyManager cardMoneyManager = new CardMoneyManager();
        CardMoneyManagerService.adminUserDao = adminUserDao;
        CardMoneyManagerService.cardDao = cardDao;
        CardMoneyManagerService.userDao = userDao;
        CardMoneyManagerService.timeManager = timeManager;
        CardMoneyManagerService.cardMoneyManagerDao = cardMoneyManagerDao;
        CardMoneyManagerService.dailyReportDao = dailyReportDao;
        CardMoneyManagerService.dailyReportMoneyManagerDao = dailyReportMoneyManagerDao;
        cardMoneyManager.setBalance(initialLoadAmount);
        cardMoneyManager.setAutomaticLoadAmount(automaticLoadAmount);
        cardMoneyManager.setRegistrationCity(registrationCity);
        DailyReportMoneyManager dailyReportMoneyManager = dailyReportMoneyManagerDao.get(dailyReportDao.get(adminUserDao.get(registrationCity).getTodayDailyReportID()).getDailyReportMoneyManagerID());
        cardMoneyManager.notifyBalanceChange(initialLoadAmount, dailyReportMoneyManager);
        packageCardMoneyManager(cardMoneyManager);
        return cardMoneyManagerDao.save(cardMoneyManager);
    }

    public static CardMoneyManager packageCardMoneyManager(CardMoneyManager cardMoneyManager) {
        cardMoneyManager.setAdminUserDao(adminUserDao);
        cardMoneyManager.setCardMoneyManagerDao(cardMoneyManagerDao);
        cardMoneyManager.setCardDao(cardDao);
        cardMoneyManager.setUserDao(userDao);
        cardMoneyManager.setDailyReportDao(dailyReportDao);
        cardMoneyManager.setDailyReportMoneyManagerDao(dailyReportMoneyManagerDao);
        cardMoneyManager.setTimeManager(timeManager);
        return cardMoneyManager;
    }
}
