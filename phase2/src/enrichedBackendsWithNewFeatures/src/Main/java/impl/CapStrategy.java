package impl;

import java.math.BigDecimal;

import entity.Card;
import entity.GeneralTrip;
import dao.GeneralTripDao;
import dao.SystemInfoDao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.TimeManager;
import strategies.ChargingStrategyWithTransfer;

import entity.TripSegment;

/** The Charging Strategy used in phase one. */
@AllArgsConstructor
public class CapStrategy implements ChargingStrategyWithTransfer {
  /** The trip Segment of corresponding cap strategy */
  private TripSegment tripSegment;
  /** The maximum amount could be charged to this card for a joined trip. */
  private BigDecimal cap;
  /** Features shared with other charging strategies. */
  private CommonStrategyFeatures commonStrategyFeatures;

  @Getter private GeneralTripDao generalTripDao;
  private SystemInfoDao systemInfoDao;
  private TimeManager timeManager;

  /**
   * Construct a cap strategy.
   *
   * @param commonStrategyFeatures common features the cap strategy shared with other charging
   *     strategies
   * @param generalTripDao the tool connecting general trips and its data stored in database
   * @param systemInfoDao the tool accessing system information stored in database
   * @param timeManager the time manager this system use
   */
  public CapStrategy(
      CommonStrategyFeatures commonStrategyFeatures,
      GeneralTripDao generalTripDao,
      SystemInfoDao systemInfoDao,
      TimeManager timeManager) {
    this.commonStrategyFeatures = commonStrategyFeatures;
    commonStrategyFeatures.setChargingStrategy(this);
    this.generalTripDao = generalTripDao;
    this.systemInfoDao = systemInfoDao;
    this.timeManager = timeManager;
  }

  /**
   * Set the trip segment for this cap strategy.
   *
   * @param tripSegment a trip segment is a part of a complete trip
   */
  public void setTripSegment(TripSegment tripSegment) {
    this.tripSegment = tripSegment;
  }

  /**
   * If the trip segment satisfies shares the cap with the last trip segment, then merge merge these
   * two segments.
   *
   * @param lastTrip the last complete trip
   * @param lastTripSegmentOfLastTrip the last segment of the last complete trip
   */
  @Override
  public void mergeTrip(GeneralTrip lastTrip, TripSegment lastTripSegmentOfLastTrip) {
    commonStrategyFeatures.mergeTripHelper(lastTrip, tripSegment);
    BigDecimal capOfLastSegment =
        ((CapStrategy) lastTripSegmentOfLastTrip.getChargingStrategy()).getCap();
    setCap(capOfLastSegment);
  }

  /**
   * Get the corresponding trip segment of this cap strategy.
   *
   * @return a trip segment linked with this cap strategy
   */
  @Override
  public TripSegment getTripSegment() {
    return tripSegment;
  }

  /**
   * The cap amount of this cap strategy. It may differs from city to city.
   *
   * @return the cap amount of this cap strategy.
   */
  public BigDecimal getCap() {
    return cap;
  }

  /**
   * Set the cap amount for this cap strategy.
   *
   * @param cap the cap amount of this cap strategy
   */
  public void setCap(BigDecimal cap) {
    this.cap = cap;
  }

  /**
   * Get the features this cap strategy shares with other type of charging strategies.
   *
   * @return return a class stores such common features
   */
  @Override
  public CommonStrategyFeatures getCommonStrategyFeatures() {
    return commonStrategyFeatures;
  }

  /**
   * Calculate the fare for this cap strategy.
   *
   * @return return a big decimal showing the amount of fare a user should pay for this trip segment
   */
  public BigDecimal calculateFare() {
    if (commonStrategyFeatures.isFree(tripSegment)) {
      return new BigDecimal("0");
    } else {
      BigDecimal discountedFare = getDiscountedFare();
      setCap(cap.subtract(discountedFare).max(new BigDecimal("0")));
      return discountedFare;
    }
  }

  /**
   * If this cap strategy's corresponding trip segment does not share cap with the last segment of
   * last trip, then create a new trip.
   *
   * @param card The card of the corresponding trip segment
   */
  @Override
  public void createNewTrip(Card card) {
    commonStrategyFeatures.createNewTripHelper(tripSegment);
    setCap(systemInfoDao.getCapAmount(tripSegment.getCity()));
  }

  /**
   * Calculate the discounted fare of this trip segment.
   *
   * @return a big decimal representing the discounted fare of this trip segment.
   */
  private BigDecimal getDiscountedFare() {
    BigDecimal normalFare;
    if (tripSegment.getPayAtEntrance()) {
      normalFare = getNormalFareAtEntrance();
    } else {
      normalFare = getNormalFareAtExit();
    }
    BigDecimal discountRate = commonStrategyFeatures.getDiscountRate(tripSegment);
    return normalFare.multiply(discountRate);
  }

  /**
   * Get the fare before discount of this trip segment.
   *
   * @return a big decimal representing the fare before discount of this trip segment.
   */
  private BigDecimal getNormalFareAtEntrance() {
    BigDecimal fare;
    BigDecimal fixedFare = tripSegment.getPricingStrategyForRevenue().calculatePrice(true);
    BigDecimal cap = getCap();
    if (cap.compareTo(fixedFare) < 0) {
      fare = cap;
    } else {
      fare = fixedFare;
    }
    return fare;
  }

    /**
     * Similar to entrance, calculate the amount of fare a user should pay at exit before discount.
     *
     * @return a big decimal representing the amount of fare a user should pay for the trip.
     */
  private BigDecimal getNormalFareAtExit() {
    BigDecimal fare;
    BigDecimal fixedFare = tripSegment.getPricingStrategyForRevenue().calculatePrice(true);
    long hourDifference =
        timeManager.getHourDifference(
            timeManager.convertTimeFromLongToStringFormatTwo(tripSegment.getEnterTime()),
            timeManager.convertTimeFromLongToStringFormatTwo(tripSegment.getExitTime()));
    if (hourDifference < 2 && cap.compareTo(fixedFare) < 0) {
      fare = fixedFare;
    } else {
      fare = cap;
    }
    return fare;
  }

  public BigDecimal calculateCost() {
    return tripSegment.getPricingStrategyForCost().calculatePrice(false);
  }
}
