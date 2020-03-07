package service;

import dao.AdminUserDao;
import dao.DailyReportDao;
import dao.TransitPassDao;
import dao.CardDao;
import dao.DailyReportMoneyManagerDao;
import dao.SystemInfoDao;
import entity.Card;
import entity.TransitPass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.DailyReportMoneyManager;
import manager.TimeManager;

@AllArgsConstructor
public class TransitPassService {
    @Getter
    private SystemInfoDao systemInfoDao;
    private CardDao cardDao;
    private TransitPassDao transitPassDao;
    private AdminUserDao adminUserDao;
    private DailyReportDao dailyReportDao;
    private DailyReportMoneyManagerDao dailyReportMoneyManagerDao;
    private AdminUserService adminUserService;
    private TimeManager timeManager;


    public TransitPass createNewTransitPass(String type, String city, Card card) {
        TransitPass transitPass = new TransitPass();
        transitPass.setCity(city);
        transitPass.setCardID(card.getCardID());
        TransitPass savedTransitPass = transitPassDao.save(transitPass);
        TransitPass packagedTransitPass = packTransitPass(savedTransitPass);
        String startDate = timeManager.convertTimeFromLongToStringFormatOne(savedTransitPass.getRegistrationDate());
        switch (type) {
            case "Day Pass":
                savedTransitPass.setExpirationDate(timeManager.increaseDate(startDate, 1));
                break;
            case "Three Day Pass":
                savedTransitPass.setExpirationDate(timeManager.increaseDate(startDate, 3));
                break;
            case "Seven Day Pass":
                savedTransitPass.setExpirationDate(timeManager.increaseDate(startDate, 7));
                break;
            case "Monthly Pass":
                String lastDayOfMonth = timeManager.findLastDayOfCurrentMonth(startDate);
                savedTransitPass.setExpirationDate(timeManager.increaseDate(lastDayOfMonth, 1));

                break;
        }
        packagedTransitPass.setChargeAmount(systemInfoDao.getTransitPassPrice(city, type));
        transitPassDao.update(packagedTransitPass);
        card.getTransitPassesIDs().add(packagedTransitPass.getTransitPassID());
        cardDao.update(card);
        adminUserService.addNewTransitPass(savedTransitPass, adminUserDao.get(city));
        packagedTransitPass.updateTotalBalance();
        return  packagedTransitPass;
    }

    private TransitPass packTransitPass(TransitPass transitPass) {
        transitPass.setAdminUserDao(adminUserDao);
        transitPass.setDailyReportMoneyManagerDao(dailyReportMoneyManagerDao);
        transitPass.setDailyReportDao(dailyReportDao);
        return transitPass;
    }
}
