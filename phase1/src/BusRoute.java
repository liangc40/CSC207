import java.util.HashMap;

/** A BusRoute which has its own name, and stores stations/stops it has. */
class BusRoute {

    /** The String representing name of a transit route. */
    private String routeName;
    /**
     * The Hash Map storing each station within a transit route. The key maintained by this Hash Map
     * is an Integer representing each station's ID number. The value maintained by this Hash Map is a
     * String representing each station's name.
     */
    private HashMap<String, Integer> stopOrders = new HashMap<>();

    /**
     * Getter of BusRoute.
     *
     * @param routeName, the identifier of a BusRoute.
     */
    BusRoute(String routeName) {
        this.routeName = routeName;
    }

    /**
     * Getter of routeName
     *
     * @return routeName, the identifier of a busRoute.
     */
    String getBusRouteName() {
        return routeName;
    }

    /**
     * getter of stopOrders.
     *
     * @return stopOrders.
     */
    HashMap<String, Integer> getStopOrders() {
        return stopOrders;
    }

    /**
     * The method that calculate the number of stops in a trip, if enter and exit distance is given.
     *
     * @param entreStation a String that tell what stop the bus trip starts(get on the bus).
     * @param exitStation a String that tell what stop the bus trip end(get off the bus).
     * @return the number of stops in a trip as primitive type int.
     */
    int calculateNumberOfStops(String entreStation, String exitStation) {
        return Math.abs(stopOrders.get(entreStation) - stopOrders.get(exitStation));
    }
}