import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javafx.util.Pair;


public class SubwayGraph {
    /** Stores a list of unique nodes in this Graph. */
    private static ArrayList<String> nodes = new ArrayList<>();

    /** Creates a mapping from a node to its neighbours. */
    private static Map<String, ArrayList<String>> subwayMap = new HashMap<>();

    /** Store the subway information in a nested ArrayList. each line is a ArrayList. */
    private static ArrayList<ArrayList<String>> stationContainer = new ArrayList<>();

    /** A map such that each station string maps to its unique path from source. */
    private static Map<String, ArrayList<String>> shortestPath = new HashMap<>();

    /** A shortcut map the pair (source, distance) to the shortest distance between them. */
    private static Map<Pair<String, String>, ArrayList<String>> pathCollection = new HashMap<>();

    /**
     * A getter for the pathCollection.
     *
     * @return a map such that a pair of source and destination maps to the shortest path, which is an
     *     arrayList.
     */
    public static Map<Pair<String, String>, ArrayList<String>> getPathCollection() {
        return pathCollection;
    }

    /**
     * a setter for stationContainer with is a nested array of String such that it contains all lines,
     * each line wrapped as an arrayList.This method does not return anything.
     *
     * @param stationContainer a nested arrayList that stores line information, each line is wrapped
     *     as an arrayList.
     */
    public static void setStationContainer(ArrayList<ArrayList<String>> stationContainer) {
        SubwayGraph.stationContainer = stationContainer;
    }

    /**
     * a getter for stationContainer.
     *
     * @return stationContainer, which is a nested arrayList that stores line information, each line
     *     is wrapped as an arrayList.
     */
    public static ArrayList<ArrayList<String>> getStationContainer() {
        return stationContainer;
    }

    /**
     * getter for nodes.
     *
     * @return nodes, which is a arrayList containing all the unique station showing up at
     *     stationContainer.
     */
    public static ArrayList<String> getNodes() {

        return nodes;
    }

    /**
     * getter for subwayMap.
     *
     * @return a subwayMap, which maps a each station to the arrayList of stations which is a
     *     neighbour of that.
     */
    public static Map<String, ArrayList<String>> getSubwayMap() {
        return subwayMap;
    }

    /**
     * Extract all the station name from stationContainer file into nodes. This method returns
     * nothing.
     */
    static void putAllStationToNodes() {
        for (ArrayList<String> array : stationContainer) {
            for (String station : array) {
                // I want to make a set(where all element is unique)
                if (!(nodes.contains(station))) {
                    nodes.add(station);
                }
            }
        }
    }

    /**
     * Iterate through all node and find the neighbour and register it into subwayMap. This method
     * returns nothing.
     */
    static void setUpEdgeForEachNode() {
        for (String station : nodes) {
            SubwayGraph.setAdjacentInAllLine(station);
        }
    }

    /**
     * Go through all line and find all the neighbour.This method returns nothing.
     *
     * @param station the station to which the neighbour is registered.
     */
    static void setAdjacentInAllLine(String station) {
        ArrayList<String> neighbourContainer = new ArrayList<>();
        for (ArrayList<String> array : stationContainer) {
            // there might be many neighbours, i need to get all of them, this is close to updating
            // neighbourContainer
            neighbourContainer = SubwayGraph.setAdjacentInOneLine(array, station, neighbourContainer);
        }
        // store all the neighbour into subwayMap.
        subwayMap.put(station, neighbourContainer);
    }

    /**
     * Go through one line and find the neighbour in this line.
     *
     * @param array an arrayList of String which is the array where we are looking for the neighbour.
     * @param station a String which is the station to which the neighbours are registered.
     * @param neighbour a arrayList which storing the neighbours.
     * @return the arrayList of neighbour in this line.
     */
    static ArrayList<String> setAdjacentInOneLine(
            ArrayList<String> array, String station, ArrayList<String> neighbour) {
        String prev;
        String next;
        int positionNumber = 0;
        boolean foundAdjacent = false;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(station)) {
                // if found our target station, locate its position.
                positionNumber = i;
                foundAdjacent = true;
            }
        }
        // iterate again and get its neighbour.
        if (foundAdjacent) {
            if (positionNumber == 0) {
                next = array.get(1);
                neighbour.add(next);
            } else if (positionNumber == array.size() - 1) {
                prev = array.get(positionNumber - 1);
                neighbour.add(prev);
            } else {
                prev = array.get(positionNumber - 1);
                next = array.get(positionNumber + 1);
                neighbour.add(prev);
                neighbour.add(next);
            }
        }
        return neighbour;
    }

    /**
     * Finds the shortest path between two nodes (source and destination) in a graph.
     *
     * @param source a String which is the start station of this transport.
     * @param destination a String which is The destination of the transport.
     * @return the shortest distance between source and destination.
     */
    static int calculateDistance(String source, String destination) {
        // check the pathCollection, if this assignment was made before, return the distance directly.
        if (pathCollection.containsKey(new Pair<>(source, destination))) {
            return pathCollection.get(new Pair<>(source, destination)).size() - 1;
        }
        // different start point have different path, therefore we have to clean it up to prepare for
        // this calculation.
        shortestPath.clear();
        ArrayList<String> visited = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        // Initiate BFS.
        queue.add(source);
        // Initiation of the path(so that neighbour can get teh path ahead of it. )
        ArrayList<String> initialPath = new ArrayList<>();
        initialPath.add(source);
        shortestPath.put(source, initialPath);
        // A standard bfs queue popping processing.
        while ((queue.size() != 0)) {
            // pop the head, so that it is visited.
            String headOfQueue = queue.remove();
            visited.add(headOfQueue);
            // if we hit the destination, break the iteration.
            if (headOfQueue.equals(destination)) {
                break;
            }
            // tear up the headOfQueue and return all its neighbours then dump to the back of queue.
            ArrayList<String> neighbours = subwayMap.get(headOfQueue);
            for (String neighbour : neighbours) {
                if (!queue.contains(neighbour) && !visited.contains(neighbour)) {
                    // get the previous path with out aliasing.
                    ArrayList<String> oldPath = new ArrayList<>(shortestPath.get(headOfQueue));
                    oldPath.add(neighbour);
                    shortestPath.put(neighbour, oldPath);
                    queue.add(neighbour);
                }
            }
        }
        // previous condition: the station exists in the subway map.
        ArrayList<String> path = shortestPath.get(destination);
        int lengthOfPath = path.size() - 1;
        pathCollection.put(new Pair<>(source, destination), path);
        return lengthOfPath;
    }

    /**
     * This is a method that give to the shortest path, as an arrayList type, if source and
     * destination is given. We first checkout is there as path exists in pathCollection. if not we
     * generate it through calculating distance. and then recall this method.
     *
     * @param source the starter node of this trip.
     * @param destination the destination of this trip.
     * @return the shortest path between source and destination, as an arrayList of String.
     * @throws Exception if we still cannot find the path if we recall this method, which means the
     *     calculateDistance function goes wrong.
     */
    static ArrayList<String> getShortestPath(String source, String destination) throws Exception {
        if (!(pathCollection.containsKey(new Pair<>(source, destination)))) {
            calculateDistance(source, destination);
            if (!(pathCollection.containsKey(new Pair<>(source, destination)))) {
                throw new Exception("The path cannot be registered to pathCollection.");
            } else {
                return pathCollection.get(new Pair<>(source, destination));
            }
        } else {
            return pathCollection.get(new Pair<>(source, destination));
        }
    }
}
