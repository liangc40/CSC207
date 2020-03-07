package manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import entity.TransitPass;
import lombok.Getter;
import service.TransitPassService;

import java.util.Observer;
import java.util.Observable;
import dao.DailyReportMoneyManagerDao;

import java.math.BigDecimal;

@Data
public class DailyReportMoneyManager implements Observer {
    private int dailyReportMoneyManagerID;
    private BigDecimal dailyCost = new BigDecimal(0);
    private BigDecimal dailyRevenue = new BigDecimal(0);
    private DailyReportMoneyManagerDao dailyReportMoneyManagerDao;

    public DailyReportMoneyManager(DailyReportMoneyManagerDao dailyReportMoneyManagerDao) {
        this.dailyReportMoneyManagerDao = dailyReportMoneyManagerDao;
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (arg.equals("update total revenue")) {
            if (observable.getClass().getSimpleName().equals("TripInfoNotifier")) {
                dailyRevenue = dailyRevenue.add(((TripInfoNotifier) observable).getRevenue());
            } else if (observable.getClass().getSimpleName().equals("TransitPass")) {
                dailyRevenue = dailyRevenue.add(((TransitPass) observable).getChargeAmount());
            } else if (observable.getClass().getSimpleName().equals("CardMoneyManager")) {
                dailyRevenue = dailyRevenue.add(((CardMoneyManager) observable).getBalance());
            }
        } else if (arg.equals("update total cost")) {
            dailyCost = dailyCost.add(((TripInfoNotifier) observable).getCost());
        }
        observable.deleteObserver(this);
        dailyReportMoneyManagerDao.update(this);
    }

    public String toString(String date) {
        return date + " TODAY'S DAILY REVENUE IS " + dailyRevenue + "TODAY'S DAILY COST IS " + dailyCost;
    }
}
