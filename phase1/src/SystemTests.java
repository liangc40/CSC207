import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

public class SystemTests {
    NormalCardHolder normalCardHolder;
    private Card card;
    private AdminUser adminUserOne, adminUserTwo;
    private String dateOne, dateTwo, dateThree, dateFour;
    private double monthlyAverageCostOne, monthlyAverageCostTwo, monthlyAverageCostThree;
    private String recentThreeTripsOne, recentThreeTripsTwo, recentThreeTripsThree, recentThreeTripsFour;
    private GeneralTrip trip1,
            trip2,
            caseOneCurrentTripOne,
            caseOneLastTripOne,
            caseOneCurrentTripTwo,
            caseOneLastTripTwo;
    private float caseOneBalanceOne, caseOneBalanceTwo, caseOneBalanceThree, caseOneBalanceFour;
    private double caseOneCostOne, caseOneCostTwo, caseOneCostThree, caseOneCostFour;
    private double caseOneRevenueOne, caseOneRevenueTwo, caseOneRevenueThree, caseOneRevenueFour;
    private GeneralTrip trip3,
            trip4,
            caseTwoCurrentTripOne,
            caseTwoLastTripOne,
            caseTwoCurrentTripTwo,
            caseTwoLastTripTwo;
    private float caseTwoBalanceOne, caseTwoBalanceTwo, caseTwoBalanceThree, caseTwoBalanceFour;
    private double caseTwoCostOne, caseTwoCostTwo, caseTwoCostThree, caseTwoCostFour;
    private double caseTwoRevenueOne, caseTwoRevenueTwo, caseTwoRevenueThree, caseTwoRevenueFour;
    private GeneralTrip trip5, trip6, caseThreeCurrentTripOne, caseThreeLastTripOne;
    private float caseThreeBalanceOne, caseThreeBalanceTwo, caseThreeBalanceThree;
    private double caseThreeCostOne, caseThreeCostTwo, caseThreeCostThree;
    private double caseThreeRevenueOne, caseThreeRevenueTwo, caseThreeRevenueThree;
    private GeneralTrip trip7, trip8, caseFourCurrentTripOne, caseFourLastTripOne;
    private float caseFourBalanceOne, caseFourBalanceTwo, caseFourBalanceThree;
    private double caseFourCostOne, caseFourCostTwo, caseFourCostThree;
    private double caseFourRevenueOne, caseFourRevenueTwo, caseFourRevenueThree;
    private GeneralTrip trip9,
            trip10,
            caseFiveCurrentTripOne,
            caseFiveLastTripOne,
            caseFiveCurrentTripTwo,
            caseFiveLastTripTwo;
    private float caseFiveBalanceOne, caseFiveBalanceTwo, caseFiveBalanceThree, caseFiveBalanceFour;
    private double caseFiveCostOne, caseFiveCostTwo, caseFiveCostThree, caseFiveCostFour;
    private double caseFiveRevenueOne, caseFiveRevenueTwo, caseFiveRevenueThree, caseFiveRevenueFour;
    private GeneralTrip trip11,
            trip12,
            trip13,
            trip14,
            caseSixCurrentTripOne,
            caseSixLastTripOne,
            caseSixLastTripTwo;
    private float caseSixBalanceOne, caseSixBalanceTwo, caseSixBalanceThree, caseSixBalanceFour;
    private double caseSixCostOne, caseSixCostTwo, caseSixCostThree, caseSixCostFour;
    private double caseSixRevenueOne, caseSixRevenueTwo, caseSixRevenueThree, caseSixRevenueFour;
    private GeneralTrip trip15, trip16, caseSevenCurrentTripOne, caseSevenLastTripOne;
    private float caseSevenBalanceOne,
            caseSevenBalanceTwo,
            caseSevenBalanceThree,
            caseSevenBalanceFour;
    private double caseSevenCostOne, caseSevenCostTwo, caseSevenCostThree, caseSevenCostFour;
    private double caseSevenRevenueOne,
            caseSevenRevenueTwo,
            caseSevenRevenueThree,
            caseSevenRevenueFour;
    private GeneralTrip trip17, trip18, caseEightCurrentTripOne, caseEightLastTripOne;
    private float caseEightBalanceOne,
            caseEightBalanceTwo,
            caseEightBalanceThree,
            caseEightBalanceFour;
    private double caseEightCostOne, caseEightCostTwo, caseEightCostThree, caseEightCostFour;
    private double caseEightRevenueOne,
            caseEightRevenueTwo,
            caseEightRevenueThree,
            caseEightRevenueFour;

