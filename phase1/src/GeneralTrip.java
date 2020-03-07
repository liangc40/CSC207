import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.ArrayList;
import java.util.TimeZone;

/** A GeneralTrip where a BusTrip or a SubwayTrip could extend from. */
public class GeneralTrip extends Observable {
    /** The entre time of the trip in string representation. */
    private String entreTime;
    /** The exit time of the trip in string representation. */
    private String exitTime;
    /** The entre station of the trip in string representation. */
    private String entreStation;
    /** The exit station of the trip in string representation. */
    private String exitStation;
    /** The last or most recently entered station of this cardholder represented by string. */
    private String lastEntreStation;
    /** The name of the city where this trip is taken. */
    private String city;
    /** The card number of the card taking this trip. */
    private String cardNumber;
    /** The maximum amount could be charged to this card for a joined trip. */
    private double cap;
    /** The amount of dollars charged to this card for a single trip. */
    private double chargeAmount;
    /**
     * The amount of dollars transportation administrators have to pay to maintain the system operate
     * properly.
     */
    private double adminCost;
    /** The time when the card loads money automatically. */
    private String loadMoneyTime;
    /**
     * An arraylist stores every single entre and exit public transportation information of this trip.
     */
    private ArrayList<String> tripInformation = new ArrayList<>();
    /** An UserLogger to log message for this trip. */
    UserLogger userLogger = new UserLogger();

    /** Constructor of GeneralTrip which is empty. */
    GeneralTrip() {}

    /**
     * Get the entre time of this trip which is in form of a string.
     *
     * @return return the string representation of this trip's entre time
     */
    String getEntreTime() {
        return entreTime;
    }

    /**
     * Set a new string representation of time as this trip's entre time. This method returns nothing.
     *
     * @param entreTime the string showing the new entre time of this trip
     */
    void setEntreTime(String entreTime) {
        this.entreTime = entreTime;
    }

    /**
     * Get the entre station of this trip which is in form of a string.
     *
     * @return return the string representation of this trip's entre station
     */
    String getEntreStation() {
        return entreStation;
    }

    /**
     * Set a new string representation of station as this trip's entre station. this method returns
     * nothing.
     *
     * @param entreStation the string shwoing the new entre station of this trip
     */
    void setEntreStation(String entreStation) {
        this.entreStation = entreStation;
    }

    /**
     * Get the exit time of this trip which is in form of a string.
     *
     * @return return the string representation of this trip's exit station
     */
    String getExitTime() {
        return exitTime;
    }

    /**
     * Set a new string representation of time as this trip's exit time. This method returns nothing.
     *
     * @param exitTime the string showing the new exit time of this trip
     */
    private void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * Get the exit station of this trip which is in form of a string.
     *
     * @return return the string representation of this trip's exit station.
     */
    String getExitStation() {
        return exitStation;
    }

    /**
     * Set a new string representation of station as as trip's exit station. This method does not
     * return anything.
     *
     * @param exitStation the string showing the new exit station of this trip
     */
    void setExitStation(String exitStation) {
        this.exitStation = exitStation;
    }

    /**
     * Get the most recently entered station of this trip which is represented by a string.
     *
     * @return return the string representation of this trip's most recent entre station
     */
    String getLastEntreStation() {
        return lastEntreStation;
    }

    /**
     * Set a new string representation of station as this trip's most recently entered station. This
     * method does not return anything.
     *
     * @param station the string showing the most recently entered station of this trip
     */
    private void setLastEntreStation(String station) {
        this.lastEntreStation = station;
    }

    /**
     * Set a new string representation of card number as this trip's unique card number of card
     * corresponded to this trip. This method does not return anything.
     *
     * @param cardNumber a string showing the card number of corresponded to this trip
     */
    private void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Get the amount of money this trip would charge to the corresponding card in form double.
     *
     * @return return the double representation of the amount of money this trip would charge to a
     *     card.
     */
    double getChargeAmount() {
        return chargeAmount;
    }

    /**
     * Get the amount of money administrators have to pay for this trip, which is showed by a double.
     *
     * @return return the double representation of the amount of money this trip costs to
     *     transportation administrators.
     */
    double getAdminCost() {
        return adminCost;
    }

