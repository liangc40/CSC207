package strategies;

import entity.GeneralTrip;
import entity.TripSegment;

public interface ChargingStrategyWithTransfer extends ChargingStrategy{
    void mergeTrip(GeneralTrip lastTrip, TripSegment lastTripSegmentOfLastTrip);
    TripSegment getTripSegment();
}