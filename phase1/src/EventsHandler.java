import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;

/** The class of EventsHandler implementing Singleton class, and could read and handle all events
 * events.txt.
 */
public class EventsHandler {

  /** static EventsHandler eventsHandler_instance of type Singleton */
  private static EventsHandler eventsHandler_instance = null;

  /** private constructor restricted to this class itself */
  private EventsHandler() {}

  /**
   * Create instance of Singleton EventsHandler.
   *
   * @return an instance of EventsHandler
   */
  static EventsHandler getEventsHandler_instance() {
    // To ensure only one instance is created
    if (eventsHandler_instance == null) {
      eventsHandler_instance = new EventsHandler();
    }
    return eventsHandler_instance;
  }

  /**
   * Read each line in events.txt, which is considered as the input information of our program, into
   * a String and handle different events according to the different actions specified in each line.
   * After handling the input information, it may generate output messages in the form of log
   * messages to files/consoles or printing messages to the screen.
   *
   * @throws ParseException the Exception to throw when the input date is not in the correct format
   */
   void readEvents() throws ParseException {
    // The Path instance representing the directory of file events.txt
    Path eventsPath = Paths.get("events.txt");
    // use BufferReader to read text from events.txt
    try (BufferedReader fileInput = Files.newBufferedReader(eventsPath)) {
      String line;
      // String line = fileInput.readLine();
      while ((line = fileInput.readLine()) != null) {
        // split each line by \ into String from events.txt
        String[] parts = line.split("\\|");
        // The String instance representing user's action
        String action = parts[1];
        switch (action) {
          case "creates admin user":
            AdminUser adminUser = new AdminUser(parts[0], parts[2]);
            Main.adminUserMap.put(parts[0], adminUser);
            break;
          case "generates daily report":
            Main.adminUserMap.get(parts[0]).generateDailyReport();
            break;
          case "sets cost":
            HashMap<String, Double> costMap = new HashMap<>();
            costMap.put(parts[2], Double.parseDouble(parts[3]));
            costMap.put(parts[4], Double.parseDouble(parts[5]));
            Main.costMap.put(parts[0], costMap);
            break;
          case "sets revenue":
            HashMap<String, Double> revenueMap = new HashMap<>();
            revenueMap.put(parts[2], Double.parseDouble(parts[3]));
            revenueMap.put(parts[4], Double.parseDouble(parts[5]));
            Main.revenueMap.put(parts[0], revenueMap);
            break;
          case "sets cap":
            Main.capMap.put(parts[0], Double.parseDouble(parts[2]));
            break;
          case "creates account":
            UserLogger logger = new UserLogger();
            logger.setLogId(parts[2]);
            NormalCardHolder user = new NormalCardHolder(parts[0], parts[2], parts[3], logger);
            user.setRegistrationDate(parts[3]);
            break;
          case "adds card":
            Card card = new Card(parts[2]);
            Main.userMap.get(parts[0]).addCard(card, parts[3]);
            break;
          case "removes card":
            Main.userMap.get(parts[0]).removeCard(parts[2], parts[3]);
            break;
          case "entres":
            // create an new trip
            GeneralTrip trip = TripFactory.createNewTrip(parts[3]);
            trip.userLogger.setLogId(Main.stationCityMap.get(parts[2]));
            switch (parts[3]) {
              case "Bus":
                trip.addEntreInformation(parts[5], parts[2], parts[0], parts[4]);
                // extra information at the end of this line of events.txt, meaning there is illegal
                // entry/exit or power outage
                getEventsHandler_instance().eventProcessCase3(parts, trip);
                break;
              case "Subway":
                trip.addEntreInformation(parts[4], parts[2], parts[0]);
                // extra information at the end of this line of events.txt, meaning there is illegal
                // entry/exit or power outage
                getEventsHandler_instance().eventProcessCase4(parts, trip);
                break;
            }
            break;
          case "exits":
            GeneralTrip currentTrip = Main.cardMap.get(parts[0]).getCurrentTrip();
            // special case 1: tap in with one card and tap out with another card which has no
            // previous trips
            // special case 2: tap out with a different card which has previous trips
            getEventsHandler_instance().eventProcessCase5(currentTrip, parts);
            break;
          case "loads money":
            Main.cardMap.get(parts[2]).loadMoney(parts[3], parts[4]);
            break;
          case "changes name":
            Main.userMap.get(parts[0]).setName(parts[2]);
            break;
          case "suspends stolen cards":
            Main.userMap.get(parts[0]).replaceStolenCard(parts[2], parts[3]);
            break;
          case "views recent three trips":
            // parts[2] is the time that cardholder to view recent three trips
            Main.userMap.get(parts[0]).viewRecentThreeTrips(parts[2]);
            break;
          case "tracks average monthly cost":
            // parts[2] is the time that cardholder to track monthly cost
            Main.userMap.get(parts[0]).trackMonthlyAverageCost(parts[2]);
            break;
          case "check shortest distance":
            // parts[2], parts[3], parts[4] is transit mode, start, destination respectively
            switch (parts[2]) {
              // check shortest distance related to Subway
              case "Subway":
                getEventsHandler_instance().eventProcessCase2(parts[3], parts[4]);
                break;
              // check shortest distance related to Bus
              case "Bus":
                getEventsHandler_instance().eventProcessCase1(parts[3], parts[4]);
                break;
            }
            break;
        }
      }
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Process event related to check shortest distance for a bus trip with start stop and ending
   * stop. It is possible that more than one bus routes can contains both of the starting and ending
   * stop Thus, this method will print out the shortest distance in console and the corresponding
   * bus route name.
   *
   * @param start the corresponding subway starting station an user wants to check shortest distance
   * @param destination the corresponding subway ending station an user wants to check shortest
   *     distance
   */
  private void eventProcessCase1(String start, String destination) {
    // set an initialized shortest distance as 0 to compare below
    int shortestDistance = 0;
    // set an initialized bus route name corresponded to shortest distance as empty String to
    // compare below
    String routeNameRelated = "";
    // loop over the HashMap storing bus routes to get each bus route
    for (String busRouteID : Main.busRoutesMap.keySet()) {
      BusRoute busRoute = Main.busRoutesMap.get(busRouteID);
      // to check if a bus route contains both start stop and end stop
      if (busRoute.getStopOrders().keySet().contains(start)
              && busRoute.getStopOrders().keySet().contains(destination)) {
        // calculate distance between start stop and end stop given a bus route
        int distance = busRoute.calculateNumberOfStops(start, destination);
        // get bus route name for this bus route
        String routeName = busRoute.getBusRouteName();
        // compare if distance between start stop and end stop given a bus route is shortest and
        // update
        if (shortestDistance == 0) {
          shortestDistance = distance;
          routeNameRelated = routeName;
        } else {
          if (distance < shortestDistance) {
            shortestDistance = distance;
            routeNameRelated = routeName;
          }
        }
      }
    }

    // print out the shortest distance in console and the corresponding bus route name.
    System.out.println(
            "The shortest distance between "
                    + start
                    + " and "
                    + destination
                    + " is "
                    + shortestDistance
                    + " on "
                    + routeNameRelated);
  }

  /**
   * Process event related to check shortest distance for a subway trip with start station and
   * ending station. This method will print out the shortest distance in console.
   *
   * @param start the corresponding subway starting station an user wants to check shortest distance
   * @param destination the corresponding subway ending station an user wants to check shortest
   *     distance
   */
  private void eventProcessCase2(String start, String destination) {
    System.out.println(
            "The shortest distance between "
                    + start
                    + " and "
                    + destination
                    + " is "
                    + SubwayGraph.calculateDistance(start, destination));
  }

  /**
   * Process event related to enter into a bus trip with special cases related illegal entering or
   * power outage This method will log message which card, when, and where is associated with
   * special cases
   *
   * @param lines the corresponding Strings associated with the line information in events.txt
   * @param trip the corresponding bus trip associated with entrance with special cases
   */
  private void eventProcessCase3(String[] lines, GeneralTrip trip) {
    if (lines.length != 6) {
      trip.userLogger
              .getLogger()
              .warning(
                      "Card "
                              + lines[0]
                              + " missed enter information due to "
                              + lines[6]
                              + " at stop "
                              + lines[2]
                              + " at "
                              + lines[5]);
    }
  }

  /**
   * Process event related to enter into a subway trip with special cases related illegal entering
   * or power outage This method will log message which card, when, and where is associated with
   * special cases
   *
   * @param lines the corresponding Strings associated with the line information in events.txt
   * @param trip the corresponding subway trip associated with entrance with special cases
   */
  private void eventProcessCase4(String[] lines, GeneralTrip trip) {
    if (lines.length != 5) {
      trip.userLogger
              .getLogger()
              .warning(
                      "Card "
                              + lines[0]
                              + " missed enter information due to "
                              + lines[5]
                              + " at station "
                              + lines[2]
                              + " at "
                              + lines[4]);
    }
  }

  /**
   * Process event related to exiting out a trip with a different card or special cases such as
   * illegal ans power outage. This method will simultaneously log message for cardholder and admin
   * user to identify cardholder's information related to their cases with location, time, and
   * warnings.
   *
   * @param lines the corresponding Strings associated with the line information in events.txt
   * @param currentTrip the corresponding subway trip associated with entrance with special cases
   */
  private void eventProcessCase5(GeneralTrip currentTrip, String[] lines) throws ParseException {
    if (currentTrip == null || currentTrip.getExitStation() != null) {
      NormalCardHolder normalCardHolder = Main.cardMap.get(lines[0]).getNormalCardHolder();
      // log to the CardHolder's log
      normalCardHolder
              .userLogger
              .getLogger()
              .severe(
                      normalCardHolder.getName()
                              + " used a different card "
                              + lines[0]
                              + " to tap out at "
                              + lines[2]
                              + " at "
                              + lines[4]);
      // the output message would be seen by the cardholder when he/she taps out
      System.out.println(
              normalCardHolder.getName()
                      + " should "
                      + "use the same card to tap out and pay at "
                      + lines[2]);
      // log to AdminUser's log
      UserLogger userLogger = new UserLogger();
      userLogger.setLogId(Main.stationCityMap.get(lines[2]));
      userLogger
              .getLogger()
              .severe(
                      normalCardHolder.getName()
                              + " used a different card "
                              + lines[0]
                              + " to tap out at "
                              + lines[2]
                              + " at "
                              + lines[4]);
    } else { // tap in and tap out with the same card
      currentTrip.addExitInformation(lines[4], lines[2], lines[0]);
      // extra information at the end of this line of events.txt, meaning there is illegal
      // entry/exit or power outage
      currentTrip.userLogger.setLogId(Main.stationCityMap.get(lines[2]));
      if (lines.length != 5) {
        currentTrip
                .userLogger
                .getLogger()
                .warning(
                        "Card "
                                + lines[0]
                                + " missed exit information due to "
                                + lines[5]
                                + " at stop "
                                + lines[2]
                                + " at "
                                + lines[4]);
      }
    }
  }
}
