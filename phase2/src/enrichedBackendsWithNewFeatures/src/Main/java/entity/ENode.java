package entity;

/**
 * Vertex in graph that have index called 'indexOfVertex' which shows the position of a vertex in vertexArray.
 * a double called 'weight' which gives you the weight of an edge. a ENode called nextEdge which give the next
 * ENode that is the neighbour of a VNode.
 */
public class ENode {

    public int indexOfVertex;
    public  double weight;
    public ENode nextEdge;
}