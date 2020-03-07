package impl;

import java.math.BigDecimal;

import entity.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import strategies.ChargingStrategyWithTransfer;
import entity.TripSegment;
import dao.GeneralTripDao;
import entity.GeneralTrip;

@AllArgsConstructor
public class TransferStrategy implements ChargingStrategyWithTransfer {
    private TripSegment tripSegment;
    private CommonStrategyFeatures commonStrategyFeatures;
    private Boolean isFirstSegment;

    @Getter
    private GeneralTripDao generalTripDao;

    //eden....
    public TransferStrategy(CommonStrategyFeatures commonStrategyFeatures) {
        this.commonStrategyFeatures = commonStrategyFeatures;
        commonStrategyFeatures.setChargingStrategy(this);
    }

    @Override
    public void setTripSegment(TripSegment tripSegment) {
        this.tripSegment = tripSegment;
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

    @Override
    public void createNewTrip(Card card) {
        commonStrategyFeatures.createNewTripHelper(tripSegment);
        this.isFirstSegment = true;

    }

    private BigDecimal getDiscountedFare() {
        BigDecimal normalFare;
        normalFare = getNormalFare();
        BigDecimal discountRate = commonStrategyFeatures.getDiscountRate(tripSegment);
        return normalFare.multiply(discountRate);
    }

    private BigDecimal getNormalFare() {
        if (isFirstSegment) {
            return tripSegment.getPricingStrategyForRevenue().calculatePrice(true);
        }
        return new BigDecimal("0");
    }

    @Override
    public BigDecimal calculateCost() {
        return tripSegment.getPricingStrategyForCost().calculatePrice(false);
    }

    @Override
    public TripSegment getTripSegment() {
        return tripSegment;
    }


    @Override
    public void mergeTrip(GeneralTrip lastTrip, TripSegment lastTripSegmentOfLastTrip) {
        commonStrategyFeatures.mergeTripHelper(lastTrip, tripSegment);
        this.isFirstSegment = false;
    }

    @Override
    public CommonStrategyFeatures getCommonStrategyFeatures() {
        return commonStrategyFeatures;
    }

}