    @Before
    public void setUp() throws ParseException {

        RoutesHandler.readRoutes();
        // creeate two admin users to administrate traffic systems in Montreal and Toronto
        // respectively
        adminUserOne = new AdminUser("Montréal", "2018-06-30 00:00:00 EDT");
        adminUserTwo = new AdminUser("Toronto", "2018-06-30 00:00:00 EDT");
        // Add these two admin users to adminUserMap in Main class, and store their respective
        // city names as keys.
        Main.adminUserMap.put("Montréal", adminUserOne);
        Main.adminUserMap.put("Toronto", adminUserTwo);
        HashMap<String, Double> map1 = new HashMap<>();
        HashMap<String, Double> map2 = new HashMap<>();
        HashMap<String, Double> map3 = new HashMap<>();
        HashMap<String, Double> map4 = new HashMap<>();
        map1.put("Subway", 0.5);
        map1.put("Bus", 2.0);
        map2.put("Subway", 0.3);
        map2.put("Bus", 0.15);
        // Figure out how revenue could be generated if a passenger take the subway
        // for one stop in Montreal (without considering the cap).
        Main.revenueMap.put("Montréal", map1);
        // Figure out how much cost could be generated if a passenger take the bus
        // for one stop in Montreal.
        Main.costMap.put("Montréal", map2);
        map3.put("Subway", 0.5);
        map3.put("Bus", 2.0);
        map4.put("Subway", 0.32);
        map4.put("Bus", 0.12);
        // Figure out how revenue could be generated if a passenger take the subway
        // for one stop in Toronto (without considering the cap).
        Main.revenueMap.put("Toronto", map3);
        // Figure out how much cost could be generated if a passenger take the bus
        // for one stop in Toronto.
        Main.costMap.put("Toronto", map4);
        Main.capMap.put("Montréal", 6.0);
        Main.capMap.put("Toronto", 6.0);
        // Create a hard holder with her unique email address and her unique logger
        UserLogger logger = new UserLogger();
        logger.setLogId("annamartinez@gmail.com");
        normalCardHolder = new NormalCardHolder("Anna Martinez", "annamartinez@gmail.com", "2018-06-30 10:00:00 EDT", logger);
        // Create a new card, and add this card to the card holder we just created.
        card = new Card("000001");
        card.setNormalCardHolder(normalCardHolder);
        normalCardHolder.addCard(card, "2018-06-30 10:00:00 EDT");
        normalCardHolder.setAutomaticLoadAmount("10", "000001");

        // Case One : Now we analyze the case that the preceding trip is a subway trip and the following
        // trip is a bus trip. In this case these two adjacent trips do not share a cap, and we
        // trace all possible parameters generated to make sure it works exactly as we expected.
        trip1 = TripFactory.createNewTrip("Subway");
        // Add entre information to the newly generated subway trip.
        trip1.addEntreInformation("2018-06-30 11:00:00 EDT", "McGill", "000001");
        // After adding entre information, we get the card's current trip, and check whether such
        // current trip's entre time and entre staion are the same as the entre time and entre station
        // of trip1.
        caseOneCurrentTripOne = card.getCurrentTrip();
        // For a subway trip, adding entre information should not alter the card's balance. Get the card
        // balance and check whether it is the same as we expected.
        caseOneBalanceOne = card.getBalance();
        // As the balance of the card does not change, daily report's total revenue should not change.
        // Also, as the passenger just entered the station, the stops he/she travelled is zero;
        // therefore, daily report's total revenue should not change as well. Now compare daily report's
        // total revenue and total cost with figures we expected.
        caseOneCostOne = adminUserOne.getTodayDailyReport().getDailyCost();
        caseOneRevenueOne = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip1.
        trip1.addExitInformation("2018-06-30 13:05:00 EDT", "Berri-UQAM", "000001");
        // After adding the exit information, we get the card's last trip, and check whether such trip's
        // exit information and exit time are the same as trip1.
        caseOneLastTripOne = card.getLastTrip();
        // After riding the subway for over two hours, the card's balance should be deducted by 6. Get
        // the card balance and check whether it is the same as we expected.
        caseOneBalanceTwo = card.getBalance();
        // After calculating the number of stops the passenger has travelled we could get the amount of
        // fare administrators should pay to keep the system operate properly; therefore daily report's
        // revenue should be deducted by such amount of administration fee. Now compare the daily
        // report's daily revenue to see whether it is the same as we expected.
        caseOneCostTwo = adminUserOne.getTodayDailyReport().getDailyCost();
        // As the balance of the card is subtracted by 6, the total revenue of daily report should
        // increase by 6. Now compare daily report's daily cost with the figure we expected.
        caseOneRevenueTwo = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Now we create a new bus trip which does not share the same cap with the preceding subway
        // trip.
        trip2 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated bus trip.
        trip2.addEntreInformation("2018-06-30 13:10:00 EDT", "Berri-UQAM", "000001", "Bus Route 1");
        // After adding entre information, we get the card's current trip, and check whether such
        // current trip's entre time and entre staion are the same as the entre time and entre station
        // of trip2.
        caseOneCurrentTripTwo = card.getCurrentTrip();
        // As trip2 does not share the same cap as trip one, the amount of fare that the card should be
        // charged is 2. Get the card balance and check whether it is the same as we expected.
        caseOneBalanceThree = card.getBalance();
        // As the balance of the card is subtracted by 2, the total revenue of daily report should
        // increase by 2. Now compare daily report's daily cost with the figure we expected.
        caseOneCostThree = adminUserOne.getTodayDailyReport().getDailyCost();
        // As the passenger just entered subway station, the amount of change to total revenue should be
        // zero. Now check daily report's total revenue and whether they remain the same.
        caseOneRevenueThree = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add entre information to the newly generated bus trip.
        trip2.addExitInformation("2018-06-30 13:25:00 EDT", "Jolicoeur", "000001");
        // After adding exit information, we get the card's last trip, and check whether such
        // current trip's exit time and exit station are the same as the exit time and exit station
        // of trip2 respectively.
        caseOneLastTripTwo = card.getLastTrip();
        // When passenger get off the bus the card would not be charged. In this way, the card's balance should
        // not alter at all. Now we check the card balance and see whether it's changed or not.
        caseOneBalanceFour = card.getBalance();
        // After calculating the number of stops the passenger has travelled we could get the amount of
        // fare administrators should pay to keep the system operate properly; therefore daily report's
        // revenue should be deducted by such amount of administration fee. Now compare the daily revenue
        // of daily report to see whether it is the same as we expected.
        caseOneCostFour = adminUserOne.getTodayDailyReport().getDailyCost();
        // As the balance of the card does not change, the daily revenue of today's daily revenue does not change.
        // Now we check today daily report's daily revenue to see whether it's the same as we expected.
        caseOneRevenueFour = adminUserOne.getTodayDailyReport().getDailyRevenue();
        monthlyAverageCostOne = normalCardHolder.trackMonthlyAverageCost("2018-06-30 23:59:59 EDT");
        recentThreeTripsOne = normalCardHolder.viewRecentThreeTrips("2018-06-30 23:59:59 EDT");

        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        dateOne = adminUserOne.getTodayDailyReport().getDate();
        // Case Two : Now we analyze the case that the preceding trip is a subway trip and the following
        // trip is also a bus trip. In this case these two adjacent trips do share a cap, and we
        // trace all possible parameters generated to make sure it works exactly as we expected.
        trip3 = TripFactory.createNewTrip("Subway");
        // Add entre information to the newly generated subway trip.
        trip3.addEntreInformation(
                "2018-07-01 13:00:00 EDT", "Longueuil–Université-de-Sherbrooke", "000001");
        // After adding entre information, we get the card's current trip, and check whether such
        // current trip's entre time and entre staion are the same as the entre time and entre station
        // of trip3 respectively.
        caseTwoCurrentTripOne = card.getCurrentTrip();
        // For a subway trip, adding entre information should not alter the card's balance. Get the card
        // balance and check whether it is the same as we expected.
        caseTwoBalanceOne = card.getBalance();
        // As the balance of the card does not change, daily report's total revenue should not change.
        // Also, as the passenger just entered the station, the stops he/she travelled is zero;
        // therefore, daily report's total revenue should not change as well. Now compare daily report's
        // total revenue and total cost with figures we expected.
        caseTwoCostOne = adminUserOne.getTodayDailyReport().getDailyCost();
        caseTwoRevenueOne = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip1.
        trip3.addExitInformation("2018-07-01 13:40:00 EDT", "Université-de-Montréal", "000001");
        // After adding the exit information, we get the card's last trip, and check whether such trip's
        // exit information and exit time are the same as trip3.
        caseTwoLastTripOne = card.getLastTrip();
        // After riding the subway for over 12 stops, the maximum amount of fare that could be charged to
        // this card is 6 dollars. Now we get current balance to check whether the balance amount is
        // subtracted by six.
        caseTwoBalanceTwo = card.getBalance();
        // As the balance of the card is subtracted by 6, the total revenue of daily report should
        // increase by 6. Now compare daily report's daily cost with the figure we expected.
        caseTwoCostTwo = adminUserOne.getTodayDailyReport().getDailyCost();
        // After calculating the number of stops the passenger has travelled we could get the amount of
        // fare administrators should pay to keep the system operate properly; therefore daily report's
        // revenue should be deducted by such amount of administration fee. Now compare the daily
        // report's daily revenue to see whether it is the same as we expected.
        caseTwoRevenueTwo = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Now we create a new bus trip which shares the same cap with the preceding subway
        // trip.
        trip4 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated bus trip.
        trip4.addEntreInformation(
                "2018-07-01 13:45:00 EDT", "Université-de-Montréal", "000001", "Bus Route 3");
        // As trip 4 and trip 3 share the same cap, now we merge these two trips as one and set it as our new current trip. Now check whether the entre station
        // and entre time of the new current trip equals entre station and entre time of trip3 respectively.
        caseTwoCurrentTripTwo = card.getCurrentTrip();
        // As trip4 shares the same cap as trip one, it is capped at most 6. the amount of fare that the card should be
        // charged is 0. Get the card balance and check whether it does not change as we expected.
        caseTwoBalanceThree = card.getBalance();
        // As the balance of the card is subtracted by 2, the total revenue of daily report should
        // increase by 2. Now compare daily report's daily cost with the figure we expected.
        caseTwoCostThree = adminUserOne.getTodayDailyReport().getDailyCost();
        // As the passenger just entered subway station, the amount of change to total revenue should be
        // zero. Now check daily report's total revenue and whether they remain the same.
        caseTwoRevenueThree = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add entre information to the newly generated subway trip.
        trip4.addExitInformation("2018-07-01 13:55:00 EDT", "Viau", "000001");
        caseTwoLastTripTwo = card.getLastTrip();
        // Exiting a bus would not change the balance of this card. Now check whether the balance of this card remains
        // the same as expected.
        caseTwoBalanceFour = card.getBalance();
        // We could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue
        // is the same as expected.
        caseTwoCostFour = adminUserOne.getTodayDailyReport().getDailyCost();
        // Zero balance change means zero revenue change. Check whether the total revenue remains the same as expected.
        caseTwoRevenueFour = adminUserOne.getTodayDailyReport().getDailyRevenue();
        monthlyAverageCostTwo = normalCardHolder.trackMonthlyAverageCost("2018-07-01 14:00:00 EDT");
        recentThreeTripsTwo = normalCardHolder.viewRecentThreeTrips("2018-07-01 14:05:00 EDT");

        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        // Case Three : Now we analyze the case that the preceding trip is a subway trip and the following
        // trip is also a subway trip. In this case these two adjacent trips do not share a cap, and we
        // trace all possible parameters generated to make sure it works exactly as we expected.
        trip5 = TripFactory.createNewTrip("Subway");
        // Add entre information to the newly generated subway trip.
        trip5.addEntreInformation("2018-07-02 10:00:00 EDT", "Saint-Laurent", "000001");
        // Add exit information to trip5.
        trip5.addExitInformation("2018-07-02 10:10:00 EDT", "Mont-Royal", "000001");
        // After riding the subway for three stops and total time in metro system is less than two hours, the amount of
        // fare charged to this card equals the fare per stop times the number of stops passenger travelled. In this
        // case, the card should be charged 1.5 dollars. Now we compare the card balance with what we expected.
        caseThreeBalanceOne = card.getBalance();
        // After calculating the number of stops the passenger has travelled we could get the amount of
        // fare administrators should pay to keep the system operate properly; therefore daily report's
        // revenue should be deducted by such amount of administration fee. Now compare the daily
        // report's daily revenue to see whether it is the same as we expected.
        caseThreeCostOne = adminUserOne.getTodayDailyReport().getDailyCost();
        // Since the card is charged 1.5 dollars, the total revenue of today's daily report should increase by 1.5
        // Now we compare today's total revenue to see whether it's the same as we expected.
        caseThreeRevenueOne = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Now we create a new subway trip which does not share the same cap with the preceding subway
        // trip.
        trip6 = TripFactory.createNewTrip("Subway");
        // Add entre information to the trip6.
        trip6.addEntreInformation("2018-07-02 10:30:00 EDT", "Sherbrooke", "000001");
        // After adding entre information, we get the card's current trip, and check whether such
        // current trip's entre time and entre staion are the same as the entre time and entre station
        // of trip6.
        caseThreeCurrentTripOne = card.getCurrentTrip();
        // When a passenger entered the subway station the amount of fare charged to that card should be zero. Now we
        // check whether or not the balance remain the same.
        caseThreeBalanceTwo = card.getBalance();
        // Same as above, entering would not change daily cost of our daily report.
        caseThreeCostTwo = adminUserOne.getTodayDailyReport().getDailyCost();
        // As card balance remain the same, the total revenue of today's daily report does not change.
        caseThreeRevenueTwo = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add exit information of trip6.
        trip6.addExitInformation("2018-07-02 10:45:00 EDT", "Place-Saint-Henri", "000001");
        // After adding exit information, we get the card's last trip, and check whether such
        // current trip's exit time and exit station are the same as the exit time and exit station
        // of trip6 respectively.
        caseThreeLastTripOne = card.getLastTrip();
        // After riding the subway for nine stops and total time in metro system is less than two hours, the amount of
        // fare charged to this card equals the fare per stop times the number of stops passenger travelled. In this
        // case, the card should be charged 4.5 dollars. As last balance is one dollar, the new balance should be 9.
        // Now we compare the card balance with what we expected.
        caseThreeBalanceThree = card.getBalance();
        // Same as above, update daily report's total cost and check whether it's the same as we expected.
        caseThreeCostThree = adminUserOne.getTodayDailyReport().getDailyCost();
        // Same as above, add newly generated revenue, which is 4.5 dollars should be added to daily revenue.
        caseThreeRevenueThree = adminUserOne.getTodayDailyReport().getDailyRevenue();

        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        dateTwo = adminUserTwo.getTodayDailyReport().getDate();
        // Case Three: Now we analyze the case that the preceding trip is a subway trip and the following
        // trip is a subway trip. In this case these two adjacent trips do share a cap, because their extre time gap is within 2 hours
        //and we trace all possible parameters generated to make sure it works exactly as we expected.
        trip7 = TripFactory.createNewTrip("Subway");
        // Add entre information to the newly generated subway trip.
        trip7.addEntreInformation("2018-07-03 8:00:00 EDT", "Queen's Park", "000001");
        // After adding entre information, we get the card's current trip, and check whether such
        // current trip's entre time and entre staion are the same as the entre time and entre station
        // of trip7 respectively.
        caseFourCurrentTripOne = card.getCurrentTrip();
        // For a subway trip, adding entre information should not alter the card's balance. Get the card
        // balance and check whether it is the same as we expected.
        caseFourBalanceOne = card.getBalance();
        // As the balance of the card does not change, daily report's total revenue should not change.
        // Also, as the passenger just entered the station, the stops he/she travelled is zero;
        // therefore, daily report's total revenue should not change as well. Now compare daily report's
        // total revenue and total cost with figures we expected.
        caseFourCostOne = adminUserOne.getTodayDailyReport().getDailyCost();
        caseFourRevenueOne = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip7.
        trip7.addExitInformation("2018-07-03 8:10:00 EDT", "St George", "000001");
        // For a subway trip, adding entre information should not alter the card's balance. Get the card
        // balance and check whether it is the same as we expected.
        caseFourBalanceOne = card.getBalance();
        // As the balance of the card does not change, daily report's total revenue should not change.
        // Also, as the passenger just entered the station, the stops he/she travelled is zero;
        // therefore, daily report's total revenue should not change as well. Now compare daily report's
        // total revenue and total cost with figures we expected.
        caseFourCostOne = adminUserTwo.getTodayDailyReport().getDailyCost();
        caseFourRevenueOne = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Now we create a new subway trip which shares the same cap with the preceding subway
        // trip.
        trip8 = TripFactory.createNewTrip("Subway");
        // Add entre information to the newly generated subway trip.
        trip8.addEntreInformation("2018-07-03 8:12:00 EDT", "St George", "000001");
        // As trip 8 and trip 7 share the same cap, now we merge these two trips as one and set it as our new current trip.
        // Now check whether the entre station
        // and entre time of the new current trip equals entre station and entre time of trip3 respectively.
        caseFourCurrentTripOne = card.getCurrentTrip();
        // After riding the subway for over 12 stops, the maximum amount of fare that could be charged to
        // this card is 6 dollars. Now we get current balance to check whether the balance amount is
        // subtracted by six.
        caseFourBalanceTwo = card.getBalance();
        // As the balance of the card is subtracted by 6, the total revenue of daily report should
        // increase by 6. Now compare daily report's daily cost with the figure we expected.
        caseFourCostTwo = adminUserTwo.getTodayDailyReport().getDailyCost();
        // As the passenger just entered subway station, the amount of change to total revenue should be
        // zero. Now check daily report's total revenue and whether they remain the same.
        caseFourRevenueTwo = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Add exit information to the newly generated subway trip.
        trip8.addExitInformation("2018-07-03 10:01:00 EDT", "Kennedy", "000001");
        // Get the last trip of card and check whether it has the same entre station and entre time as the entre station
        // and entre time as trip 7 respectively.
        caseFourLastTripOne = card.getLastTrip();
        // After riding the subway for over twelves stops means that the amount of money charged for trip 8 equals the
        // the amount of unused cap, which is 5 dollars. therefore, the amount of money in card balance should decrease
        // by one.
        caseFourBalanceThree = card.getBalance();
        // we could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue
        // is the same as expected.
        caseFourCostThree = adminUserTwo.getTodayDailyReport().getDailyCost();
        // Card balance decreasing by one means the total revenue of today's daily report would increase by one.
        caseFourRevenueThree = adminUserTwo.getTodayDailyReport().getDailyRevenue();

        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        // Case Five : Now we analyze the case that the preceding trip is a bus trip and the following
        // trip is also a bus trip. In this case these two adjacent trips do not share a cap, and we
        // trace all possible parameters generated to make sure it works exactly as we expected.
        trip9 = TripFactory.createNewTrip("Bus");
        // Add entre information to newly created bus trip.
        trip9.addEntreInformation("2018-07-04 12:01:00 EDT", "Dundas", "000001", "Bus Route 505");
        // After adding entre information, we get the card's current trip, and check whether such
        // current trip's entre time and entre staion are the same as the entre time and entre station
        // of trip9.
        caseFiveCurrentTripOne = card.getCurrentTrip();
        // Entering a bus would cost two dollars to that card, now we check whether the card's balance
        // has already decreased by two.
        caseFiveBalanceOne = card.getBalance();
        // Entering a bus would not generate any admin cost; therefore, the total cost of daily report would not alter.
        caseFiveCostOne = adminUserTwo.getTodayDailyReport().getDailyCost();
        // As card's balance decreases by two, the total revenue of today's daily report would increase by two.
        caseFiveRevenueOne = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip9.
        trip9.addExitInformation("2018-07-04 14:02:00 EDT", "Spadina", "000001");
        // After adding the exit information, we get the card's last trip, and check whether such trip's
        // exit information and exit time are the same as trip1.
        caseFiveLastTripOne = card.getLastTrip();
        // Exiting a bus would not change the balance of this card. Now check whether the balance of this card remains
        // the same as expected.
        caseFiveBalanceTwo = card.getBalance();
        // Same as above, we could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue is the same as expected.
        caseFiveCostTwo = adminUserTwo.getTodayDailyReport().getDailyCost();
        // Zero balance change means zero revenue change. Check whether the total revenue remains the same as expected.
        caseFiveRevenueTwo = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Create a new bus trip.
        trip10 = TripFactory.createNewTrip("Bus");
        // Add entre information to this new trip.
        trip10.addEntreInformation("2018-07-04 14:06:00 EDT", "Spadina", "000001", "Bus Route 510");
        // After reading entre information we note trip 10 and trip 9 are two distinct trips.
        // Get the current trip of the card to check whether the current trip of card equals to trip 10.
        caseFiveCurrentTripTwo = card.getCurrentTrip();
        // For a distinct trip, entering a bus would cause the balance of the card be deducted by two.
        caseFiveBalanceThree = card.getBalance();
        // Entering a bus would not generate any admin cost; therefore, the total cost of daily report would not alter.
        caseFiveCostThree = adminUserTwo.getTodayDailyReport().getDailyCost();
        // As card's balance decreases by two, the total revenue of today's daily report would increase by two.
        caseFiveRevenueThree = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip 10.
        trip10.addExitInformation("2018-07-04 14:20:00 EDT", "Union", "000001");
        // After adding the exit information, we get the card's last trip, and check whether such trip's
        // exit information and exit time are the same as trip1.
        caseFiveLastTripTwo = card.getLastTrip();
        // Exiting a bus would not change the balance of this card. Now check whether the balance of this card remains
        // the same as expected.
        caseFiveBalanceFour = card.getBalance();
        // Same as above, we could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue is the same as expected.
        caseFiveCostFour = adminUserTwo.getTodayDailyReport().getDailyCost();
        // Zero balance change means zero revenue change. Check whether the total revenue remains the same as expected.
        caseFiveRevenueFour = adminUserTwo.getTodayDailyReport().getDailyRevenue();

        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        // Case Six: Now we analyze the case that the preceding trip is a bus trip and the following
        // trip is a bus trip. In this case these two adjacent trips do share a cap,
        //and we trace all possible parameters generated to make sure it works exactly as we expected.
        trip11 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated subway trip.
        trip11.addEntreInformation("2018-07-05 12:00:00 EDT", "High Park", "000001", "Bus Route 506");
        // The cost for a bus trip is 2 dollars, which is directly subtracted from balance.
        caseSixBalanceOne = card.getBalance();
        // As the balance of the card is reduced by 2, daily report's total revenue should will increase by 2.
        // Also, as the passenger just entered the station, the stops he/she travelled is zero;
        // therefore, daily report's total revenue increase by 2. Now compare daily report's
        // total revenue and total cost with figures we expected.
        caseSixCostOne = adminUserTwo.getTodayDailyReport().getDailyCost();
        caseSixRevenueOne = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip11.
        trip11.addExitInformation("2018-07-05 12:10:00 EDT", "Bathurst", "000001");
        // get last trip of the bus.
        caseSixLastTripOne = card.getLastTrip();
        // when we tap off the bus, the balance would not change.
        caseSixBalanceTwo = card.getBalance();
        // As the balance of the card is subtracted by 2, the total revenue of daily report should
        // increase by 2. Now compare daily report's daily cost with the figure we expected.
        caseSixCostTwo = adminUserTwo.getTodayDailyReport().getDailyCost();
        caseSixRevenueTwo = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Now we create a new subway trip which shares the same cap with the preceding bus
        // trip.
        trip12 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated bus trip.
        trip12.addEntreInformation("2018-07-05 12:11:59 EDT", "Bathurst", "000001", "Bus Route 509");
        // ada exit information to the trip, which cost 2 dollars and within the cap.
        trip12.addExitInformation("2018-07-05 12:59:11 EDT", "Union", "000001");
        // Now we create a new bus trip which shares the same cap with the preceding bus trip.
        trip13 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated bus trip.
        trip13.addEntreInformation("2018-07-05 13:00:00 EDT", "Union", "000001", "Bus Route 510");
        // Add exit information to the newly generated bus trip. This is capped as well.
        trip13.addExitInformation("2018-07-05 13:09:59 EDT", "Dundas West", "000001");
        // Now we create a new bus trip which shares the same cap with the preceding bus trip.
        trip14 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated bus trip.
        trip14.addEntreInformation("2018-07-05 13:11:00 EDT", "Dundas West", "000001", "Bus Route 505");
        // As trip 11, 12, 13, 14 share the same cap, now we merge these 4 trips as one and set it as our new current trip. Now check whether the entre station
        // and entre time of the new current trip equals entre station and entre time of trip3 respectively.
        caseSixCurrentTripOne = card.getCurrentTrip();
        //Because within the cap, the trip only cost 6 dollars.
        caseSixBalanceThree = card.getBalance();
        // The first 3 trips cost 6 dollars in total, and the last one is capped, so charge 0. Now compare daily report's daily cost
        // with the figure we expected.
        caseSixCostThree = adminUserTwo.getTodayDailyReport().getDailyCost();
        caseSixRevenueThree = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip14.
        trip14.addExitInformation("2018-07-05 13:20:00 EDT", "Danforth Ave", "000001");
        // get last trip of the bus.
        caseSixLastTripTwo = card.getLastTrip();
        //The exit information does not affect the charge and revenue.
        caseSixBalanceFour = card.getBalance();
        // Same as above, we could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue is the same as expected.
        caseSixCostFour = adminUserTwo.getTodayDailyReport().getDailyCost();
        // Zero balance change means zero revenue change. Check whether the total revenue remains the same as expected.
        caseSixRevenueFour = adminUserTwo.getTodayDailyReport().getDailyRevenue();
        recentThreeTripsThree = normalCardHolder.viewRecentThreeTrips("2018-07-05 18:00:00 EDT");

        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        // Case Seven: Now we analyze the case that the preceding trip is a bus trip and the following
        // trip is a subway trip. In this case these two adjacent trips do not share a cap, and we
        // trace all possible parameters generated to make sure it works exactly as we expected.
        trip15 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated bus trip.
        trip15.addEntreInformation(
                "2018-07-06 17:00:00 EDT", "Square-Victoria-OACI", "000001", "Bus Route 2");
        dateThree = adminUserOne.getTodayDailyReport().getDate();
        // The cost for a bus trip is 2 dollars, which is directly subtracted from balance.
        caseSevenBalanceOne = card.getBalance();
        // As the balance of the card is subtracted by 2, the total revenue of daily report should
        // increase by 2. Now compare daily report's daily cost with the figure we expected.
        caseSevenCostOne = adminUserOne.getTodayDailyReport().getDailyCost();
        caseSevenRevenueOne = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip15.
        trip15.addExitInformation("2018-07-06 17:30:00 EDT", "Crémazie", "000001");
        // The exit information does not affect the charge and revenue.
        caseSevenBalanceTwo = card.getBalance();
        // Same as above, we could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue is the same as expected.
        caseSevenCostTwo = adminUserOne.getTodayDailyReport().getDailyCost();
        // Zero balance change means zero revenue change. Check whether the total revenue remains the same as expected.
        caseSevenRevenueTwo = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Now we create a new subway trip which does not share the same cap with the preceding bus trip.
        trip16 = TripFactory.createNewTrip("Subway");
        // Add entre information to the newly generated subway trip.
        trip16.addEntreInformation("2018-07-06 17:40:00 EDT", "Henri-Bourassa", "000001");
        // After reading entre information we note trip 15 and trip 16 are two distinct trips.
        // Get the current trip of the card to check whether the current trip of card equals to trip 16.
        caseSevenCurrentTripOne = card.getCurrentTrip();
        // For a subway trip, adding entre information should not alter the card's balance. Get the card
        // balance and check whether it is the same as we expected.
        caseSevenBalanceThree = card.getBalance();
        // Entering a subway would not generate any admin cost; therefore, the total cost of daily report would not alter.
        caseSevenCostThree = adminUserOne.getTodayDailyReport().getDailyCost();
        // The total revenue of today's daily report would not change since balance did not change.
        caseSevenRevenueThree = adminUserOne.getTodayDailyReport().getDailyRevenue();
        // Add exit information to trip16.
        trip16.addExitInformation("2018-07-06 17:55:00 EDT", "De La Conorde", "000001");
        // After adding the exit information, we get the card's last trip, and check whether such trip's
        // exit information and exit time are the same as trip15.
        caseSevenLastTripOne = card.getLastTrip();
        // After riding two stops, the balance should be deducted by 1 dollar.
        caseSevenBalanceFour = card.getBalance();
        // Same as above, we could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue is the same as expected.
        caseSevenCostFour = adminUserOne.getTodayDailyReport().getDailyCost();
        // A one-dollar decrease in balance indicates a one-dollar increase in revenue.
        caseSevenRevenueFour = adminUserOne.getTodayDailyReport().getDailyRevenue();

        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        // Case Seven: Now we analyze the case that the preceding trip is a bus trip and the following
        // trip is a subway trip. In this case these two adjacent trips do share a cap, and we
        // trace all possible parameters generated to make sure it works exactly as we expected.
        trip17 = TripFactory.createNewTrip("Bus");
        // Add entre information to the newly generated bus trip.
        trip17.addEntreInformation("2018-07-07 23:00:00 EDT", "Anjou", "000001", "Bus Route 3");
        // The cost for a bus trip is 2 dollars, which is directly subtracted from balance.
        caseEightBalanceOne = card.getBalance();
        // As the balance of the card is subtracted by 2, the total revenue of daily report should
        // increase by 2. Now compare daily report's daily cost with the figure we expected.
        caseEightCostOne = adminUserOne.getTodayDailyReport().getDailyCost();
        // Zero balance change means zero revenue change. Check whether the total revenue remains the same as expected.
        caseEightRevenueOne = adminUserOne.getTodayDailyReport().getDailyRevenue();
        recentThreeTripsFour = normalCardHolder.viewRecentThreeTrips("2018-07-07 23:01:00");
        // Add exit information to trip17.
        trip17.addExitInformation("2018-07-07 23:40:00 EDT", "Snowdon", "000001");
        // The exit information does not affect the balance and revenue.
        caseEightBalanceTwo = card.getBalance();
        // Same as above, we could get the total admin cost after knowing how many stops the passenger travelled.
        // Adding such amount of admin cost to total revenue and check whether the total revenue is the same as expected.
        caseEightCostTwo = adminUserOne.getTodayDailyReport().getDailyCost();
        // Zero balance change means zero revenue change. Check whether the total revenue remains the same as expected.
        caseEightRevenueTwo = adminUserOne.getTodayDailyReport().getDailyRevenue();
        trip18 = TripFactory.createNewTrip("Subway");
        // Add entre information to the newly generated subway trip.
        trip18.addEntreInformation("2018-07-07 23:45:00 EDT", "Snowdon", "000001");
        caseEightCurrentTripOne = card.getCurrentTrip();
        // For a subway trip, adding entre information should not alter the card's balance. Get the card
        // balance and check whether it is the same as we expected.
        caseEightBalanceThree = card.getBalance();
        caseEightCostThree = adminUserOne.getTodayDailyReport().getDailyCost();
        caseEightRevenueThree = adminUserOne.getTodayDailyReport().getDailyRevenue();
        adminUserOne.generateDailyReport();
        adminUserTwo.generateDailyReport();
        dateFour = adminUserOne.getTodayDailyReport().getDate();
        // Add exit information to trip18.
        trip18.addExitInformation("2018-07-08 00:00:01 EDT", "Montmorency", "000001");
        caseEightLastTripOne = card.getLastTrip();
        caseEightBalanceFour = card.getBalance();
        caseEightCostFour = adminUserOne.getTodayDailyReport().getDailyCost();
        caseEightRevenueFour = adminUserOne.getTodayDailyReport().getDailyRevenue();
        monthlyAverageCostThree = normalCardHolder.trackMonthlyAverageCost("2018-07-08 00:00:59");
    }

