package manager;

import dao.*;
import entity.User;
import service.UserService;
import lombok.Getter;
import lombok.Data;
import service.CardMoneyManagerService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Observable;
import java.util.Observer;

@Data
public class CardMoneyManager extends Observable implements Observer{
    private int cardMoneyManagerID;
    private int cardID;
    private BigDecimal balance;
    private BigDecimal automaticLoadAmount;
    private String registrationCity;

    @Getter private TimeManager timeManager;
    private CardDao cardDao;
    private UserDao userDao;
    private CardMoneyManagerDao cardMoneyManagerDao;
    private AdminUserDao adminUserDao;
    private DailyReportDao dailyReportDao;
    private DailyReportMoneyManagerDao dailyReportMoneyManagerDao;


    @Override
    public void update(Observable observable, Object arg) {
        balance = balance.subtract(((TripInfoNotifier) observable).getRevenue());
        if (balance.compareTo(new BigDecimal("0")) < 0) {
            loadMoney(
                    automaticLoadAmount);
        }
        observable.deleteObserver(this);
    }

    /**
     * Increase the balance of this card by the integer value of loadAmount and log the activity. If
     * the card is suspended, print the error message and log a message at severe level.
     *
     * @param loadAmount the amount to be loaded to this card
    //   * @param time the time when this activity happens
     */
    public void loadMoney(BigDecimal loadAmount) {
        CardMoneyManagerService.packageCardMoneyManager(this);
        if (cardDao.get(cardID).isActive()) {
            balance.add(loadAmount);
            cardMoneyManagerDao.save(this);
            User packagedUser = UserService.packageUser(userDao.get(cardDao.get(cardID).getUserID()));
            packagedUser
                    .getUserLogger()
                    .getLogger()
                    .fine("Loaded " + loadAmount + " dollars into my account at " + timeManager.convertTimeFromLongToStringFormatTwo(Instant.now().getEpochSecond()));
        } else {
            User packagedUser = UserService.packageUser(userDao.get(cardDao.get(cardID).getUserID()));
            packagedUser
                    .getUserLogger()
                    .getLogger()
                    .severe(
                            "Someone tried to load "
                                    + loadAmount
                                    + " dollars into suspended card "
                                    + cardID
                                    + " at "
                                    + timeManager.convertTimeFromLongToStringFormatTwo(Instant.now().getEpochSecond()));
        }
    }

    public void notifyBalanceChange(BigDecimal intialLoadAmout, DailyReportMoneyManager dailyReportMoneyManager) {
        dailyReportMoneyManager.update(this, "update total revenue");
        setChanged();
        notifyObservers();
    }
}