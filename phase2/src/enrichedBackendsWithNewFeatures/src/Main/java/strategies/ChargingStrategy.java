package strategies;

import entity.TripSegment;
import entity.Card;
import impl.CommonStrategyFeatures;
import java.math.BigDecimal;

public interface ChargingStrategy {
    void setTripSegment(TripSegment tripSegment);
    BigDecimal calculateCost();
    BigDecimal calculateFare();
    CommonStrategyFeatures getCommonStrategyFeatures();
    void createNewTrip(Card card);
}