    /**
     * Set a double showing the amount of money administrators have to pay for this trip. This method
     * does not return anything.
     *
     * @param adminCost the double representation of the amount of money this trip costs to
     *     transportation administrators
     */
    void setAdminCost(double adminCost) {
        this.adminCost = adminCost;
    }

    /**
     * Set a new string representation of the city in which this trip is taken place. This method does
     * not return anything.
     *
     * @param city the string representation of the city where this trip is taken.
     */
    private void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the string representation of the city where this trip is taken place.
     *
     * @return a string showing where this trip is taken place.
     */
    String getCity() {
        return city;
    }

    /**
     * Get an array list containing all string representation of trip information of this trip.
     *
     * @return return an array list containing all trip information of this trip
     */
    ArrayList<String> getTripInformation() {
        return tripInformation;
    }

    /**
     * Set an array list of strings as this trip's trip information, where each string represents
     * every single step of trip information of this trip. This method returns nothing.
     *
     * @param tripInformation an array list of strings showing containing all trip information of a
     *     trip
     */
    private void setTripInformation(ArrayList<String> tripInformation) {
        this.tripInformation = tripInformation;
    }

    /**
     * Get an double showing the maximum amount of money that could be charged to the corresponding
     * card as a joined trip.
     *
     * @return return the double showing the cap of this trip
     */
    double getCap() {
        return cap;
    }

    /**
     * Set a double as the maximum amount of money that could be charged to the corresponding card as
     * a joined trip. This method returns nothing.
     *
     * @param cap a double showing the cap of this trip
     */
    private void setCap(double cap) {
        this.cap = cap;
    }

    /**
     * Set a double as the amount of money this trip would charge to the corresponding card. This
     * method returns nothing.
     *
     * @param chargeAmount a double showing the amount of money this trip charges to the card
     */
    void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * Get the string representation of the time when this card's balance gets automatically loaded.
     *
     * @return a string showing the when this card's balance gets automatically loaded
     */
    String getLoadMoneyTime() {
        return loadMoneyTime;
    }

    /**
     * Set a new time represented by string as the time when this card gets automatically loaded. This
     * method returns nothing.
     *
     * @param loadMoneyTime a string representing when this card's balance gets automatically loaded
     */
    void setLoadMoneyTime(String loadMoneyTime) {
        this.loadMoneyTime = loadMoneyTime;
    }

    /**
     * If two consecutive trips share a cap, which means the entre time of the following trip and
     * preceding trip are within two hours and the the exit staion/stop of the preceding trip and the
     * entre station/stop are exactly the same, we say these two trips share a cap, then we merge
     * these two trips as one trip, and set such merged trip as corresponding card's current trip. To
     * achieve this, we set preceding trip's entre station as merged trip's entre station, and set
     * preceding trip's entre time as merged trip's entre time, and set preceding trip's trip
     * information as merged trip's trip information, and set preceding trip's unused cap (if still
     * positive) as merged trip's cap. This method returns nothing.
     *
     * @param lastTrip a general trip showing the last trip of this trip's corresponding card
     */
    private void mergeTwoTrips(GeneralTrip lastTrip, Card card) {
        // Set the entre station of the following trip as this merged trip's last entered staion.
        setLastEntreStation(entreStation);
        // Set preceding trip's entre time as merged trip's entre time.
        setEntreTime(lastTrip.getEntreTime());
        // Set preceding trip's entre station as merged trip's entre station.
        setEntreStation(lastTrip.getEntreStation());
        // Set preceding trip's trip information as merged trip's entre information.
        setTripInformation(lastTrip.getTripInformation());
        // If preceding trip's unused cap amount is positive, set this cap amount as merged trip's cap
        // amount.
        // Otherwise, the cap of the merged trip's cap is zero.
        card.getNormalCardHolder().update(this,"delete the most recent trip");
        setCap(lastTrip.getCap());
    }

