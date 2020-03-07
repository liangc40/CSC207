import java.text.ParseException;

public class SubwayTrip extends GeneralTrip {

    /**
     * Add entre information to this trip with given string representation of time, station, card
     * number. When this method is called, if this trip
     * is sharing the cap with the previous trip, merge this trip with the previous one as one trip.
     * If this trip does not share the cap with the previous one, treat this trip as a distinct trip,
     * and update this trip as the card's current trip as well as the three most recent trips of this
     * this card's card holder. This method returns nothing.
     *
     * @param time a string showing the time when the card board on the subway
     * @param station a string showing the time where the card board on the subway
     * @param cardNumber a string showing the card number of the card board on the subway
     * @throws ParseException
     */
    public void addEntreInformation(String time, String station, String cardNumber)
            throws ParseException {
        // Set the given time, station, and card number as this trip's entre time, entre station,
        // and card number respectively.
        setEntreInformation(time, station, cardNumber);
        // With given card number, we could get this card from card map.
        Card card = Main.cardMap.get(cardNumber);
        // If the card is active, then check whether this trip shares the cap with the previous one,
        // merge this trip with the previous one; otherwise, treat this trip as a distinct trip.
        if (card.isActive()) {
            checkAndMergeLastTrip(card);
            // Update this trip as the current trip of the card, we also update this trips as the three
            // most recent trips of card holder.
            updateEntreInformation(card);
            //Add this trip into trip information.
            getTripInformation().add("entres " + station + " station at " + time);
            // Add this entre information to an array list representing trip information.
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
                                    + " station at "
                                    + time
                                    + ". Please try another card.");
        }
    }

    /**
     * Add exit information to this trip with the given string representation of time, station and
     * card number. When this method is called, we need to calculate the how many stops a passenger
     * has stayed on the subway in order to calculate the charge amount and administration cost for this service.
     * Then, we update the total cost of today's daily report by calculate the total profit(daily revenue - cost).
     * Then we set this trip as the card's last trip and update the three most recent three trips of the card holder.
     * This method returns nothing.
     *
     * @param time a string representation showing the time when the card exits the subway station/bus
     *     stop
     * @param station a string representation showing the time where the card eixts the subway
     *     station/bus stop
     * @param cardNumber a string representation showing the card number of the card exits the subway
     *     station/bus stop
     */
    public void addExitInformation(String time, String station, String cardNumber)
            throws ParseException {
        // Set the given time, station, and card number as this trip's exit time, exit station and card
        // number respectively.
        setExitInformation(time, station);
        // With given card number, we could get this card from card map.
        Card card = Main.cardMap.get(cardNumber);
        // Calculate how many stops the passenger has ridden.
        int stops = SubwayGraph.calculateDistance(getLastEntreStation(), getExitStation());
        // After exiting the subway fare can be calculated. set this subway fare as this trip's charge amount.
        setChargeAmount(calculateSubwayFare(stops));
        // Time service fare per stop with number of stops, we could get the admin cost for this ride.
        setAdminCost(stops * Main.costMap.get(getCity()).get("Subway"));
        // Then update the total revenue of today's daily revenue.
        updateDailyInfo(getCity(), "update total revenue");
        // We set this entering time as the time to automatically load money, once we trigger the
        // automatic loading money method, then such entering time is exactly the same time when money
        // is automatically loaded.
        setLoadMoneyTime(time);
        // Update this trip as the card's last trip and the cardholder's most recent three trips.
        updateExitInformation(card);
        // Add this exit information to an array list representing trip information.
        getTripInformation().add("exits " + station + " station at " + time);
        // Log this exit information as an info to this card holder's logger.
        card.getNormalCardHolder().userLogger.getLogger().info("Exited " + station + " stop at " + time);
    }

    /**
     * Return a double showing how much it costs for this subway trip. From revenueMap we could derive the fixed fare
     * for a bus ride in this city (eg. In our case, in Toronto and Montreal, a single bus ride is 2 dollars if we
     * do not consider cap). If fixed fare is smaller than the amount of unused cap, then we charge fixed fare for
     * this trip; otherwise, we charge the amount of unused cap for this trip (eg, if the cap is only 1 dollar on
     * for this trip, and fixed fee for a single bus ride is 2 dollars, admin users would charge 1 dollar for this trip).
     *
     * @param stops
     * @return
     * @throws ParseException
     */
    public double calculateSubwayFare(int stops) throws ParseException {
        double subwayFare;
        if (getHourDifference(getEntreTime(), getExitTime()) < 2) {
            double fixedFare = stops * Main.revenueMap.get(getCity()).get("Subway");
            if (getCap() < fixedFare) {
                subwayFare = getCap();
            } else {
                subwayFare = fixedFare;
            }
        } else {
            subwayFare = getCap();
        }
        return subwayFare;
    }
}
