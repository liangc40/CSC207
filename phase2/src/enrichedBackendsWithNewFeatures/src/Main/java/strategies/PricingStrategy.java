package strategies;

import entity.TripSegment;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(boolean isRevenue);
    void setTripSegment(TripSegment tripSegment);
}