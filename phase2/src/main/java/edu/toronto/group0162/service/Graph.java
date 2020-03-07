package edu.toronto.group0162.service;

import edu.toronto.group0162.entity.ENode;
import edu.toronto.group0162.entity.Edge;
import edu.toronto.group0162.entity.Node;
import edu.toronto.group0162.entity.VNode;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
public class Graph {
    /**
     * A graph that store the nodes and edges.
     */

    private static int INF = Integer.MAX_VALUE;

    public VNode[] vertexArray;

    /**
     * Initiate a graph which stores the array of VNode and ENode.
     *
     * @param nodes: a instance extracted from database.
     * @param edges: a instance extracted from database.
     */
    public Graph(List<Node> nodes, List<Edge> edges) {
        int vertexNum = nodes.size();
        int edgeNum = edges.size();
        // A vertexArray that record all the VNode
        vertexArray = new VNode[vertexNum];
        // A initiation of vertexArray. We extract info from node and dump into vertexArray an VNode object.
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
            // This tell that the index of a node have their unique index which is the index in vertexArray.
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
     * @param list: a object which stores the index of node it is pointing at, and weight of the edge, and the next ENode
     * @param node: a new ENode need to add to list.
     */
    private void linkLast(ENode list, ENode node) {
        ENode p = list;
        while (p.nextEdge != null) p = p.nextEdge;
        p.nextEdge = node;
    }

    /**
     * get the weight of two vertices, if they are neighbour, return their weight, otherwise return INF.
     *
     * @param start: the index of start(in vertexArray)
     * @param end:   the index of end(in vertexArray)
     * @return distance between start and end, can be infinitely far.
     */
    private double getWeight(int start, int end) {

        if (start == end) return 0;

        ENode node = vertexArray[start].firstEdge;
        while (node != null) {
            if (end == node.indexOfVertex)
                return node.weight;
            node = node.nextEdge;
        }

        return INF;
    }

    /**
     * pick a vertex and a pivot and check out its neighbours, and find the shortest path between them.
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


    String tripInfo = "";

    /**
     * get shortest path by inputting the starterName and endName.
     *
     * @param startNode: the name of start Station
     * @param endNode:   the name of the end Station.
     * @param dist:      the distance matrix
     * @param path:      the path matrix
     * @param nodes:     list of nodes
     */
    public void GetShortestPath(String startNode, String endNode, double[][] dist, int[][] path, List<Node> nodes) {
        int count = 0;
        int startIndex = 0;
        int endIndex = 0;
        if (startNode.equals(endNode)){
            tripInfo += "\n ->  shortest distance: \n" + " " + startNode + " " + endNode + " " + 0;
            tripInfo += "\n" + " shortest path: " + "";
            setTripInfo(tripInfo);
        }else{
            Iterator nodesList = nodes.iterator();
            while (nodesList.hasNext()) {
                Node nodeGet = (Node) nodesList.next();
                if (nodeGet.getName().equals(startNode)) {
                    startIndex = count;
                }
                if (nodeGet.getName().equals(endNode)) {
                    endIndex = count;
                }
                count++;
            }

            ArrayList<Integer> shortestPathIndex = getAllVertices(startIndex, endIndex, path);

            ArrayList<String> wholeVertexByName = new ArrayList<>();
            for(Integer index: shortestPathIndex){
                wholeVertexByName.add(this.vertexArray[index].data);
            }

            tripInfo += "\n ->  shortest distance: \n" + " " + startNode + " " + endNode + " " + dist[startIndex][endIndex];
            tripInfo += "\n" + " shortest path: " + wholeVertexByName.get(0);
            for(int i = 1; i < wholeVertexByName.size() ; i++){
                    tripInfo += "->" + " " + wholeVertexByName.get(i);
            }

            setTripInfo(tripInfo);
        }


    }

    /**
     * An exclusive helper method for GetShortestPath
     *
     * @param startIndex: the index in vertexArray.
     * @param endIndex: the index in vertexArray.
     * @param path: path Matrix.
     * @return an arraylist have all vertices in the path.
     */
    private ArrayList<Integer> getAllVertices(int startIndex, int endIndex, int[][] path){
        if(this.isNeighbour(startIndex, endIndex)){
            ArrayList<Integer> array = new ArrayList<>();
            array.add(startIndex);
            array.add(endIndex);
            return array;
        }else{
            int mediumIndex = path[startIndex][endIndex];
            ArrayList<Integer> startNodeSide = getAllVertices(startIndex, mediumIndex, path);
            ArrayList<Integer> endNodeSide = getAllVertices(mediumIndex, endIndex, path);
            ArrayList<Integer> wholeArray = new ArrayList<>();
            startNodeSide.remove(startNodeSide.size() -1);
            wholeArray.addAll(startNodeSide);
            wholeArray.addAll(endNodeSide);
            return wholeArray;
        }
    }

    /**
     * is two vertices neighbour?
     *
     * @param startIndex: the index in vertexArray.
     * @param endIndex: the index in vertexArray.
     * @return a boolean tells whether they are neighbour.
     */
    private boolean isNeighbour(int startIndex, int endIndex){
        VNode startNode = this.vertexArray[startIndex];

        ENode next;
        next = startNode.firstEdge;
        while (next != null){
            if(next.indexOfVertex == endIndex){
                return true;
            }else {next = next.nextEdge;}
        }
        return false;
    }
    
    /**
     * setter for trip info
     *
     * @param tripInfo: the string representation of tripInfo
     */
    private void setTripInfo(String tripInfo) {
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