    /**
     * Test the number of stops between "Angrignon" station on "Subway Route Green" and the next station "Monk" which is also on
     * "Subway Route Green". As these two stations are adjacent, the distance between "Angrignon" and "Monk" should be
     * one.
     */
    @Test
    public void testCalculateDistanceCase1() {
        int result = SubwayGraph.calculateDistance("Angrignon", "Monk");
        assertEquals(result, 1);
    }

    /**
     * Test the shortest distance between "Angrignon" station on "Subway Route Green" and "Côte-Vertu" station on "Subway Route Orange"
     * After graphing all four metro lines we find out the number of stops between "Angrignon" and "Côte-Vertu" should be 17.
     */
    @Test
    public void testCalculateDistanceCase2() {

        int result = SubwayGraph.calculateDistance("Angrignon", "Côte-Vertu");
        assertEquals(result, 17);
    }

    /**
     * Test the shortest distance between "Square-Victoria-OACI" station on "Subway Route Orange" and "Édouard-Montpetit" station on "Subway Route Blue"
     * After graphing all four metro lines we find out the number of stops between "Angrignon" and "Côte-Vertu" should be 11.
     */
    @Test
    public void testCalculateDistanceCase3() {
        int result = SubwayGraph.calculateDistance("Square-Victoria-OACI", "Édouard-Montpetit");
        assertEquals(result, 11);
    }

