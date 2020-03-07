import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** A CardHolder who is a passenger of the transit system. */
public class NormalCardHolder implements Observer {

  /** The name of this cardholder. */
  private String name;

  /** The email address of this cardholder. */
  private String emailAddress;

  /** The total fare this card holder has paid since first trip. */
  private double totalFare;

  /**
   * The HashMap, in which the key is the card number and the value is the card, contains all cards
   * of this cardholder.
   */
  private HashMap<String, Card> userCardMap = new HashMap<>();

  /** The recent three trips. */
  private ArrayList<GeneralTrip> recentTrips = new ArrayList<>(3);

  /** The time when this cardholer created the account. */
  private String registrationDate;

  /** The logger of this card holder. */
  UserLogger userLogger;

  /**
   * Construct a new card holder.
   *
   * @param name name of this cardholder
   * @param emailAddress email address of this cardholder
   */
  NormalCardHolder(String name, String emailAddress, String registrationDate, UserLogger userLogger) {
    this.name = name;
    this.emailAddress = emailAddress;
    this.userLogger = userLogger;
    setRegistrationDate(registrationDate);
    Main.userMap.put(emailAddress, this);
  }

  /**
   * Add the Card newCard to this cardholder's list of cards. Log this activity with its time.
   *
   * @param newCard the card to be added
   * @param time the time when the activity happens
   */
  void addCard(Card newCard, String time) {
    userCardMap.put(newCard.getCardNumber(), newCard);
    newCard.setNormalCardHolder(this);
    userLogger.getLogger().info("Added card " + newCard.getCardNumber() + " at " + time);
  }

  /**
   * Remove the Card represented by cardNumber from this cardholder's list of cards. Log this
   * activity with its time.
   *
   * @param cardNumber id number of the card to be removed
   * @param time time of this activity
   */
  void removeCard(String cardNumber, String time) {
    userCardMap.remove(cardNumber);
    userLogger.getLogger().finer("Removed card " + cardNumber + " at " + time);
  }

  public ArrayList<GeneralTrip> getRecentTrips() {
    return recentTrips;
  }

  /**
   * Return the name of this cardholder.
   *
   * @return the name of this cardholder
   */
  String getName() {
    return name;
  }

  /**
   * Return the email address of this cardholder.
   *
   * @return the email address of this cardholder
   */
  String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Set the registration date of this cardholder to registrationDate.
   *
   * @param registrationDate the date to be set as registration date
   */
  void setRegistrationDate(String registrationDate) {
    this.registrationDate = registrationDate;
    this.userLogger
        .getLogger()
        .finest("Card holder: " + name + " is registered at " + registrationDate);
  }

  /**
   * Set the name of this cardholder to newName.
   *
   * @param newName the name to be set
   */
  void setName(String newName) {
    this.name = newName;
    this.userLogger.getLogger().info("Card holder: " + name + " changed name to " + newName);
  }

  /**
   * Suspend the Card represented by oldCardNumber and replace it with the Card represented by
   * newCardNumber if the old card is currently being used. Add the new card to this cardholder and
   * log a warning massage. If the old card is not in use, print error message and log a warning
   * message.
   *
   * @param oldCardNumber the card number of the card to be suspended
   * @param newCardNumber the card number of the card which is to replace the suspended card
   */
  void replaceStolenCard(String oldCardNumber, String newCardNumber) {
    // the old card is in this cardholder's list of cards
    if (userCardMap.containsKey(oldCardNumber)) {
      userCardMap.get(oldCardNumber).setActive(false);
      // the new card has never been added
      if (!Main.cardMap.containsKey((newCardNumber))) {
        Card newCard = new Card(newCardNumber);
        // transfer the balance to the new card
        newCard.setBalance(userCardMap.get(oldCardNumber).getBalance());
        userCardMap.put(newCardNumber, newCard);
        this.userLogger
            .getLogger()
            .warning(
                "Cardholder suspended card "
                    + oldCardNumber
                    + " and replaced it with card "
                    + newCardNumber);
      } else { // the new card has been added by someone other than this cardholder
        System.out.println(
            "Invalid operation: Card "
                + newCardNumber
                + " has already been in use. Please choose another card number.");
        this.userLogger
            .getLogger()
            .warning("Cardholder can't replace suspended card with a card " + "that is in use.");
      }
    } else { // the old card is not in this cardholder's list of cards
      System.out.println(
          "Invalid operation: User "
              + emailAddress
              + " does not have to the right to suspend Card "
              + oldCardNumber);
      this.userLogger
          .getLogger()
          .warning("Cardholder didn't have right to suspend card " + oldCardNumber);
    }
  }

