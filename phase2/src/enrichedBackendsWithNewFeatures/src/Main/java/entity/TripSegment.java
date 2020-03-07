package entity;

import lombok.Data;
import manager.TripInfoNotifier;
import strategies.PricingStrategy;
import strategies.ChargingStrategy;
import java.math.BigDecimal;

/** A TripSegment of a GeneralTrip. */
@Data
public class TripSegment {
    /** ID in database. */
    private int tripSegmentID;
    /** ID of Card of this TripSegment. */
    private int cardID;
    /** enter Time of this TripSegment. */
    private long enterTime;
    /** exit Time of this TripSegment. */
    private long exitTime;
    /** enter Station of this TripSegment. */
    private String enterStation;
    /** exit Station of this TripSegment. */
    private String exitStation;
    /** city of this TripSegment. */
    private String city;
    /** True if it is paid at entrance. */
    private Boolean payAtEntrance;
    /** stopsTraveled of this TripSegment. */
    private BigDecimal stopsTraveled;
    /** distanceTraveled of this TripSegment. */
    private BigDecimal distanceTraveled;
    /** trafficMode of this TripSegment. */
    private String trafficMode;
    /** ChargingStrategy of this TripSegment. */
    private ChargingStrategy chargingStrategy;
    /** pricingStrategyForRevenue of this TripSegment. */
    private PricingStrategy pricingStrategyForRevenue;
    /** pricingStrategyForCost of this TripSegment. */
    private PricingStrategy pricingStrategyForCost;
    /** tripInfoNotifier of this TripSegment. */
    private TripInfoNotifier tripInfoNotifier;
}