    /**
     * Test the shortest distance between "Snowdon" station and "Berri-UQAM" station.
     */
    @Test
    public void testCalculateDistanceCase4() {
        int result = SubwayGraph.calculateDistance("Snowdon", "Berri-UQAM");
        assertEquals(result, 11);
    }

    /**
     * Test the shortest distance between "Longueuil–Université-de-Sherbrooke" station and "Université-de-Montréal" station.
     */
    @Test
    public void testCalculateDistanceCase5() {
        int result =
                SubwayGraph.calculateDistance(
                        "Longueuil–Université-de-Sherbrooke", "Université-de-Montréal");
        assertEquals(result, 14);
    }

    /**
     * Test the shortest path we find on map between "Angrignon" station and "Monk" station, which is represented by an array list
     * consisting of string representations all station names, is exactly the same as the shortest path we generated by
     * getShortestPath method.
     */
    @Test
    public void testGetShortestPathCase1() {
        try {
            ArrayList<String> result = SubwayGraph.getShortestPath("Angrignon", "Monk");
            ArrayList<String> path = new ArrayList<>();
            path.add("Angrignon");
            path.add("Monk");
            assertTrue(result.equals(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the shortest path we find on map between "Angrignon" station and "Côte-Vertu" station, which is represented by an array list
     * consisting of string representations all station names, is exactly the same as the shortest path we generated by
     * getShortestPath method.
     */
    @Test
    public void testGetShortestPathCase2() {
        try {
            ArrayList<String> result = SubwayGraph.getShortestPath("Angrignon", "Côte-Vertu");
            String[] array =
                    new String[] {
                            "Angrignon",
                            "Monk",
                            "Jolicoeur",
                            "Verdun",
                            "De l'Église",
                            "LaSalle",
                            "Charlevoix",
                            "Lionel-Groulx",
                            "Place-Saint-Henri",
                            "Vendôme",
                            "Villa-Maria",
                            "Snowdon",
                            "Côte-Saint-Catharine",
                            "Plamonton",
                            "Namur",
                            "De La Savane",
                            "Du Collège",
                            "Côte-Vertu"
                    };
            ArrayList<String> path = new ArrayList<>(Arrays.asList(array));
            assertTrue(result.equals(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the distance between "Snowdon" station and "Berri-UQAM" station, which is calculated by the method of
     * calculateDistance  in the class of SubwayGraph, is exactly the same as the length of ArrayList of pathCollection
     * stored in the SubwayGraph which constructs Pair of "Snowdon" station and "Berri-UQAM" station minus one.
     */
    @Test
    public void testDistanceCollectionInCalculateDistanceCase() {
        int result01 = SubwayGraph.calculateDistance("Snowdon", "Berri-UQAM");
        int result02 =
                SubwayGraph.getPathCollection().get(new Pair<>("Snowdon", "Berri-UQAM")).size() - 1;
        assertEquals(result01, result02);
    }

    /**
     * Test the number of stops between stop "Spadina" and stop "Union" on "Bus Route 510" equals 10.
     */
    @Test
    public void testcalculateNumOfStops_01() {
        int result = Main.busRoutesMap.get("Bus Route 510").calculateNumberOfStops("Spadina", "Union");
        assertEquals(result, 10);
    }

    /**
     * Test the number of stops between stop "Broadview" and stop "Dundas" on "Bus Route 505" equals to 6.d
     */
    @Test
    public void testcalculateNumOfStops_02() {
        int result =
                Main.busRoutesMap.get("Bus Route 505").calculateNumberOfStops("Broadview", "Dundas");
        assertEquals(result, 6);
    }

    /**
     * Test after initializing a card holder, this card holder's name and email address should be exactly the same as the name and
     * email address in constructor.
     */
    @Test
    public void testInitializeCardHolder() {
        assertEquals(normalCardHolder.getName(), "Anna Martinez");
        assertEquals(normalCardHolder.getEmailAddress(), "annamartinez@gmail.com");
    }

    /**
     * Test after initializing a card, this card's number should be exactly the same as the card number in its constructor.
     */
    @Test
    public void testInitializeCard() {
        assertEquals(card.getCardNumber(), "000001");
    }

    /**
     * Test after adding a card to a card holder, the card holder should be exactly the same as what we could get by calling
     * getCardHolder method on this card.
     */
    @Test
    public void testAddCardToCardHolder() {
        Card card = new Card("000002");
        normalCardHolder.addCard(card, "2018-06-30 10:01:00 EDT");
        assertEquals(card.getNormalCardHolder(), normalCardHolder);
    }

    /**
     * Test after loading money onto a card, the balance of this card should increase by the amount we loaded.
     */
    @Test
    public void testLoadMoney() {
        Card card = new Card("000009");
        normalCardHolder.addCard(card, "2018-06-30 10:02:00 EDT");
        card.setBalance(1);
        card.loadMoney("10", "12/31/2018 10:00:00");
        assertEquals(card.getBalance(), 11, 0.1);
    }

    /**
     * Test after resetting a new name to an card holder, the new name should be exactly the same as what we get by calling
     * getName method on card holder
     */
    @Test
    public void testResetCardHolderName() {
        normalCardHolder.setName("Alejandro Lopez");
        assertEquals(normalCardHolder.getName(), "Alejandro Lopez");
    }

    /**
     * Test when a card holder's card was stolen, we could replace the stolen card with a new one
     * and the new card would have the same balance as the original one.
     */
    @Test
    public void testReplaceStolenCard() {
        Card oldCard = card;
        oldCard.setBalance(15);
        normalCardHolder.addCard(oldCard, "2018-12-31 10:00:00 EDT");
        normalCardHolder.replaceStolenCard(oldCard.getCardNumber(), "000003");
        assertEquals(Main.cardMap.get("000003").getBalance(), 15, 0.1);
    }

    /**
     * Case One : Now we analyze the case that the preceding trip is a subway trip and the
     * following trip is a bus trip. In this case these two adjacent trips do not share a cap, and
     * we trace all possible parameters generated to make sure it works exactly as we expected. In
     * trip1, the passenger entered subway "McGill" subway station at 11:00:00, and entering a
     * subway station would not balance or daily revenue change or total cost change. We test whether
     * the entre station and entre time of trip 1 would be the same the card's current trip's entre
     * station and entre time respectively; and we also test whether the card's balance, total revenue
     * and total cost remain 19, 0, 0, respectively.
     */
    @Test
    public void testSubwayToBusEntreInformationCase1Step1() {
        assertEquals(trip1.getEntreTime(), "2018-06-30 11:00:00 EDT");
        assertEquals(trip1.getEntreStation(), "McGill");
        assertEquals(
                trip1.getTripInformation().get(0), "entres McGill station at 2018-06-30 11:00:00 EDT");
        assertEquals(caseOneCurrentTripOne.getEntreTime(), "2018-06-30 11:00:00 EDT");
        assertEquals(caseOneCurrentTripOne.getEntreStation(), "McGill");
        assertEquals(caseOneBalanceOne, 19, 0.01);
        assertEquals(caseOneCostOne, 0, 0.01);
        assertEquals(caseOneRevenueOne, 0, 0.01);
    }

    /**
     * The passenger stayed in the subway system for over two hours, which means this subway trip would cost
     * six dollars to his card. As he/she has already exited the subway system, the last trip stored on this
     * card would be exactly the same as trip 1. As he travelled 3 stops, which would generate 0.9 dollar
     * cost to administrators. Now we test whether the last trip equals to trip1, and whether the card balance
     * would be deduced by 6, the total revenue would increase by 6, and total cost would increase by 0.9
     */
    @Test
    public void testSubwayToBusExitInformationCase1Step1() {
        assertEquals(trip1.getExitTime(), "2018-06-30 13:05:00 EDT");
        assertEquals(trip1.getExitStation(), "Berri-UQAM");
        assertEquals(caseOneLastTripOne, trip1);
        assertEquals(
                caseOneLastTripOne.getTripInformation().get(0),
                "entres McGill station at 2018-06-30 11:00:00 EDT");
        assertEquals(
                caseOneLastTripOne.getTripInformation().get(1),
                "exits Berri-UQAM station at 2018-06-30 13:05:00 EDT");
        assertEquals(
                caseOneLastTripOne.toString(),
                "Card 000001 entres McGill station at 2018-06-30 11:00:00 EDT\n"
                        + ", then exits Berri-UQAM station at 2018-06-30 13:05:00 EDT");
        assertEquals(caseOneBalanceTwo, 13, 0.01);
        assertEquals(caseOneCostTwo, 0.9, 0.01);
        assertEquals(caseOneRevenueTwo, 6, 0.01);
    }

    /**
     * As the entre time of the new bus trip and the entre time of the preceding subway trip are
     * out of two hours, the newly generated trip does not share the same cap with trip1, which means
     * trip2 and trip1 are two distinct trips. In trip2, the passenger entered subway "Berri-UQAM"
     * bus stop at 11:00:00, and boarding a bus would increase balance by two or daily revenue
     * also increase by two. We test whether the entre station and entre time of trip 2 would
     * be the same the card's current trip's entre station and entre time respectively; and we also
     * test whether the card's balance, total revenue would increase by two and total cost would remain
     * the same.
     */
    @Test
    public void testSubwayToBusEntreInformationCase1Step2() {
        //        assertFalse(trip2.shareCap(trip1));
        assertEquals(trip2.getEntreTime(), "2018-06-30 13:10:00 EDT");
        assertEquals(trip2.getEntreStation(), "Berri-UQAM");
        assertEquals(caseOneCurrentTripTwo.getEntreTime(), "2018-06-30 13:10:00 EDT");
        assertEquals(caseOneCurrentTripTwo.getEntreStation(), "Berri-UQAM");
        assertEquals(caseOneBalanceThree, 11, 0.01);
        assertEquals(caseOneCostThree, 0.9, 0.01);
        assertEquals(caseOneRevenueThree, 8, 0.01);
    }

    /**
     * As he/she has already exited the bus system, the last trip stored on this
     * card would be exactly the same as trip 2. For this bus trip, which would generate 1.8 dollar
     * cost to administrators since there are . Now we test whether the last trip equals to trip2, and whether the card balance
     * would remain same, the total revenue would remain same, and total cost would increase by 1.8 for a bus trip's exit.
     */
    @Test
    public void testSubwayToBusExitInformationCase1Step2() {
        assertEquals(trip2.getExitTime(), "2018-06-30 13:25:00 EDT");
        assertEquals(trip2.getExitStation(), "Jolicoeur");
        assertEquals(caseOneLastTripTwo, trip2);
        assertEquals(
                caseOneLastTripTwo.getTripInformation().get(0),
                "entres Berri-UQAM stop at 2018-06-30 13:10:00 EDT");
        assertEquals(
                caseOneLastTripTwo.getTripInformation().get(1),
                "exits Jolicoeur stop at 2018-06-30 13:25:00 EDT");
        assertEquals(
                caseOneLastTripTwo.toString(),
                "Card 000001 entres Berri-UQAM stop at 2018-06-30 13:10:00 EDT\n"
                        + ", then exits Jolicoeur stop at 2018-06-30 13:25:00 EDT");
        assertEquals(caseOneBalanceFour, 11, 0.01);
        assertEquals(caseOneCostFour, 2.7, 0.01);
        assertEquals(caseOneRevenueFour, 8, 0.01);
    }

    /**
     * Case Two : Now we analyze the case that the preceding trip is a subway trip and the
     * following trip is a bus trip. In this case these two adjacent trips share a cap, and
     * we trace all possible parameters generated to make sure it works exactly as we expected. In
     * trip1, the passenger entered subway "Longueuil–Université-de-Sherbrooke" subway station at 13:00:00, and entering a
     * subway station would not balance or daily revenue change or total cost change. We test whether
     * the entre station and entre time of trip 3 would be the same the card's current trip's entre
     * station and entre time respectively; and we also test whether the card's balance, total revenue
     * and total cost remain 11, 0, 0, respectively.
     *
     * @throws ParseException
     */

    @Test
    public void testSubwayToBusEntreInformationCase2Step1() throws ParseException {
        assertEquals(dateOne, "2018-07-01");
        assertFalse(trip3.shareCap(trip2));
        assertEquals(trip3.getEntreTime(), "2018-07-01 13:00:00 EDT");
        assertEquals(trip3.getEntreStation(), "Longueuil–Université-de-Sherbrooke");
        assertEquals(
                trip3.getTripInformation().get(0),
                "entres Longueuil–Université-de-Sherbrooke station at 2018-07-01 13:00:00 EDT");
        assertEquals(caseTwoCurrentTripOne.getEntreTime(), "2018-07-01 13:00:00 EDT");
        assertEquals(caseTwoCurrentTripOne.getEntreStation(), "Longueuil–Université-de-Sherbrooke");
        assertEquals(caseTwoBalanceOne, 11, 0.01);
        assertEquals(caseTwoCostOne, 0, 0.01);
        assertEquals(caseTwoRevenueOne, 0, 0.01);
    }

    // In this case the passenger travelled more than (cap/fare per stop)

    /**
     * The passenger stayed in the subway system within two hours, As he/she has already exited the subway system,
     * the last trip stored on this
     * card would be exactly the same as trip 3. As he travelled 3 stops, which would generate 4.2 dollar
     * cost to administrators. Now we test whether the last trip equals to trip1, and whether the card balance
     * would be deduced by 6, the total revenue would increase by 6, and total cost would increase by 4.2
     */
    @Test
    public void testSubwayToBusExitInformationCase2Step1() {
        assertEquals(trip3.getExitTime(), "2018-07-01 13:40:00 EDT");
        assertEquals(trip3.getExitStation(), "Université-de-Montréal");
        assertEquals(caseTwoLastTripOne, trip3);
        assertEquals(
                caseTwoLastTripOne.getTripInformation().get(0),
                "entres Longueuil–Université-de-Sherbrooke station at 2018-07-01 13:00:00 EDT");
        assertEquals(
                caseTwoLastTripOne.getTripInformation().get(1),
                "exits Université-de-Montréal station at 2018-07-01 13:40:00 EDT");
        assertEquals(caseTwoBalanceTwo, 5, 0.01);
        assertEquals(caseTwoCostTwo, 4.2, 0.01);
        assertEquals(caseTwoRevenueTwo, 6, 0.01);
    }

    // In this case connecting trip share the cap with the previous one.

    /**
     * As the entre time of the new bus trip and the entre time of the preceding subway trip are
     * within two hours, the newly generated trip shares the same cap with trip3, which means
     * trip4 and trip3 are jointed trips. In trip4, the passenger entered subway "Longueuil–Université-de-Sherbrooke"
     * bus stop at 13:00:00, and boarding a bus would increase balance by two or daily revenue
     * also increase by two. We test whether the entre station and entre time of trip 3 would
     * be the same the card's current trip's entre station and entre time respectively; and we also
     * test whether the card's balance, total revenue and total cost would keep unchanged.
     *
     */
    @Test
    public void testSubwayToBusEntreInformationCase2Step2() {
        assertEquals(trip4.getEntreTime(), "2018-07-01 13:00:00 EDT");
        assertEquals(trip4.getEntreStation(), "Longueuil–Université-de-Sherbrooke");
        assertEquals(caseTwoCurrentTripTwo.getEntreTime(), "2018-07-01 13:00:00 EDT");
        assertEquals(caseTwoCurrentTripTwo.getEntreStation(), "Longueuil–Université-de-Sherbrooke");
        assertEquals(caseTwoBalanceThree, 5, 0.01);
        assertEquals(caseTwoCostThree, 4.2, 0.01);
        assertEquals(caseTwoRevenueThree, 6, 0.01);
    }

    /**
     * As he/she has already exited the subway system, the last trip stored on this
     * card would be exactly the same as trip 4. For this subway trip, which would generate 0 dollar
     * cost to administrators because of within one cap. Now we test whether the last trip equals to trip4, and whether the card balance
     * would remain same, the total revenue would remain same, and total cost would increase by 1.65 for this subway trip's exit.
     */
    @Test
    public void testSubwayToBusExitInformationCase2Step2() {
        assertEquals(trip4.getExitTime(), "2018-07-01 13:55:00 EDT");
        assertEquals(trip4.getExitStation(), "Viau");
        assertEquals(trip4.getLastEntreStation(), "Université-de-Montréal");
        assertEquals(caseTwoLastTripTwo, trip4);
        assertEquals(
                caseTwoLastTripTwo.getTripInformation().get(0),
                "entres Longueuil–Université-de-Sherbrooke station at 2018-07-01 13:00:00 EDT");
        assertEquals(
                caseTwoLastTripTwo.getTripInformation().get(1),
                "exits Université-de-Montréal station at 2018-07-01 13:40:00 EDT");
        assertEquals(
                caseTwoLastTripTwo.getTripInformation().get(2),
                "entres Université-de-Montréal stop at 2018-07-01 13:45:00 EDT");
        assertEquals(
                caseTwoLastTripTwo.getTripInformation().get(3),
                "exits Viau stop at 2018-07-01 13:55:00 EDT");
        assertEquals(
                caseTwoLastTripTwo.toString(),
                "Card 000001 entres Longueuil–Université-de-Sherbrooke station at 2018-07-01 13:00:00 EDT\n"
                        + ", then exits Université-de-Montréal station at 2018-07-01 13:40:00 EDT\n"
                        + ", then entres Université-de-Montréal stop at 2018-07-01 13:45:00 EDT\n"
                        + ", then exits Viau stop at 2018-07-01 13:55:00 EDT");
        assertEquals(caseTwoBalanceFour, 5, 0.01);
        assertEquals(caseTwoCostFour, 5.85, 0.01);
        assertEquals(caseTwoRevenueFour, 6, 0.01);
    }

    /**
     * We test whether the card's balance, total revenue
     * and total cost remain 3.5, 0.9, 1.5, respectively.
     *
     * @throws ParseException
     */
    // In this case the passenger is charged according to number of stops he/she travelled
    @Test
    public void testSubwayToSubwayExitInformationCase1Step1() {
        assertEquals(caseThreeBalanceOne, 3.5, 0.01);
        assertEquals(caseThreeCostOne, 0.9, 0.01);
        assertEquals(caseThreeRevenueOne, 1.5, 0.01);
    }

    /**
     * Case Three : Now we analyze the case that the preceding trip is a subway trip and the
     * following trip is a subway trip. In this case these two disjointed trips, and
     * we trace all possible parameters generated to make sure it works exactly as we expected. In
     * trip6, the passenger entered subway "Sherbrooke" subway station at 10:30:00, and entering a
     * subway station would not balance or daily revenue change or total cost change. We test whether
     * the entre station and entre time of trip 6 would be the same the card's current trip's entre
     * station and entre time respectively; and we also test whether the card's balance, total revenue
     * and total cost remain 3.5, 0.9, 1.5, respectively.
     *
     * @throws ParseException
     */
    // In this case connecting trip does not share the cap with the previous one.
    @Test
    public void testSubwayToSubwayEntreInformationCase1Step2() {
        assertEquals(trip6.getEntreTime(), "2018-07-02 10:30:00 EDT");
        assertEquals(trip6.getEntreStation(), "Sherbrooke");
        assertEquals(caseThreeCurrentTripOne.getEntreTime(), "2018-07-02 10:30:00 EDT");
        assertEquals(caseThreeCurrentTripOne.getEntreStation(), "Sherbrooke");
        assertEquals(caseThreeBalanceTwo, 3.5, 0.01);
        assertEquals(caseThreeCostTwo, 0.9, 0.01);
        assertEquals(caseThreeRevenueTwo, 1.5, 0.01);
    }

    /**
     * The passenger stayed in the subway system within two hours, As he/she has already exited the subway system,
     * the last trip stored on this
     * card would be exactly the same as trip 6. For this trip, which would generate 4.5 dollar
     * cost to administrators. Now we test whether the last trip equals to trip6, and whether the card balance
     * would be increased by 6 with automatic money loading, the total revenue would increase by 4.5,
     * and total cost would increase by 2.7
     */
    @Test
    public void testSubwayToSubwayExitInformationCase1Step2() {
        assertEquals(trip6.getExitTime(), "2018-07-02 10:45:00 EDT");
        assertEquals(trip6.getExitStation(), "Place-Saint-Henri");
        assertEquals(trip6.getLastEntreStation(), "Sherbrooke");
        assertEquals(caseThreeLastTripOne, trip6);
        assertEquals(
                caseThreeLastTripOne.getTripInformation().get(0),
                "entres Sherbrooke station at 2018-07-02 10:30:00 EDT");
        assertEquals(
                caseThreeLastTripOne.getTripInformation().get(1),
                "exits Place-Saint-Henri station at 2018-07-02 10:45:00 EDT");
        assertEquals(caseThreeBalanceThree, 9, 0.01);
        assertEquals(caseThreeCostThree, 3.6, 0.01);
        assertEquals(caseThreeRevenueThree, 6, 0.01);
    }

    /**
     * We test whether the card's balance, total revenue
     * and total cost remain 8, 0.64, 1.0, respectively, and dailyReport's date is "2018-07-03".
     *
     * @throws ParseException
     */
    // city, and the way to calculate cost updated.
    @Test
    public void testSubwayToSubwayExitInformationCase2Step1() {
        assertEquals(dateTwo, "2018-07-03");
        assertEquals(caseFourBalanceOne, 8, 0.01);
        assertEquals(caseFourCostOne, 0.64, 0.01);
        assertEquals(caseFourRevenueOne, 1.0, 0.01);
    }

    /**
     * As the entre time of the new bus trip and the entre time of the preceding subway trip are
     * within two hours, the newly generated trip shares the same cap with trip3, which means
     * trip4 and trip3 are jointed trips. In trip4, the passenger entered subway "Longueuil–Université-de-Sherbrooke"
     * bus stop at 13:00:00, and boarding a bus would increase balance by two or daily revenue
     * also increase by two. We test whether the entre station and entre time of trip 3 would
     * be the same the card's current trip's entre station and entre time respectively; and we also
     * test whether the card's balance, total revenue and total cost would keep unchanged.
     *
     */
    // In this case connecting trip does not share the cap with the previous one.
    @Test
    public void testSubwayToSubwayEntreInformationCase2Step2() {
        assertEquals(trip8.getEntreTime(), "2018-07-03 8:00:00 EDT");
        assertEquals(trip8.getEntreStation(), "Queen's Park");
        assertEquals(caseFourCurrentTripOne.getEntreTime(), "2018-07-03 8:00:00 EDT");
        assertEquals(caseFourCurrentTripOne.getEntreStation(), "Queen's Park");
        assertEquals(caseFourBalanceTwo, 8.0, 0.01);
        assertEquals(caseFourCostTwo, 0.64, 0.01);
        assertEquals(caseFourRevenueTwo, 1.0, 0.01);
    }

    /**
     *Tests exit information for the second subway trip
     *
     */
    @Test
    public void testSubwayToSubwayExitInformationCase2Step2() {
        assertEquals(trip8.getExitTime(), "2018-07-03 10:01:00 EDT");
        assertEquals(trip8.getExitStation(), "Kennedy");
        assertEquals(trip8.getLastEntreStation(), "St George");
        assertEquals(caseFourLastTripOne, trip8);
        assertEquals(
                caseFourLastTripOne.getTripInformation().get(0),
                "entres Queen's Park station at 2018-07-03 8:00:00 EDT");
        assertEquals(
                caseFourLastTripOne.getTripInformation().get(1),
                "exits St George station at 2018-07-03 8:10:00 EDT");
        assertEquals(
                caseFourLastTripOne.getTripInformation().get(2),
                "entres St George station at 2018-07-03 8:12:00 EDT");
        assertEquals(
                caseFourLastTripOne.getTripInformation().get(3),
                "exits Kennedy station at 2018-07-03 10:01:00 EDT");
        assertEquals(
                caseFourLastTripOne.toString(),
                "Card 000001 entres Queen's Park station at 2018-07-03 8:00:00 EDT\n"
                        + ", then exits St George station at 2018-07-03 8:10:00 EDT\n"
                        + ", then entres St George station at 2018-07-03 8:12:00 EDT\n"
                        + ", then exits Kennedy station at 2018-07-03 10:01:00 EDT");
        assertEquals(caseFourBalanceThree, 3, 0.01);
        assertEquals(caseFourCostThree, 5.44, 0.01);
        assertEquals(caseFourRevenueThree, 6, 0.01);
    }

    /**
     * Case Four: Now we analyze the case that the preceding trip is a bus trip and the
     * following trip is a bus trip. In this case two trips don't share within one cap, and
     * we trace all possible parameters generated to make sure it works exactly as we expected.
     *
     */
    @Test
    public void testBusToBusEntreInformationCase1Step1() {
        assertEquals(trip9.getEntreTime(), "2018-07-04 12:01:00 EDT");
        assertEquals(trip9.getEntreStation(), "Dundas");
        assertEquals(
                trip9.getTripInformation().get(0), "entres Dundas stop at 2018-07-04 12:01:00 EDT");
        assertEquals(caseFiveCurrentTripOne.getEntreTime(), "2018-07-04 12:01:00 EDT");
        assertEquals(caseFiveCurrentTripOne.getEntreStation(), "Dundas");
        assertEquals(caseFiveBalanceOne, 1, 0.01);
        assertEquals(caseFiveCostOne, 0, 0.01);
        assertEquals(caseFiveRevenueOne, 2, 0.01);
    }


    // In this case the passenger stayed over two hours in the subway.
    /**
     *Tests exit information for the first bus trip
     *
     */

    @Test
    public void testBusToBusExitInformationCase1Step1() {
        assertEquals(trip9.getExitTime(), "2018-07-04 14:02:00 EDT");
        assertEquals(trip9.getExitStation(), "Spadina");
        assertEquals(caseFiveLastTripOne, trip9);
        assertEquals(
                caseFiveLastTripOne.getTripInformation().get(0),
                "entres Dundas stop at 2018-07-04 12:01:00 EDT");
        assertEquals(
                caseFiveLastTripOne.getTripInformation().get(1),
                "exits Spadina stop at 2018-07-04 14:02:00 EDT");
        assertEquals(
                caseFiveLastTripOne.toString(),
                "Card 000001 entres Dundas stop at 2018-07-04 12:01:00 EDT\n"
                        + ", then exits Spadina stop at 2018-07-04 14:02:00 EDT");
        assertEquals(caseFiveBalanceTwo, 1, 0.01);
        assertEquals(caseFiveCostTwo, 0.24, 0.01);
        assertEquals(caseFiveRevenueTwo, 2, 0.01);
    }

    // In this case connecting trip does NOT share the cap with the previous one.
    /**
     * Tests entrance information for the first bus trip
     *
     */
    @Test
    public void testBusToBusEntreInformationCase1Step2() {
        //        assertFalse(trip2.shareCap(trip1));
        assertEquals(trip10.getEntreTime(), "2018-07-04 14:06:00 EDT");
        assertEquals(trip10.getEntreStation(), "Spadina");
        assertEquals(caseFiveCurrentTripTwo.getEntreTime(), "2018-07-04 14:06:00 EDT");
        assertEquals(caseFiveCurrentTripTwo.getEntreStation(), "Spadina");
        assertEquals(caseFiveBalanceThree, 9, 0.01);
        assertEquals(caseFiveCostThree, 0.24, 0.01);
        assertEquals(caseFiveRevenueThree, 4, 0.01);
    }

    /**
     * Tests exit information for the first bus trip
     *
     */
    @Test
    public void testBusToBusExitInformationCase1Step2() {
        assertEquals(trip10.getExitTime(), "2018-07-04 14:20:00 EDT");
        assertEquals(trip10.getExitStation(), "Union");
        assertEquals(caseFiveLastTripTwo, trip10);
        assertEquals(
                caseFiveLastTripTwo.getTripInformation().get(0),
                "entres Spadina stop at 2018-07-04 14:06:00 EDT");
        assertEquals(
                caseFiveLastTripTwo.getTripInformation().get(1),
                "exits Union stop at 2018-07-04 14:20:00 EDT");
        assertEquals(
                caseFiveLastTripTwo.toString(),
                "Card 000001 entres Spadina stop at 2018-07-04 14:06:00 EDT\n"
                        + ", then exits Union stop at 2018-07-04 14:20:00 EDT");
        assertEquals(caseFiveBalanceFour, 9, 0.01);
        assertEquals(caseFiveCostFour, 1.44, 0.01);
        assertEquals(caseFiveRevenueFour, 4, 0.01);
    }

    /**
     * Tests entrance information for the second bus trip
     *
     */
    @Test
    public void testBusToBusEntreInformationCase2Step1() {
        assertEquals(
                trip11.getTripInformation().get(0), "entres High Park stop at 2018-07-05 12:00:00 EDT");
        assertEquals(caseSixBalanceOne, 7, 0.01);
        assertEquals(caseSixCostOne, 0, 0.01);
        assertEquals(caseSixRevenueOne, 2, 0.01);
    }

    /**
     *Tests exit information for the second bus trip
     *
     */
    // In this case the passenger stayed over two hours in the subway.
    @Test
    public void testBusToBusExitInformationCase2Step1() {
        assertEquals(caseSixLastTripOne, trip11);
        assertEquals(
                caseSixLastTripOne.getTripInformation().get(0),
                "entres High Park stop at 2018-07-05 12:00:00 EDT");
        assertEquals(
                caseSixLastTripOne.getTripInformation().get(1),
                "exits Bathurst stop at 2018-07-05 12:10:00 EDT");
        assertEquals(caseSixBalanceTwo, 7, 0.01);
        assertEquals(caseSixCostTwo, 0.72, 0.01);
        assertEquals(caseSixRevenueTwo, 2, 0.01);
    }

    /**
     * Tests entrance information for the second bus trip
     *
     */
    // In this case connecting trip does NOT share the cap with the previous one.
    @Test
    public void testBusToBusEntreInformationCase2Step2() {
        assertEquals(trip14.getEntreTime(), "2018-07-05 12:00:00 EDT");
        assertEquals(trip14.getEntreStation(), "High Park");
        assertEquals(caseSixCurrentTripOne.getEntreTime(), "2018-07-05 12:00:00 EDT");
        assertEquals(caseSixCurrentTripOne.getEntreStation(), "High Park");
        assertEquals(caseSixBalanceThree, 3, 0.01);
        assertEquals(caseSixCostThree, 1.8, 0.01);
        assertEquals(caseSixRevenueThree, 6, 0.01);
    }

    /**
     * Case Five: Now we analyze the case that the preceding trip is a bus trip and the
     * following trip is a subway trip. In this case two trips don't share within one cap, and
     * we trace all possible parameters generated to make sure it works exactly as we expected.
     *
     */
    // In this case connecting trip does NOT share the cap with the previous one.
    @Test
    public void testBusToSubwayEntreInformationCase1Step1() {
        assertEquals(dateThree, "2018-07-06");
        assertEquals(
                trip15.getTripInformation().get(0),
                "entres Square-Victoria-OACI stop at 2018-07-06 17:00:00 EDT");
        assertEquals(caseSevenBalanceOne, 1, 0.01);
        assertEquals(caseSevenCostOne, 0, 0.01);
        assertEquals(caseSevenRevenueOne, 2, 0.01);
    }

    /**
     * Tests exit information for the first bus trip
     *
     */
    // In this case the passenger stayed over two hours in the subway.
    @Test
    public void testBusToSubwayExitInformationCase1Step1() {
        assertEquals(
                trip15.getTripInformation().get(1), "exits Crémazie stop at 2018-07-06 17:30:00 EDT");
        assertEquals(caseSevenBalanceTwo, 1, 0.01);
        assertEquals(caseSevenCostTwo, 1.65, 0.01);
        assertEquals(caseSevenRevenueTwo, 2, 0.01);
    }

    /**
     * Tests entrance information for the first bus trip
     *
     */
    // In this case connecting trip does NOT share the cap with the previous one.
    @Test
    public void testBusToSubwayEntreInformationCase1Step2() {
        assertEquals(trip16.getEntreTime(), "2018-07-06 17:40:00 EDT");
        assertEquals(trip16.getEntreStation(), "Henri-Bourassa");
        assertEquals(caseSevenCurrentTripOne.getEntreTime(), "2018-07-06 17:40:00 EDT");
        assertEquals(caseSevenCurrentTripOne.getEntreStation(), "Henri-Bourassa");
        assertEquals(caseSevenBalanceThree, 1, 0.01);
        assertEquals(caseSevenCostThree, 1.65, 0.01);
        assertEquals(caseSevenRevenueThree, 2, 0.01);
    }

    /**
     * Tests exit information for the first bus trip
     *
     */
    @Test
    public void testBusToSubwayExitInformationCase1Step2() {
        assertEquals(trip16.getExitTime(), "2018-07-06 17:55:00 EDT");
        assertEquals(trip16.getExitStation(), "De La Conorde");
        assertEquals(caseSevenLastTripOne, trip16);
        assertEquals(
                caseSevenLastTripOne.toString(),
                "Card 000001 entres Henri-Bourassa station at 2018-07-06 17:40:00 EDT\n"
                        + ", then exits De La Conorde station at 2018-07-06 17:55:00 EDT");
        assertEquals(caseSevenBalanceFour, 0, 0.01);
        assertEquals(caseSevenCostFour, 2.25, 0.01);
        assertEquals(caseSevenRevenueFour, 3, 0.01);
    }

    /**
     * Tests entrance information for the second subway trip
     *
     */
    @Test
    public void testBusToSubwayEntreInformationCase2Step1() {
        assertEquals(
                trip17.getTripInformation().get(0), "entres Anjou stop at 2018-07-07 23:00:00 EDT");
        assertEquals(caseEightBalanceOne, 8, 0.01);
        assertEquals(caseEightCostOne, 0, 0.01);
        assertEquals(caseEightRevenueOne, 2, 0.01);
    }

    /**
     * Tests exit information for the second subway trip
     *
     */
    // In this case the passenger stayed over two hours in the subway.
    @Test
    public void testBusToSubwayExitInformationCase2Step1() {
        assertEquals(
                trip17.getTripInformation().get(1), "exits Snowdon stop at 2018-07-07 23:40:00 EDT");
        assertEquals(caseEightBalanceTwo, 8, 0.01);
        assertEquals(caseEightCostTwo, 2.4, 0.01);
        assertEquals(caseEightRevenueTwo, 2, 0.01);
    }

    /**
     * Tests entrance information for the second subway trip
     *
     */
    // In this case connecting trip does NOT share the cap with the previous one.
    @Test
    public void testBusToSubwayEntreInformationCase2Step2() {
        assertEquals(trip18.getEntreTime(), "2018-07-07 23:00:00 EDT");
        assertEquals(trip18.getEntreStation(), "Anjou");
        assertEquals(caseEightCurrentTripOne.getEntreTime(), "2018-07-07 23:00:00 EDT");
        assertEquals(caseEightCurrentTripOne.getEntreStation(), "Anjou");
        assertEquals(caseEightBalanceThree, 8, 0.01);
        assertEquals(caseEightCostThree, 2.4, 0.01);
        assertEquals(caseEightRevenueThree, 2, 0.01);
    }

    /**
     * Tests exit information for the second subway trip
     *
     */
    @Test
    public void testBusToSubwayExitInformationCase2Step2() {
        assertEquals(dateFour, "2018-07-08");
        assertEquals(trip18.getExitTime(), "2018-07-08 00:00:01 EDT");
        assertEquals(trip18.getExitStation(), "Montmorency");
        assertEquals(caseEightLastTripOne, trip18);
        assertEquals(
                caseEightLastTripOne.toString(),
                "Card 000001 entres Anjou stop at 2018-07-07 23:00:00 EDT\n"
                        + ", then exits Snowdon stop at 2018-07-07 23:40:00 EDT\n"
                        + ", then entres Snowdon station at 2018-07-07 23:45:00 EDT\n"
                        + ", then exits Montmorency station at 2018-07-08 00:00:01 EDT");
        assertEquals(caseEightBalanceFour, 4, 0.01);
        assertEquals(caseEightCostFour, 4.5, 0.01);
        assertEquals(caseEightRevenueFour, 4, 0.01);
    }

    /**
     * In this case, the time tracking monthly average cost and the time the card holder was
     * initialized are in the same month. Test whether the result of tracking monthly average
     * cost is the same as we expected.
     */
    @Test
    public void testTrackMonthlyAverageCostCaseOne() {
        assertEquals(monthlyAverageCostOne, 8.0, 0);
    }

    /**
     * In this case, the time tracking monthly average cost and the time the card holder was
     * initialized span over two months. Test whether the result of tracking monthly average
     * cost is the same as we expected.
     */
    @Test
    public void testTrackMonthlyAverageCostCaseTwo() {
        assertEquals(monthlyAverageCostTwo, 7.0, 0);
        assertEquals(monthlyAverageCostThree, 22.5, 0);
    }

    /**
     * In this case, tests viewRecentThreeTrips method for recentThreeTripsOne and recentThreeTripsFour.
     *
     */
    @Test
    public void testViewRecentThreeTripsCaseOne() {
        assertEquals(
                recentThreeTripsOne,
                "Trip 1 is: Card 000001 entres McGill station at 2018-06-30 11:00:00 EDT\n"
                        + ", then exits Berri-UQAM station at 2018-06-30 13:05:00 EDT\n"
                        + "Trip 2 is: Card 000001 entres Berri-UQAM stop at 2018-06-30 13:10:00 EDT\n"
                        + ", then exits Jolicoeur stop at 2018-06-30 13:25:00 EDT\n");
        assertEquals(
                recentThreeTripsFour,
                "Trip 1 is: Card 000001 entres Square-Victoria-OACI stop at 2018-07-06 17:00:00 EDT\n" +
                        ", then exits Crémazie stop at 2018-07-06 17:30:00 EDT\n" +
                        "Trip 2 is: Card 000001 entres Henri-Bourassa station at 2018-07-06 17:40:00 EDT\n" +
                        ", then exits De La Conorde station at 2018-07-06 17:55:00 EDT\n" +
                        "Trip 3 is: Card 000001 entres Anjou stop at 2018-07-07 23:00:00 EDT\n");
    }

    /**
     * In this case, tests viewRecentThreeTrips method for recentThreeTripsTwo and recentThreeTripsThree.
     *
     */
    @Test
    public void testViewRecentThreeTripsCaseTwo() {
        assertEquals(
                recentThreeTripsTwo,
                "Trip 1 is: Card 000001 entres McGill station at 2018-06-30 11:00:00 EDT\n"
                        + ", then exits Berri-UQAM station at 2018-06-30 13:05:00 EDT\n"
                        + "Trip 2 is: Card 000001 entres Berri-UQAM stop at 2018-06-30 13:10:00 EDT\n"
                        + ", then exits Jolicoeur stop at 2018-06-30 13:25:00 EDT\n"
                        + "Trip 3 is: Card 000001 entres Longueuil–Université-de-Sherbrooke station at 2018-07-01 13:00:00 EDT\n"
                        + ", then exits Université-de-Montréal station at 2018-07-01 13:40:00 EDT\n"
                        + ", then entres Université-de-Montréal stop at 2018-07-01 13:45:00 EDT\n"
                        + ", then exits Viau stop at 2018-07-01 13:55:00 EDT\n");
        assertEquals(
                recentThreeTripsThree,
                "Trip 1 is: Card 000001 entres Dundas stop at 2018-07-04 12:01:00 EDT\n"
                        + ", then exits Spadina stop at 2018-07-04 14:02:00 EDT\n"
                        + "Trip 2 is: Card 000001 entres Spadina stop at 2018-07-04 14:06:00 EDT\n"
                        + ", then exits Union stop at 2018-07-04 14:20:00 EDT\n"
                        + "Trip 3 is: Card 000001 entres High Park stop at 2018-07-05 12:00:00 EDT\n"
                        + ", then exits Bathurst stop at 2018-07-05 12:10:00 EDT\n"
                        + ", then entres Bathurst stop at 2018-07-05 12:11:59 EDT\n"
                        + ", then exits Union stop at 2018-07-05 12:59:11 EDT\n"
                        + ", then entres Union stop at 2018-07-05 13:00:00 EDT\n"
                        + ", then exits Dundas West stop at 2018-07-05 13:09:59 EDT\n"
                        + ", then entres Dundas West stop at 2018-07-05 13:11:00 EDT\n"
                        + ", then exits Danforth Ave stop at 2018-07-05 13:20:00 EDT\n");
    }

    @Test
    public void testGetHourDifferenceCase1() throws ParseException {
        String time1 = "2018-12-31 10:00:00 EDT";
        String time2 = "2018-12-31 11:59:00 EDT";
        assertEquals(GeneralTrip.getHourDifference(time1, time2), 1);
    }

    @Test
    public void testGetHourDifferenceCase2() throws ParseException {
        String time1 = "2018-12-30 10:00:00 EDT";
        String time2 = "2018-12-31 11:59:00 EDT";
        assertEquals(GeneralTrip.getHourDifference(time1, time2), 25);
    }

    @Test
    public void testTotalMonthCase1() throws ParseException {
        String time1 = "2018-06-30 10:00:00 EDT";
        String time2 = "2018-12-01 10:00:00 EDT";
        System.out.println(time1);
        System.out.println(time2);
        assertEquals(NormalCardHolder.getTotalMonth(time1, time2), 7);
    }

    @Test
    public void testTotalMonthCase2() throws ParseException {
        String time1 = "2018-06-30 10:00:00 EDT";
        String time2 = "2018-06-30 12:00:00 EDT";
        assertEquals(NormalCardHolder.getTotalMonth(time1, time2), 1);
    }

    @Test
    public void testWithinCapCase1() throws ParseException {
        GeneralTrip lastTrip = TripFactory.createNewTrip("Subway");
        lastTrip.setEntreTime("2018-12-31 10:00:00 EDT");
        lastTrip.setExitStation("St George Station");
        GeneralTrip currentTrip = TripFactory.createNewTrip("Subway");
        currentTrip.setEntreTime("2018-12-31 11:59:59 EDT");
        currentTrip.setEntreStation("St George Station");
        assertTrue(currentTrip.shareCap(lastTrip));
    }

    @Test
    public void testWithinCapCase2() throws ParseException {
        GeneralTrip lastTrip = TripFactory.createNewTrip("Subway");
        lastTrip.setEntreTime("2018-12-31 10:00:00 EDT");
        lastTrip.setExitStation("St George Station");
        GeneralTrip currentTrip = TripFactory.createNewTrip("Subway");
        currentTrip.setEntreTime("2018-12-31 12:59:59 EDT");
        currentTrip.setEntreStation("St George Station");
        assertFalse(currentTrip.shareCap(lastTrip));
    }

    @Test
    public void testWithinCapCase3() throws ParseException {
        GeneralTrip lastTrip = TripFactory.createNewTrip("Subway");
        lastTrip.setEntreTime("2018-12-31 10:00:00 EDT");
        lastTrip.setExitStation("St George Station");
        GeneralTrip currentTrip = TripFactory.createNewTrip("Subway");
        currentTrip.setEntreTime("2018-12-31 11:59:59 EDT");
        currentTrip.setEntreStation("St Patrick Station");
        assertFalse(currentTrip.shareCap(lastTrip));
    }


    @Test
    public void testInitializeDailyReport() {
        DailyReport dailyReport = new DailyReport("06/30/2018 10:00:00");
        assertEquals(dailyReport.date, "06/30/2018");
    }

    @Test
    public void testIncreaseDateByOneCase1() throws ParseException {
        AdminUser adminUser = new AdminUser("Toronto", "2018-06-29 10:00:00");
        assertEquals(adminUser.increaseDateByOne(), "2018-06-30");
    }

    @Test
    public void testIncreaseDateByOneCase2() throws ParseException {
        AdminUser adminUser = new AdminUser("Toronto", "2018-06-30 10:00:00");
        assertEquals(adminUser.increaseDateByOne(), "2018-07-01");
    }

    @Test
    public void testIncreaseDateByOneCase3() throws ParseException {
        AdminUser adminUser = new AdminUser("Toronto", "2018-12-31 10:00:00");
        assertEquals(adminUser.increaseDateByOne(), "2019-01-01");
    }

    @Test
    public void testputAllStationToNodes() {
        String[] line1 = new String[] {"A", "B", "C", "D"};
        String[] line2 = new String[] {"E", "B", "F", "G", "C", "H"};
        ArrayList<String> lineOneArray = new ArrayList<>(Arrays.asList(line1));
        ArrayList<String> lineTwoArray = new ArrayList<>(Arrays.asList(line2));
        SubwayGraph.setStationContainer(new ArrayList<>());

        SubwayGraph.getStationContainer().add(lineOneArray);
        SubwayGraph.getStationContainer().add(lineTwoArray);
        SubwayGraph.getNodes().clear();
        SubwayGraph.getPathCollection().clear();
        SubwayGraph.getSubwayMap().clear();
        SubwayGraph.putAllStationToNodes();

        String[] allNodes = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};

        ArrayList<String> allNodeArray = new ArrayList<>(Arrays.asList(allNodes));
        Collections.sort(allNodeArray);
        Collections.sort(SubwayGraph.getNodes());
        assertEquals(SubwayGraph.getNodes(), allNodeArray);
    }

    @Test
    public void testSetAdjacentInAllLine() {
        String[] line1 = new String[] {"A", "B", "C", "D"};
        String[] line2 = new String[] {"E", "B", "F", "G", "C", "H"};
        ArrayList<String> lineOneArray = new ArrayList<>(Arrays.asList(line1));
        ArrayList<String> lineTwoArray = new ArrayList<>(Arrays.asList(line2));
        SubwayGraph.setStationContainer(new ArrayList<>());

        SubwayGraph.getStationContainer().add(lineOneArray);
        SubwayGraph.getStationContainer().add(lineTwoArray);
        SubwayGraph.getNodes().clear();
        SubwayGraph.getPathCollection().clear();
        SubwayGraph.getSubwayMap().clear();
        SubwayGraph.putAllStationToNodes();

        String node = SubwayGraph.getNodes().get(1);
        SubwayGraph.setAdjacentInAllLine(node);
        ArrayList<String> actual = SubwayGraph.getSubwayMap().get(node);
        ArrayList<String> expect = new ArrayList<>(Arrays.asList("A", "C", "E", "F"));
        assertEquals(actual, expect);
    }

    @Test
    public void testSetAdjacentInOneLine() {
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        ArrayList<String> neighbour = new ArrayList<>();
        String node = "B";
        ArrayList<String> actual = SubwayGraph.setAdjacentInOneLine(arrayList, node, neighbour);
        ArrayList<String> expect = new ArrayList<>(Arrays.asList("A", "C"));
        assertEquals(actual, expect);
    }

    @Test
    public void testsetUpEdgeForEachNode() {
        String[] line1 = new String[] {"A", "B", "C", "D"};
        String[] line2 = new String[] {"E", "B", "F", "G", "C", "H"};
        ArrayList<String> lineOneArray = new ArrayList<>(Arrays.asList(line1));
        ArrayList<String> lineTwoArray = new ArrayList<>(Arrays.asList(line2));
        SubwayGraph.setStationContainer(new ArrayList<>());

        SubwayGraph.getStationContainer().add(lineOneArray);
        SubwayGraph.getStationContainer().add(lineTwoArray);
        SubwayGraph.getNodes().clear();
        SubwayGraph.getPathCollection().clear();
        SubwayGraph.getSubwayMap().clear();
        SubwayGraph.putAllStationToNodes();
        SubwayGraph.setUpEdgeForEachNode();

        Map<String, ArrayList<String>> map = new HashMap<>();
        String[] A = new String[] {"B"};
        ArrayList<String> neighbourOfA = new ArrayList<>(Arrays.asList(A));
        String[] B = new String[] {"A", "C", "E", "F"};
        ArrayList<String> neighbourOfB = new ArrayList<>(Arrays.asList(B));
        String[] C = new String[] {"B", "D", "G", "H"};
        ArrayList<String> neighbourOfC = new ArrayList<>(Arrays.asList(C));
        String[] D = new String[] {"C"};
        ArrayList<String> neighbourOfD = new ArrayList<>(Arrays.asList(D));
        String[] E = new String[] {"B"};
        ArrayList<String> neighbourOfE = new ArrayList<>(Arrays.asList(E));
        String[] F = new String[] {"B", "G"};
        ArrayList<String> neighbourOfF = new ArrayList<>(Arrays.asList(F));
        String[] G = new String[] {"F", "C"};
        ArrayList<String> neighbourOfG = new ArrayList<>(Arrays.asList(G));
        String[] H = new String[] {"C"};
        ArrayList<String> neighbourOfH = new ArrayList<>(Arrays.asList(H));
        map.put("A", neighbourOfA);
        map.put("B", neighbourOfB);
        map.put("C", neighbourOfC);
        map.put("D", neighbourOfD);
        map.put("E", neighbourOfE);
        map.put("F", neighbourOfF);
        map.put("G", neighbourOfG);
        map.put("H", neighbourOfH);

        assertEquals(SubwayGraph.getSubwayMap(), map);
    }
}