    /**
     * Return a boolean indicating whether two consecutive trips could share one cap and be considered
     * as one trip. To achieve this, we compare whether the entre time of the preceding trip and the
     * entre time of the following trip are within two hours and whether the exit station of the
     * preceding trip and the entre station of the following trip are are exactly the same. If both
     * conditions satisfy, we say these two trips share a cap. Once one of these two conditions does
     * not satisfy, we say these two trips does not share a cap and could not be considered as one
     * whole trip.
     *
     * @param lastTrip a general trip showing the last trip of this trip's corresponding card
     * @return a boolean showing whether this trip and the last trip finished share the same cap and
     *     be considered as one trip
     * @throws ParseException ParseException
     */
    boolean shareCap(GeneralTrip lastTrip) throws ParseException {
        // The first boolean indicates weather the entre time of the last trip and the entre time
        // of this trip are within two hours.
        boolean condition1 = (getHourDifference(lastTrip.getEntreTime(), this.getEntreTime()) < 2);
        // The second boolean indicates weather the exit station of the last trip and the entre station
        // of this trip are the same.
        boolean condition2 = (lastTrip.getExitStation().equals(this.entreStation));
        // If first boolean and second boolean are both true, we say these two trips share a cap.
        return (condition1 && condition2);
    }

    /**
     * Return a long type indicating the hour difference between two given time, and rounded down this
     * time difference to closest integer number.
     *
     * @param time1 the string representation of the first time
     * @param time2 the string representation of the second time
     * @return the double representing the hour difference between two given time
     * @throws ParseException ParseException
     */
    static long getHourDifference(String time1, String time2) throws ParseException {
        // Set new simple date format that input time should stick to.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        // Set the time zone of our format as "EDT".
        format.setTimeZone(TimeZone.getTimeZone("Eastern Daylight Time"));
        // Parse time1 and time2 to simple date format.
        Date d1 = format.parse(time1);
        Date d2 = format.parse(time2);
        // Return the time difference rounded down closest hour.
        return (d2.getTime() - d1.getTime()) / (1000 * 60 * 60);
    }

    /**
     * Get the last trip finished by the card corresponded to current trip and check whether these two
     * trips share a cap. If they do share a cap, then merge these two trip as one trip; otherwise,
     * treat these two trips as two distinct trips. This method returns nothing.
     *
     * @param card a card object represents the corresponding card of this trip
     * @throws ParseException ParseException
     */
    void checkAndMergeLastTrip(Card card) throws ParseException {
        // If the card has taken a trip before, then get the last trip of this card.
        if (card.getLastTrip() != null) {
            GeneralTrip lastTrip = card.getLastTrip();
            // If last trip and curren trip share a cap, then merge these two trips as one trip.
            if (this.shareCap(lastTrip)) mergeTwoTrips(lastTrip, card);
        }
    }

    /**
     * Override the toString method of Object, and return a string containing all trip information of
     * this trip, and every two pieces of trip information are separated by a comma and the word
     * "then".
     *
     * @return return a string containing all trip information of one single trip
     */
    @Override
    public String toString() {
        // Set a new string builder where we can append new contents at the end of it.
        StringBuilder result = new StringBuilder();
        // At the beginning of the string, we write "Card: " and the card number of this card.
        result.append("Card ").append(cardNumber).append(" ");
        // Loop through every single entre and exit information of this trip and append it to the end of
        // this string.
        if (!(getTripInformation().size() == 0)) {
            int i = 0;
            while (i < getTripInformation().size() - 1) {
                // Separate every single entre and exit information with a comma and word "then".
                result.append(getTripInformation().get(i)).append(System.lineSeparator()).append(", then ");
                i++;
            }
            result.append(getTripInformation().get(getTripInformation().size() - 1));
        }
        return result.toString();
    }

    /**
     * Add entre information to this trip with given string representation of time, station, and card
     * number. This method can only be applied to SubwayTrip. This method returns nothing.
     *
     * @param time a string showing the time when the card entered the subway system
     * @param station a string showing the station where the card entered the subway system
     * @param cardNumber a string showing the card number of the card entered the subway system
     * @throws ParseException ParseException
     */
    public void addEntreInformation(String time, String station, String cardNumber)
            throws ParseException {}

