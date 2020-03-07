package entity;

import dao.DailyReportMoneyManagerDao;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.Getter;
import manager.DailyReportMoneyManager;
import manager.TimeManager;
import entity.Card;
import java.math.BigDecimal;
import java.util.Observable;

import dao.SystemInfoDao;
import dao.AdminUserDao;
import dao.DailyReportDao;

@Data

public class TransitPass extends Observable{
    private int transitPassID;
    private int cardID;
    private String city;
    private long registrationDate;
    private String expirationDate;
    private BigDecimal chargeAmount;

    @Getter
    private DailyReportMoneyManagerDao dailyReportMoneyManagerDao;
    private DailyReportDao dailyReportDao;
    private AdminUserDao adminUserDao;

    public void updateTotalBalance() {
        System.out.println(adminUserDao);
        DailyReportMoneyManager dailyReportMoneyManager =
                dailyReportMoneyManagerDao.get(
                        dailyReportDao
                                .get(adminUserDao.get(city).getTodayDailyReportID())
                                .getDailyReportMoneyManagerID());
        dailyReportMoneyManager.update(this, "update total revenue");
        setChanged();
        notifyObservers();
    }
}
