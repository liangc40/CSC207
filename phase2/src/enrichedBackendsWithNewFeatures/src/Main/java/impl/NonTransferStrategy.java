package impl;
import entity.Card;
import strategies.ChargingStrategy;
import entity.TripSegment;
import entity.GeneralTrip;


import java.math.BigDecimal;

public class NonTransferStrategy implements ChargingStrategy{
    private TripSegment tripSegment;
    private CommonStrategyFeatures commonStrategyFeatures;

    @Override
    public void setTripSegment(TripSegment tripSegment) {
        this.tripSegment = tripSegment;
    }

    @Override
    public BigDecimal calculateCost() {
        return tripSegment.getPricingStrategyForCost().calculatePrice(false);
    }

    @Override
    public BigDecimal calculateFare() {
        if (commonStrategyFeatures.isFree(tripSegment)) {
            return new BigDecimal("0");
        } else {
            BigDecimal discountedFare = getDiscountedFare();
            return discountedFare;
        }
    }

    private BigDecimal getDiscountedFare() {
        BigDecimal normalFare;
        normalFare = getNormalFare();
        BigDecimal discountRate = commonStrategyFeatures.getDiscountRate(tripSegment);
        return normalFare.multiply(discountRate);
    }

    private BigDecimal getNormalFare() {
        return tripSegment.getPricingStrategyForRevenue().calculatePrice(true);
    }

    @Override
    public CommonStrategyFeatures getCommonStrategyFeatures() {
        return getCommonStrategyFeatures();
    }

    @Override
    public void createNewTrip(Card card) {
        commonStrategyFeatures.createNewTripHelper(tripSegment);
    }
}