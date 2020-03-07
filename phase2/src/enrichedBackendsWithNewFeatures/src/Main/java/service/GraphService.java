package service;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import entity.ENode;
import entity.Edge;
import entity.Node;
import entity.VNode;
import java.util.List;

@AllArgsConstructor
public class GraphService {
    /** A graph that store the nodes and edges. */
    private static int INF = Integer.MAX_VALUE;

    public VNode[] vertexArray;

    /**
     * Initiate a graph which stores the array of VNode and ENode.
     *
     * @param nodes: a instance extracted from database.
     * @param edges: a instance extracted from database.
     */
    public GraphService(List<Node> nodes, List<Edge> edges) {
        int vertexNum = nodes.size();
        int edgeNum = edges.size();
        // A vertexArray that record all the VNode
        vertexArray = new VNode[vertexNum];
        // A initiation of vertexArray. We extract info from node and dump into vertexArray an VNode
        // object.
        for (int i = 0; i < vertexArray.length; i++) {
            vertexArray[i] = new VNode();
            vertexArray[i].data = nodes.get(i).getName();
            vertexArray[i].firstEdge = null;
        }

        for (int i = 0; i < edgeNum; i++) {
            // get distance of an edge.
            double distance = (int) edges.get(i).getDistance();
            // find out the index of startNode and endNode of an edges.
            int p1 = edges.get(i).getStart() - 1;
            int p2 = edges.get(i).getStop() - 1;

            ENode node1 = new ENode();
            // This tell that the index of a node have their unique index which is the index in
            // vertexArray.
            node1.indexOfVertex = p2;
            node1.weight = distance;
            if (vertexArray[p1].firstEdge == null) {
                vertexArray[p1].firstEdge = node1;
            } else {
                linkLast(vertexArray[p1].firstEdge, node1);
            }

            ENode node2 = new ENode();
            node2.indexOfVertex = p1;
            node2.weight = distance;
            if (vertexArray[p2].firstEdge == null) {
                vertexArray[p2].firstEdge = node2;
            } else {
                linkLast(vertexArray[p2].firstEdge, node2);
            }
        }
    }

    /**
     * link up all the ENode which is the neighbour of a vertex.
     *
     * @param list: a object which stores the index of node it is pointing at, and weight of the edge,
     *     and the next ENode
     * @param node: a new ENode need to add to list.
     */
    private void linkLast(ENode list, ENode node) {
        ENode p = list;
        while (p.nextEdge != null) p = p.nextEdge;
        p.nextEdge = node;
    }

    private int getPosition(char ch) {
        //        for (int i = 0; i < vertexArray.length; i++) if (vertexArray[i].data == ch) return i;
        return -1;
    }

    /**
     * get the weight of two vertices, if they are neighbour, return their weight, otherwise return
     * INF.
     *
     * @param start: the index of start(in vertexArray)
     * @param end: the index of end(in vertexArray)
     * @return distance between start and end, can be infinitely far.
     */
    private double getWeight(int start, int end) {

        if (start == end) return 0;

        ENode node = vertexArray[start].firstEdge;
        while (node != null) {
            if (end == node.indexOfVertex) return node.weight;
            node = node.nextEdge;
        }

        return INF;
    }

    /**
     * pick a vertex and a pivot and check out its neighbours, and find the shortest path between
     * them.
     *
     * @param path a matrix stores the pivot between two points.
     * @param dist a matrix stores the distance between two points.
     */
    public void floyd(int[][] path, double[][] dist) {

        for (int i = 0; i < vertexArray.length; i++) {
            for (int j = 0; j < vertexArray.length; j++) {
                dist[i][j] = getWeight(i, j);
                path[i][j] = j;
            }
        }
        System.out.printf("path1: \n");
        for (int i = 0; i < vertexArray.length; i++) {
            for (int j = 0; j < vertexArray.length; j++) System.out.printf("%2d  ", path[i][j]);
            System.out.printf("\n");
        }
        for (int k = 0; k < vertexArray.length; k++) {
            for (int i = 0; i < vertexArray.length; i++) {
                for (int j = 0; j < vertexArray.length; j++) {

                    double tmp = (dist[i][k] == INF || dist[k][j] == INF) ? INF : (dist[i][k] + dist[k][j]);
                    if (dist[i][j] > tmp) {

                        dist[i][j] = tmp;

                        path[i][j] = path[i][k];
                    }
                }
            }
        }
    }

    int startIndex;
    int endIndex;
    String tripInfo = "";

    /**
     * get shortest path by inputting the starterName and endName.
     *
     * @param StartNode: the name of start Station
     * @param EndNode: the name of the end Station.
     * @param dist: the distance matrix
     * @param path: the path matrix
     * @param nodes: list of nodes
     */
    public void GetShortestPath(
            String StartNode, String EndNode, double[][] dist, int[][] path, List<Node> nodes) {

        int count = 0;
        Iterator nodesList = nodes.iterator();
        while (nodesList.hasNext()) {
            Node nodeGet = (Node) nodesList.next();
            if (nodeGet.getName().equals(StartNode)) {
                startIndex = count;
            }
            if (nodeGet.getName().equals(EndNode)) {
                endIndex = count;
            }
            count++;
        }

        tripInfo +=
                "\n ->  shortest distance: \n"
                        + " "
                        + StartNode
                        + " "
                        + EndNode
                        + " "
                        + dist[startIndex][endIndex];

        int k;
        k = path[startIndex][endIndex];

        tripInfo += "\n" + " shortest path: " + StartNode;

        while (k != endIndex) {
            tripInfo += "->" + " " + nodes.get(k).getName();
            k = path[k][endIndex];
        }

        tripInfo += "-> " + " " + EndNode;

        setTripInfo(tripInfo);
    }

    /**
     * setter for trip info
     *
     * @param tripInfo: the string representation of tripInfo
     */
    public void setTripInfo(String tripInfo) {
        this.tripInfo = tripInfo;
    }

    /**
     * getter for trip info
     *
     * @return the string representation of tripInfo
     */
    public String getTripInfo() {
        return this.tripInfo;
    }
}