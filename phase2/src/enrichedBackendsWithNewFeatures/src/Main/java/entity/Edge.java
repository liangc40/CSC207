package entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * the object Edge that have eid, start, stop, edge, distance, duration.
 */
@Data
public class Edge {

    @Getter
    @Setter
    /** ID in database. */
    private int eid;
    /** Starting station. */
    private int start;
    /** Ending station. */
    private int stop;
    /** Type of transit. */
    private String edge;
    /** Distance between two stations. */
    private double distance;
    /** The time it takes to go from start to stop. */
    private int duration;
}