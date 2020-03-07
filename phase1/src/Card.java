import java.util.Observable;
import java.util.Observer;

/** A Card that represents a transit card. */
public class Card implements Observer {

  /** This card's unique id number. */
  private String cardNumber;

  /** The balance of this card. */
  private float balance = 19;

  /** The cardholder of this card. */
  private NormalCardHolder normalCardHolder;

  /** This card's last trip that has finished. */
  private GeneralTrip lastTrip;

  /** This card's current trip that hasn't finished. */
  private GeneralTrip currentTrip;

  /** True if the card is active, false if it's suspended. */
  private boolean active = true;

  /** The amount to be automatically loaded when balance is below zero. */
  private String automaticLoadAmount;

  /**
   * Construct a new card.
   *
   * @param cardNumber the new card's card number
   */
  Card(String cardNumber) {
    this.cardNumber = cardNumber;
    Main.cardMap.put(cardNumber, this);
  }

  /**
   * Return the current trip of this card.
   *
   * @return current trip of the card
   */
  GeneralTrip getCurrentTrip() {
    return currentTrip;
  }

  /**
   * Set this card's current trip to currentTrip.
   *
   * @param currentTrip the trip to be set as current trip
   */
  private void setCurrentTrip(GeneralTrip currentTrip) {
    this.currentTrip = currentTrip;
  }

  /**
   * Return true if and only if the card is active(not suspended), false otherwise.
   *
   * @return whether the card is active or suspended
   */
  boolean isActive() {
    return active;
  }

  /**
   * Return the balance of this card.
   *
   * @return balance of this card
   */
  float getBalance() {
    return balance;
  }

  /**
   * Set the balance of this card to balance.
   *
   * @param balance the amount to be set as balance
   */
  void setBalance(float balance) {
    this.balance = balance;
  }

  /**
   * Set the card to active if the argument active is true, to inactive(suspended) if active is
   * false.
   *
   * @param active the status to be set to this card
   */
  void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Return the id number of this card.
   *
   * @return the card number of this card
   */
  String getCardNumber() {
    return cardNumber;
  }

  /**
   * Set the card holder of this card to Cardholder cardHolder.
   *
   * @param normalCardHolder the card holder to be set to this card
   */
  void setNormalCardHolder(NormalCardHolder normalCardHolder) {
    this.normalCardHolder = normalCardHolder;
  }

  /**
   * Return the last trip of this card.
   *
   * @return the last trip of this card
   */
  GeneralTrip getLastTrip() {
    return lastTrip;
  }

  /**
   * Set the last trip of this card to GeneralTrip lastTrip.
   *
   * @param lastTrip the trip to be set as last trip of this card
   */
  private void setLastTrip(GeneralTrip lastTrip) {
    this.lastTrip = lastTrip;
  }

  /**
   * Return the card holder of this card.
   *
   * @return the card holder of this card
   */
  NormalCardHolder getNormalCardHolder() {
    return normalCardHolder;
  }

  /**
   * Set the amount to be automatically loaded when balance is below zero to automaticLoadAmount.
   *
   * @param automaticLoadAmount the amount to be set as automaticLoadAmount of this card
   */
  void setAutomaticLoadAmount(String automaticLoadAmount) {
    this.automaticLoadAmount = automaticLoadAmount;
  }

  /**
   * Increase the balance of this card by the integer value of loadAmount and log the activity. If
   * the card is suspended, print the error message and log a message at severe level.
   *
   * @param loadAmount the amount to be loaded to this card
   * @param time the time when this activity happens
   */
  void loadMoney(String loadAmount, String time) {
    // if this card is not suspended
    if (active) {
      balance += Integer.parseInt(loadAmount);
      getNormalCardHolder()
              .userLogger
              .getLogger()
              .fine("Loaded " + loadAmount + " dollars into my account at " + time);
    } else { // if this card is suspended
      getNormalCardHolder()
              .userLogger
              .getLogger()
              .severe(
                      "Someone tried to load "
                              + loadAmount
                              + " dollars into suspended card "
                              + cardNumber
                              + " at "
                              + time);
    }
  }

  /**
   * Update the current trip or last trip of this card based on the information prompt arg. Update
   * the balance of this card by the calculated charged amount. Load automaticLoadAmount to this
   * card if the balance is below zero.
   *
   * @param observable the observed trip that has changed
   * @param arg a string giving information on how to update this card
   */
  public void update(Observable observable, Object arg) {
    GeneralTrip trip = ((GeneralTrip) observable);
    // update corresponding card information when entering
    if (arg.equals("update this trip as its current trip")) {
      setCurrentTrip(trip);
      // bus trips charge at entry
      if (trip instanceof BusTrip) {
        balance -= trip.getChargeAmount();
      }
      // update corresponding card information when exiting
    } else if (arg.equals("update the balance and last trip of this card")) {
      setLastTrip((GeneralTrip) observable);
      // subway trips charge at exit
      if (trip instanceof SubwayTrip) {
        balance -= trip.getChargeAmount();
      }
    }
    // balance below zero would trigger the system to automatically load money
    if (balance < 0) {
      loadMoney(automaticLoadAmount, ((GeneralTrip) observable).getLoadMoneyTime());
    }
    observable.deleteObserver(this);
  }
}

