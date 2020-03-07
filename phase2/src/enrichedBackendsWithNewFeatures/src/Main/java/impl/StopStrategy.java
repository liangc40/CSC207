package impl;

import dao.SystemInfoDao;
import entity.TripSegment;
import lombok.AllArgsConstructor;
import strategies.PricingStrategy;

import java.math.BigDecimal;

@AllArgsConstructor
public class StopStrategy implements PricingStrategy {
    //    private final RevenueCostDao revenueCostDao;
    private SystemInfoDao systemInfoDao;
    private TripSegment tripSegment;

    public StopStrategy(SystemInfoDao systemInfoDao) {
        this.systemInfoDao = systemInfoDao;
    }

    @Override
    public BigDecimal calculatePrice(boolean isRevenue) {
        if (isRevenue) {
            return systemInfoDao
                    .getRevenue(tripSegment.getCity(), tripSegment.getTrafficMode())
                    .multiply(tripSegment.getStopsTraveled());
        } else {
            return systemInfoDao
                    .getCost(tripSegment.getCity(), tripSegment.getTrafficMode())
                    .multiply(tripSegment.getStopsTraveled());
        }
    }

    @Override
    public void setTripSegment(TripSegment tripSegment) {
        this.tripSegment = tripSegment;
    }
}
