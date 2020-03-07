package entity;

import lombok.Data;
import java.util.ArrayList;

@Data
/** A GeneralTrip that is a complete trip. */
public class GeneralTrip {
    /** ID in database. */
    private int generalTripID;
    /** Enter station of this GeneralTrip. */
    private String enterStation;
    /** Exit station of this GeneralTrip. */
    private String exitStation;
    /** ID of the TripSegment's of this GeneralTrip. */
    private ArrayList<Integer> tripSegmentsIDs = new ArrayList<>();
}