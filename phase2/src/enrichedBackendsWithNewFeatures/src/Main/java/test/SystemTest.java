package test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import exception.InvalidStrategyException;
import factories.ChargingStrategyFactory;
import factories.PricingStrategyFactory;
import manager.*;
import service.*;
import dao.*;
import entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import manager.TimeManager;
import service.CardMoneyManagerService;
import service.CardService;
import service.TransitPassService;
import service.UserService;

@Slf4j
@AllArgsConstructor
public class SystemTest {

  @Getter private TransitPassService transitPassService;

  public static void main(String[] args)
      throws ClassNotFoundException, SQLException, InvalidStrategyException {
    Class.forName("org.postgresql.Driver");
    Connection connection =
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "chenliang", "");

    log.info("test log at sl4j");
    log.error("throw exception");
    log.warn("without tapping out");

    TimeManager timeManager = new TimeManager();
    UserDao userDao = new UserDao(connection);
    CardDao cardDao = new CardDao(connection);
    AdminUserDao adminUserDao = new AdminUserDao(connection);
    TransitPassDao transitPassDao = new TransitPassDao(connection);
    DailyReportDao dailyReportDao = new DailyReportDao(connection);
    DailyReportMoneyManagerDao dailyReportMoneyManagerDao =
        new DailyReportMoneyManagerDao(connection);
    DailyReportsStoredByTimeDao dailyReportsStoredByTimeDao =
        new DailyReportsStoredByTimeDao(connection);
    TransitPassExpirationDateDao transitPassExpirationDateDao =
        new TransitPassExpirationDateDao(connection);
    ChildrenDiscountDao childrenDiscountDao = new ChildrenDiscountDao(connection);
    UserMoneyManagerDao userMoneyManagerDao = new UserMoneyManagerDao(connection);
    TotalFareByCityDao totalFareByCityDao = new TotalFareByCityDao(connection);
    SystemInfoDao systemInfoDao = new SystemInfoDao(connection);
    CardMoneyManagerDao cardMoneyManagerDao = new CardMoneyManagerDao(connection);
    GeneralTripDao generalTripDao = new GeneralTripDao(connection);
    OneWayTripFrequencyDao oneWayTripFrequencyDao = new OneWayTripFrequencyDao(connection);
    PastTripManagerDao pastTripManagerDao = new PastTripManagerDao(connection);
    TripFrequencyBetweenStationsDao tripFrequencyBetweenStationsDao =
        new TripFrequencyBetweenStationsDao(connection);
    TripSegmentDao tripSegmentDao = new TripSegmentDao(connection);
    PricingStrategyFactory pricingStrategyFactory = new PricingStrategyFactory(systemInfoDao);
    ChargingStrategyFactory chargingStrategyFactory =
        new ChargingStrategyFactory(
            userDao,
            cardDao,
            systemInfoDao,
            generalTripDao,
            tripSegmentDao,
            transitPassDao,
            userMoneyManagerDao,
            childrenDiscountDao,
            timeManager);

    AdminUserService adminUserService =
        new AdminUserService(
            userDao,
            cardDao,
            adminUserDao,
            transitPassDao,
            dailyReportDao,
            dailyReportMoneyManagerDao,
            dailyReportsStoredByTimeDao,
            transitPassExpirationDateDao,
            childrenDiscountDao,
            userMoneyManagerDao,
            timeManager);

    UserMoneyManagerService userMoneyManagerService =
        new UserMoneyManagerService(childrenDiscountDao, timeManager, userDao);

    UserMoneyManager userMoneyManager =
        userMoneyManagerService.createUserMoneyManager(
            totalFareByCityDao, systemInfoDao, userMoneyManagerDao);

    PastTripManagerService pastTripManagerService =
        new PastTripManagerService(
            generalTripDao,
            oneWayTripFrequencyDao,
            tripFrequencyBetweenStationsDao, pastTripManagerDao);

    PastTripManager pastTripManager = pastTripManagerService.createNewPastTripManager();

    UserService userService = new UserService(userDao, cardDao, userMoneyManagerDao);

    User user =
        userService.createNewUser(
            "Frank",
            "liangc40@yahoo.com",
            "123456",
            "2015-10-21",
            "St George",
            "St Patrick",
            userMoneyManager,
            pastTripManager);
    AdminUser adminUser = adminUserService.createNewAdminUser(user, "Toronto");

    CardMoneyManagerService cardMoneyManagerService = new CardMoneyManagerService();

    CardMoneyManager cardMoneyManager =
        cardMoneyManagerService.createNewCardMoneyManager(
            new BigDecimal("10"),
            new BigDecimal("20"),
            "Toronto",
            timeManager,
            cardDao,
            userDao,
            cardMoneyManagerDao,
            adminUserDao,
            dailyReportDao,
            dailyReportMoneyManagerDao);

    PastTripManager pastTripManager1 = pastTripManagerService.createNewPastTripManager();
    TransitPassService transitPassService =
        new TransitPassService(
            systemInfoDao,
            cardDao,
            transitPassDao,
            adminUserDao,
            dailyReportDao,
            dailyReportMoneyManagerDao,
            adminUserService,
            timeManager);
    CardService cardService = new CardService(new CardDao(connection), cardMoneyManagerDao);
    Card card = cardService.createNewCard(adminUser, cardMoneyManager, pastTripManager1);

    cardMoneyManager.loadMoney(new BigDecimal("50"));

    CardMoneyManager cardMoneyManager1 =
        cardMoneyManagerService.createNewCardMoneyManager(
            new BigDecimal("20"),
            new BigDecimal("20"),
            "Toronto",
            timeManager,
            cardDao,
            userDao,
            cardMoneyManagerDao,
            adminUserDao,
            dailyReportDao,
            dailyReportMoneyManagerDao);

    PastTripManager pastTripManager2 = pastTripManagerService.createNewPastTripManager();

    Card card1 = cardService.createNewCard(adminUser, cardMoneyManager1, pastTripManager2);

    userService.addCard(adminUser, card);

    userService.addCard(adminUser, card1);

    userService.deleteCard(adminUser, card1);

    userMoneyManagerService.addChildrenDiscount("child pass", "Toronto", adminUser);

    transitPassService.createNewTransitPass("Monthly Pass", "Toronto", card);


    TripSegmentService tripSegmentService =
        new TripSegmentService(
            timeManager,
            systemInfoDao,
            tripSegmentDao,
            pastTripManagerDao,
            pastTripManagerService,
            pricingStrategyFactory,
            chargingStrategyFactory);

    TripInfoNotifier tripInfoNotifier =
        new TripInfoNotifier(
            new SystemInfoDao(connection),
            new AdminUserDao(connection),
            new DailyReportDao(connection),
            new DailyReportMoneyManagerDao(connection),
            new PastTripManagerDao(connection),
            new CardDao(connection),
            new CardMoneyManagerDao(connection),
            new UserMoneyManagerDao(connection),
            new UserDao(connection));
    TripSegment tripSegment = tripSegmentService.createNewTripSegment(tripInfoNotifier);
    tripSegmentService.enterStation(tripSegment, "St George", card, "Bus");
    tripSegmentService.exitStation(tripSegment, "St Patrick");
  }
}
