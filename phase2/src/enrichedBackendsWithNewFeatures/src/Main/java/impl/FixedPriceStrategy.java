package impl;

import dao.SystemInfoDao;
import entity.TripSegment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import strategies.PricingStrategy;

import java.math.BigDecimal;

@AllArgsConstructor
public class FixedPriceStrategy implements PricingStrategy{
    private TripSegment tripSegment;

    @Getter
    private SystemInfoDao systemInfoDao;

    public FixedPriceStrategy(SystemInfoDao systemInfoDao) {
        this.systemInfoDao = systemInfoDao;
    }

    @Override
    public BigDecimal calculatePrice(boolean isRevenue) {
        if (isRevenue) {
            return systemInfoDao.getRevenue(tripSegment.getCity(), tripSegment.getTrafficMode());
        } else {
            return systemInfoDao.getCost(tripSegment.getCity(), tripSegment.getTrafficMode());
        }
    }

    @Override
    public void setTripSegment(TripSegment tripSegment) {
        this.tripSegment = tripSegment;
    }
}