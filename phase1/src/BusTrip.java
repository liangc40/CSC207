import java.text.ParseException;

/** A BusTrip extending from GeneralTrip. */
public class BusTrip extends GeneralTrip {
    /** The route name of the bus trip. */
    private String routeName;

    /**
     * Add entre information to this trip with given string representation of time, station, card
     * number, and route name. When this method is called, we need to update the card balance of the
     * card entering the bus stop, as well as the daily revenue of today's daily report. If this trip
     * is sharing the cap with the previous trip, merge this trip with the previous one as one trip.
     * If this trip does not share the cap with the previous one, treat this trip as a distinct trip,
     * and update this trip as the card's current trip as well as the three most recent trips of this
     * this card's card holder. This method returns nothing.
     *
     * @param time a string showing the time when the card board on the bus
     * @param station a string showing the time where the card board on the bus
     * @param cardNumber a string showing the card number of the card board on the bus
     * @param routeName a string showing the route number of the bus trip
     * @throws ParseException a ParseException
     */
    public void addEntreInformation(String time, String station, String cardNumber, String routeName)
            throws ParseException {
        this.routeName = routeName;
        // Set the given time, station, and card number as this trip's entre time, entre station,
        // and card number respectively.
        setEntreInformation(time, station, cardNumber);
        // With given card number, we could get this card from card map.
        Card card = Main.cardMap.get(cardNumber);
        // If the card is active, then check whether this trip shares the cap with the previous one,
        // merge this trip with the previous one; otherwise, treat this trip as a distinct trip.
        if (card.isActive()) {
            checkAndMergeLastTrip(card);
            // After calculating the bus fare, set this bus fare as this trip's charge amount.
            setChargeAmount(calculateBusFare());
            // Then update the total revenue of today's daily revenue.
            updateDailyInfo(getCity(), "update total revenue");
            // We set this entering time as the time to automatically load money, once we trigger the
            // automatic loading money method, then such entering time is exactly the same time when money
            // is automatically loaded.
            setLoadMoneyTime(time);
            // Update this trip as the current trip of the card, we also update this trips as the three
            // most recent trips of card holder.
            updateEntreInformation(card);
            // Add this entre information to an array list representing trip information.
            getTripInformation().add("entres " + station + " stop at " + time);
            // Log this entre information as an info to this card holder's logger.
            card.getNormalCardHolder().userLogger.getLogger().info("Entered " + station + " stop at " + time);
            // If this card is not active, then we log a message saying that the is suspended and try
            // another card to the card holder.
        } else {
            card.getNormalCardHolder()
                    .userLogger
                    .getLogger()
                    .severe(
                            "Suspended card "
                                    + cardNumber
                                    + " entered "
                                    + station
                                    + " stop at "
                                    + time
                                    + "Please try another card");
        }
    }

    /**
     * Add exit information to this trip with the given string representation of time, station and
     * card number. When this method is called, we need to calculate the how many stops a passenger
     * has stayed on the bus in order to calculate the amount of administration cost for this service.
     * After getting admin cost, we update the total cost of today's daily report by adding admin cost
     * to the original total cost. Then we set this trip as the card's last trip and update the three
     * most recent three trips of the card holder. This method returns nothing.
     *
     * @param time a string representation showing the time when the card exits the subway station/bus
     *     stop
     * @param station a string representation showing the time where the card eixts the subway
     *     station/bus stop
     * @param cardNumber a string representation showing the card number of the card exits the subway
     *     station/bus stop
     */
    public void addExitInformation(String time, String station, String cardNumber) {
        // Set the given time, station, and card number as this trip's exit time, exit station and card
        // number respectively.
        setExitInformation(time, station);
        // With given card number, we could get this card from card map.
        Card card = Main.cardMap.get(cardNumber);
        // Calculate how many stops the passenger has ridden.
        int stops =
                Main.busRoutesMap
                        .get(routeName)
                        .calculateNumberOfStops(getLastEntreStation(), getExitStation());
        // Time service fare per stop with number of stops, we could get the admin cost for this ride.
        setAdminCost(stops * Main.costMap.get(getCity()).get("Bus"));
        // Update our newly calculated admin cost to the total cost of today's daily report.
        updateDailyInfo(getCity(), "update total cost");
        // Update this trip as the card's last trip and the cardholder's most recent three trips.
        updateExitInformation(card);
        // Add this exit information to an array list representing trip information.
        getTripInformation().add("exits " + station + " stop at " + time);
        // Log this exit information as an info to this card holder's logger.
        card.getNormalCardHolder().userLogger.getLogger().info("Exited " + station + " stop at " + time);
    }

    /**
     * Return a double showing how much it costs for this bus trip. From revenueMap we could derive the fixed fare
     * for a bus ride in this city (eg. In our case, in Toronto and Montreal, a single bus ride is 2 dollars if we
     * do not consider cap). If fixed fare is smaller than the amount of unused cap, then we charge fixed fare for
     * this trip; otherwise, we charge the amount of unused cap for this trip (eg, if the cap is only 1 dollar on
     * for this trip, and fixed fee for a single bus ride is 2 dollars, admin users would charge 1 dollar for this trip).
     *
     * @return a double showing how much it costs for this bus trip.
     */
    private double calculateBusFare() {
        double busFare;
        // Get the amount of fixed fare from revenue map stored in Main class.
        double fixedFare = Main.revenueMap.get(getCity()).get("Bus");
        // If the cap amount is smaller than fixed fare, then charge the card the cap amount.
        if (getCap() < fixedFare) {
            busFare = getCap();
            // If the fixed fare is smaller then cap amount, then charge the card fixed fare.
        } else {
            busFare = fixedFare;
        }
        return busFare;
    }
}