import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.io.Serializable;
import com.sun.xml.internal.ws.developer.Serialization;

import java.io.IOException;



public class Main {

    /**
     * The Hash Map storing each card within a transit system. The key maintained by this Hash Map
     * is a String representing each card's ID number. The value maintained by this Hash Map is a
     * Card object representing the card attached by the key of its ID number.
     */
    public static HashMap<String, Card> cardMap = new HashMap<>();
    /**
     * The Hash Map storing each cardholder within a transit system. The key maintained by this Hash Map
     * is a String representing each cardholder's email. The value maintained by this Hash Map is a
     * CardHolder object representing the cardholder attached by the key of its email.
     */
    static HashMap<String, NormalCardHolder> userMap = new HashMap<>();
    /**
     * The Hash Map storing each admin users within a transit system. Every single key in this Hash Map
     * is a String representing the city one admin user is in charge of and the corresponding keys are
     * admin users of respective cities.
     */
    static HashMap<String, AdminUser> adminUserMap = new HashMap<>();
    /**
     * The Hash Map storing each string representation of station as keys and that station's corresponding
     * cities as values.
     */
    static HashMap<String, String> stationCityMap = new HashMap<>();
    /**
     * This is a 2D hashMap saving all admin user's cost information. The first set of keys representing the
     * cities using our system,like "Toronto", "Montreal"etc,  and the second set of keys are transportation
     * methods like "Bus", "Subway" etc, and the corresponding value are doubles showing operating costs for
     * a certain traffic mode in certain city.
     */
    static HashMap<String, HashMap<String, Double>> costMap = new HashMap<>();
    /**
     * This is a 2D hashMap saving all admin user's revenue information. The first set of keys representing the
     * cities using our system,like "Toronto", "Montreal"etc,  and the second set of keys are transportation
     * methods like "Bus", "Subway" etc, and the corresponding value are doubles showing revenues that admin user
     * could gain from a certain traffic mode in certain city.
     */
    static HashMap<String, HashMap<String, Double>> revenueMap = new HashMap<>();
    /**
     * The Hash Map stores each file path containing a city's all transportation information as a key, and that
     * path's corresponding city as a value.
     */
    static HashMap<Path, String> pathCityMap = new HashMap<>();
    /**
     * The Hash Map stores each bus route's route name as a key and the corresponding bus route as a value.
     */
    static HashMap<String, BusRoute> busRoutesMap = new HashMap<>();
    /**
     * The Hash Map stores each city's name as key and that city's cap as a value.
     */
    static HashMap<String, Double> capMap = new HashMap<>();

    static HashMap<String, NormalCardHolder> cardHolderMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, ClassNotFoundException,ParseException {
        // Read all routes files.
        RoutesHandler.readRoutes();
        // Instantiate a new events handler.
        EventsHandler eventsHandler = EventsHandler.getEventsHandler_instance();
        // Let this events handler read and handle all events.
        eventsHandler.readEvents();
        // Exit this program.
        System.exit(0);

        // eden ...............
        // de-serialize the cards and cardholders to retrieve their information
        Serializer s = new Serializer();

        cardMap = (HashMap<String, Card>) s.readFromFile("cardMap.ser");
        cardHolderMap = (HashMap<String, NormalCardHolder>) s.readFromFile("cardHolderMap.ser");
//
//        // Read all routes files.
//        RoutesHandler.readRoutes();
//        // Instantiate a new events handler.
//        EventsHandler eventsHandler = EventsHandler.getEventsHandler_instance();
//        // Let this events handler read and handle all events.
//        eventsHandler.readEvents();

        // eden...........
        // serialize the cards and cardholders to store their information
        s.saveToFile(cardMap, "cardMap.ser");
        s.saveToFile(cardHolderMap, "cardHolderMap.ser");

        // Exit this program.
        System.exit(0);
    }
}
