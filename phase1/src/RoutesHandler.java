import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/** A Routes Handler which reads route files and build a map for a city's transportation system. */
public class RoutesHandler {
    /**
     * Read txt files representing transportation systems of different cities and build "maps" for
     * these cities cities respectively in order to calculate the minimum number of stops between two
     * subway stations or two bus stops on the same route. In our case, we read "Montréal Routes.txt"
     * which contains the information of subway routes and bus routes of Montréal city, and we also
     * read "Toronto Routes.txt" which shows the Toronto's bus and subway routes information. After
     * reading these two files, we could build subway and bus networks for Toronto and Montréal
     * respectively. Moreover, we could read routes way more than just "Montréal Routes.txt" and
     * "Toronto Routes.txt" since we could read more transportation info of different cities and
     * associate these cities with the path built for its txt file. In other words, the readRoutes
     * method could be extensible to as many cities as we want.
     */
    public static void readRoutes() {
        // Create a new arraylist to save route path.
        ArrayList<Path> routePathList = new ArrayList<>();
        // Build a path for file "Montréal Routes.txt"
        Path routesPath1 = Paths.get("Montréal Routes.txt");
        // Store this path as key and city name "Montréal" as value into pathCityMap in Main class.
        // The reason to do this is that when reading route information of a subway line or a bus route,
        // we need to associate every single subway station or bus stop with the city it's actually in,
        // in order to find out where the passenger by reading the entre information his/her card
        // generates.
        Main.pathCityMap.put(routesPath1, "Montréal");
        // Build a path for file "Toronto Routes.txt"
        Path routesPath2 = Paths.get("Toronto Routes.txt");
        // Store this path as key and city name "Toronto" as value into pathCityMap in Main class.
        Main.pathCityMap.put(routesPath2, "Toronto");
        // Add these two path to path.
        routePathList.add(routesPath1);
        routePathList.add(routesPath2);

        // For every single route path saved in route path list, get its associated city name from the
        // pathCityMap in main method, and read this route path line by line.
        for (Path routesPath : routePathList) {
            try (BufferedReader routesFile = Files.newBufferedReader(routesPath)) {
                String cityName = Main.pathCityMap.get(routesPath);
                String transitLine = routesFile.readLine();
                // As long as such line is not empty, split this line by dashes and get the first elements
                // to
                // check what kind of transportation method this route is and set this element as its route
                // name.
                // For example, for "Bus Route 510", we could know that the transportation method is Bus and
                // we set
                // "Bus Route 510" as this route's route name.
                while (transitLine != null) {
                    String[] routeInfo = transitLine.split("\\|");
                    String routeName = routeInfo[0];
                    // If the new route is a subway, read all station names, which are the second element all
                    // the way
                    // to the last one, and associate these stations with cities they are in.
                    if (routeName.contains("Subway")) {
                        // Create a new array called stationArray to store all string representation of stations
                        // of one subway line.
                        ArrayList<String> stationArray = new ArrayList<>();
                        for (int i = 1; i < routeInfo.length; i++) {
                            // Associate each station name with the city it's in.
                            Main.stationCityMap.put(routeInfo[i], cityName);
                            // Add this station name to stationArray.
                            stationArray.add(routeInfo[i]);
                        }
                        // Add this array list of station names to stationContainer
                        SubwayGraph.getStationContainer().add(stationArray);
                    } else if (routeName.contains("Bus")) {
                        // If the new route is a bus route, initialize a new bus route with route name
                        BusRoute busRoute = new BusRoute(routeName);
                        // For stations on this route, mark them from 1 all the way to the length of this
                        // bus route accordingly.
                        for (int stationID = 1; stationID <= routeInfo.length - 1; stationID++) {
                            busRoute.getStopOrders().put(routeInfo[stationID], stationID);
                            //
                            Main.stationCityMap.put(routeInfo[stationID], cityName);
                            Main.busRoutesMap.put(routeName, busRoute);
                        }
                    }
                    transitLine = routesFile.readLine();
                }
                // In subway graph, convert all stations in stationOrders to nodes.
                SubwayGraph.putAllStationToNodes();
                // In subway graph, set up edges among nodes.
                SubwayGraph.setUpEdgeForEachNode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}