  /**
   * Set the automatic load amount of the card represented by cardNumber to automaticLoadAmount.
   *
   * @param automaticLoadAmount the amount to be set
   * @param cardNumber the card number of the card to be modified
   */
  void setAutomaticLoadAmount(String automaticLoadAmount, String cardNumber) {
    userCardMap.get(cardNumber).setAutomaticLoadAmount(automaticLoadAmount);
  }

  /**
   * Return the total months in the duration from time1 to time2.
   *
   * @param time1 the starting time
   * @param time2 the ending time
   * @return the total months in between
   * @throws ParseException the exception to be thrown
   */
  static int getTotalMonth(String time1, String time2) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    format.setTimeZone(TimeZone.getTimeZone("EDT"));
    Date d1 = format.parse(time1);
    Date d2 = format.parse(time2);
    return (d2.getYear() - d1.getYear()) * 12 + (d2.getMonth() - d1.getMonth()) + 1;
  }

  /**
   * Return the monthly average cost of this cardholder since first trip.
   *
   * @param currentTime the time of this activity
   * @return the monthly average cost
   * @throws ParseException the exception to be thrown
   */
  double trackMonthlyAverageCost(String currentTime) throws ParseException {
    double monthlyAverageCost = totalFare / getTotalMonth(registrationDate, currentTime);
    userLogger
        .getLogger()
        .info("Tracked average monthly cost at " + currentTime + ": " + monthlyAverageCost);
    return monthlyAverageCost;
  }

  /**
   * Return the string representation of recent three trips.
   *
   * @param time the time of this activity
   * @return the string representation of recent three trips
   */
  String viewRecentThreeTrips(String time) {
    String result;
    if (recentTrips.size() < 3) {
      result = buildRecentTripString(recentTrips.size());
      userLogger.getLogger().info("The number of recent trips is less than three.");
    } else {
      result = buildRecentTripString(recentTrips.size());
      userLogger
              .getLogger()
              .info("Viewed recent three trips at " + time + ":" + System.lineSeparator() + result);
    }
    return result;
  }

  String buildRecentTripString(int size) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < recentTrips.size(); i++) {
      result
              .append("Trip ")
              .append(i + 1)
              .append(" is: ")
              .append(recentTrips.get(i).toString())
              .append(System.lineSeparator());
    }
    return result.toString();
  }

  /**
   * Update the cardholder's recent trips. Remove the earliest trip if the number of recent trips
   * exceeds three. Accumulate the total fare of this cardholder.
   *
   * @param observable the observed trip that has changed
   * @param arg the string that shows information of this update
   */
  public void update(Observable observable, Object arg) {
    if (arg.equals("add trip to recent trips and update total fare")) {
      // control the ArrayList recentTrips to have only three recent trips
      if (recentTrips.size() >= 3) {
        recentTrips.remove(0);
      }
      GeneralTrip trip = ((GeneralTrip) observable);
      recentTrips.add(trip);
      // accumulate this cardholder's total fares generated since registration
      totalFare += (((GeneralTrip) observable).getChargeAmount());
      observable.deleteObserver(this);
    } else if (arg.equals("delete the most recent trip")) {
      recentTrips.remove(recentTrips.size()-1);
    }
  }
}
