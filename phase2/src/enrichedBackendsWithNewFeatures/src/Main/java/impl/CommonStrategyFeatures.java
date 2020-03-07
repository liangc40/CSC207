package impl;

import java.math.BigDecimal;
import dao.*;
import entity.*;
import lombok.Setter;
import manager.TimeManager;
import dao.CardDao;
import dao.UserDao;
import manager.UserMoneyManager;
import strategies.ChargingStrategy;
import strategies.ChargingStrategyWithTransfer;

import java.sql.Time;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CommonStrategyFeatures {
    private ChargingStrategy chargingStrategy;
    private UserDao userDao;
    private CardDao cardDao;
    private SystemInfoDao systemInfoDao;
    private TimeManager timeManager;
    private TripSegmentDao tripSegmentDao;
    private GeneralTripDao generalTripDao;
    private TransitPassDao transitPassDao;
    private UserMoneyManagerDao userMoneyManagerDao;
    private ChildrenDiscountDao childrenDiscountDao;

    public CommonStrategyFeatures(
            UserDao userDao,
            CardDao cardDao,
            SystemInfoDao systemInfoDao,
            TimeManager timeManager,
            GeneralTripDao generalTripDao,
            TransitPassDao transitPassDao,
            UserMoneyManagerDao userMoneyManagerDao,
            ChildrenDiscountDao childrenDiscountDao) {
        this.systemInfoDao = systemInfoDao;
        this.cardDao = cardDao;
        this.userDao = userDao;
        this.timeManager = timeManager;
        this.transitPassDao = transitPassDao;
        this.userMoneyManagerDao = userMoneyManagerDao;
        this.childrenDiscountDao = childrenDiscountDao;
        this.generalTripDao = generalTripDao;
    }

    public void setChargingStrategy(ChargingStrategy chargingStrategy) {
        this.chargingStrategy = chargingStrategy;
    }

    public boolean transferAtExitStation(
            TripSegment lastSegmentOfLastTrip, TripSegment currentTripSegment) {
        return lastSegmentOfLastTrip.getExitStation().equals(currentTripSegment.getEnterStation());
    }

    public boolean withinMaximumCommutingTime(
            TripSegment firstSegmentOfLastTrip, TripSegment currentTripSegment, int hours) {
        String convertedTimeOne =
                timeManager.convertTimeFromLongToStringFormatTwo(firstSegmentOfLastTrip.getEnterTime());
        String convertedTimeTwo =
                timeManager.convertTimeFromLongToStringFormatTwo(currentTripSegment.getEnterTime());
        return timeManager.getMonthDifference(convertedTimeOne, convertedTimeTwo) < hours;
    }

    public void handleNewSegment(GeneralTrip lastTrip, Card card) {
        if (systemInfoDao
                .isStrategyTransferable(chargingStrategy.getClass().getSimpleName())
                .equals(true)
                && lastTrip != null) {
            TripSegment firstSegmentOfLastTrip = tripSegmentDao.get(lastTrip.getTripSegmentsIDs().get(0));
            int indexOfLastSegmentID = lastTrip.getTripSegmentsIDs().size() - 1;
            TripSegment lastSegmentOfLastTrip =
                    tripSegmentDao.get(lastTrip.getTripSegmentsIDs().get(indexOfLastSegmentID));
            if (isContinuous(firstSegmentOfLastTrip, lastSegmentOfLastTrip)) {
                ((ChargingStrategyWithTransfer) chargingStrategy)
                        .mergeTrip(lastTrip, lastSegmentOfLastTrip);
            } else {
                chargingStrategy.createNewTrip(card);
            }
        } else {
            chargingStrategy.createNewTrip(card);
        }
    }

    private boolean isContinuous(
            TripSegment firstSegmentOfLastTrip, TripSegment lastSegmentOfLastTrip) {
        return transferAtExitStation(
                lastSegmentOfLastTrip,
                ((ChargingStrategyWithTransfer) chargingStrategy).getTripSegment())
                && withinMaximumCommutingTime(
                firstSegmentOfLastTrip,
                ((ChargingStrategyWithTransfer) chargingStrategy).getTripSegment(),
                2);
    }

    /**
     * Return true if and only if the TripSegment currentTripSegment would be free for the card of
     * this trip. It could be free if the transit pass didn't expire or it's the passenger's birthday.
     *
     * @param tripSegment the current trip of the passenger
     * @return a boolean representing whether the current trip is free or not
     */
    public boolean isTransitPassFree(TripSegment tripSegment) {
        ArrayList<Integer> transitPassesIDs =
                cardDao.get(tripSegment.getCardID()).getTransitPassesIDs();
        for (Integer transitPassID : transitPassesIDs) {
            if (transitPassDao.get(transitPassID).getCity().equals(tripSegment.getCity())) {
                return true;
            }
        }
        return false;
    }

    public boolean isBirthdayFree(TripSegment tripSegment) {
        String chargingTime = getChargingTime(tripSegment);
        System.out.println("USER IS " + userDao.get(cardDao.get(tripSegment.getCardID()).getUserID()));
        String birthday = userDao.get(cardDao.get(tripSegment.getCardID()).getUserID()).getBirthday();
        return timeManager.isBirthday(chargingTime, birthday);
    }

    public boolean isFree(TripSegment tripSegment) {
        boolean birthdayApplicable = systemInfoDao.getBirthdayApplicability(tripSegment.getCity());
        boolean transitPassApplicable =
                systemInfoDao.getTransitPassApplicability(tripSegment.getCity());
        if (!birthdayApplicable && !transitPassApplicable) {
            return false;
        } else if (!birthdayApplicable) {
            return isTransitPassFree(tripSegment);
        } else {
            if (isBirthdayFree(tripSegment)) {
                return true;
            } else {
                return transitPassApplicable && isTransitPassFree(tripSegment);
            }
        }
    }

    private String getChargingTime(TripSegment tripSegment) {
        if (tripSegment.getPayAtEntrance()) {
            return timeManager.convertTimeFromLongToStringFormatTwo(tripSegment.getEnterTime());
        } else {
            return timeManager.convertTimeFromLongToStringFormatTwo(tripSegment.getEnterTime());
        }
    }

    BigDecimal getDiscountRate(TripSegment tripSegment) {
        ChildrenDiscount childrenDiscount = getChildrenDiscount(tripSegment);
        BigDecimal membershipDiscountRate = getMemberShipDiscountRate(tripSegment);
        if ((childrenDiscount != null)) {
            BigDecimal childrenDiscountRate = childrenDiscount.getDiscountRate();
            if ((membershipDiscountRate != null)) {
                return childrenDiscountRate.multiply(membershipDiscountRate);
            } else {
                return childrenDiscountRate;
            }
        } else if (membershipDiscountRate != null) {
            return membershipDiscountRate;
        } else {
            return new BigDecimal("1");
        }
    }

    private ChildrenDiscount getChildrenDiscount(TripSegment tripSegment) {
        return childrenDiscountDao.get(
                userMoneyManagerDao
                        .get(
                                userDao
                                        .get(cardDao.get(tripSegment.getCardID()).getUserID())
                                        .getUserMoneyManagerID())
                        .getChildrenDiscountID());
    }

    private BigDecimal getMemberShipDiscountRate(TripSegment tripSegment) {
        return userMoneyManagerDao
                .get(userDao.get(cardDao.get(tripSegment.getCardID()).getUserID()).getUserMoneyManagerID())
                .getMembershipDiscountRate();
    }

    void createNewTripHelper(TripSegment tripSegment) {
        GeneralTrip newTrip = new GeneralTrip();
        newTrip.setEnterStation(tripSegment.getEnterStation());
        newTrip.getTripSegmentsIDs().add(tripSegment.getTripSegmentID());
        GeneralTrip savedGeneralTrip = generalTripDao.save(newTrip);
        tripSegment.getTripInfoNotifier().addNewTrip(savedGeneralTrip);
    }

    void mergeTripHelper(GeneralTrip lastTrip, TripSegment tripSegment) {
        lastTrip.getTripSegmentsIDs().add(tripSegment.getTripSegmentID());
        generalTripDao.update(lastTrip);
    }
}