    /**
     * Add entre information to this trip with given string representation of time, station, card
     * number, and route name. This method can only be applied to BusTrip. This method returns
     * nothing.
     *
     * @param time a string showing the time when the card board on the bus
     * @param station a string showing the time where the card board on the bus
     * @param cardNumber a string showing the card number of the card board on the bus
     * @param routeNumber a string showing route number of bus this card board on
     * @throws ParseException ParseException
     */
    public void addEntreInformation(
            String time, String station, String cardNumber, String routeNumber) throws ParseException {}


    /**
     * Add exit information to this trip with given string representation of time, station, card
     * number, and route name. This method can be applied to both BusTrip and SubwayTrip. This method
     * returns nothing.
     *
     * @param time a string representation showing the time when the card exits the subway station/bus
     *     stop
     * @param station a string representation showing the time where the card eixts the subway
     *     station/bus stop
     * @param cardNumber a string representation showing the card number of the card exits the subway
     *     station/bus stop
     * @throws ParseException ParseException
     */
    public void addExitInformation(String time, String station, String cardNumber)
            throws ParseException {}

    /**
     * Update the total revenue and the total cost today's daily report. If the argument is "update
     * total revenue", then update the total today's revenue by adding this trip's charge amount to
     * the original revenue; similarly, if the argument is "update total cost", then update today's
     * total cost by adding this trip's admin cost to the original cost. This method returns nothing.
     *
     * @param city the string showing the city where this trip is taken place
     * @param updateContent the string showing which part of our daily report need to be updated
     */
    void updateDailyInfo(String city, String updateContent) {
        // Get the daily report of this city.
        DailyReport todayDailyReport = Main.adminUserMap.get(city).getTodayDailyReport();
        switch (updateContent) {
            // If the update content is revenue, then add this trip's charge amount to total revenue.
            case "update total revenue":
                todayDailyReport.update(this, "update total revenue");
                // If the update content is cost, then add this trip's admin cost to total cost.
            case "update total cost":
                todayDailyReport.update(this, "update total cost");
        }
    }

    /**
     * Update this trip as a card's current trip and car holder's three most recent trips after
     * reading the entre information of this trip. This method returns nothing.
     *
     * @param card the corresponding card of this trip
     */
    void updateEntreInformation(Card card) {
        // Update this trip as the current trip of the corresponding card.
        card.update(this, "update this trip as its current trip");
        // Update this trip as the most recent three trips of the cardholder of the corresponding card.
        card.getNormalCardHolder().update(this, "add trip to recent trips and update total fare");
        setCap(Math.max(cap - chargeAmount, 0));
        chargeAmount = 0;
        setChanged();
        notifyObservers();
    }

    /**
     * Set the string representation of time, station, and card number as this trip's entre time,
     * entre station and card number. This method returns nothing.
     *
     * @param time the string representing the entre time of this trip
     * @param station the string representing the entre station of this trip
     * @param cardNumber the card number of the corresponding card of this trip
     */
    void setEntreInformation(String time, String station, String cardNumber) {
        this.setEntreStation(station);
        this.setEntreTime(time);
        this.setLastEntreStation(station);
        this.setCardNumber(cardNumber);
        this.setCity(Main.stationCityMap.get(station));
        this.setCap(Main.capMap.get(city));
    }

    /**
     * Set the string representation of time, station and card number as this trip's exit time, exit
     * station and card number. This method returns nothing.
     *
     * @param time the string representation of this trip's exit time
     * @param station the string representation of this trip's exit station
     */
    void setExitInformation(String time, String station) {
        this.setExitTime(time);
        this.setExitStation(station);
    }

    /**
     * Update this trip as a card's last trip and car holder's three most recent trips after reading
     * the exit information of this trip. This method returns nothing.
     *
     * @param card the corresponding card of this trip
     */
    void updateExitInformation(Card card) {
        // Update this trip as the last trip of the corresponding card. Also, we update the card balance of this card
        // by distracting the amount of charge amount from card balance.
        card.update(this, "update the balance and last trip of this card");
        // Update this trip as the most recent three trips of the card holder of the corresponding card.
        card.getNormalCardHolder().update(this, "delete the most recent trip");
        card.getNormalCardHolder().update(this, "add trip to recent trips and update total fare");
        setCap(Math.max(cap - chargeAmount, 0));
        setChanged();
        notifyObservers();
    }
}