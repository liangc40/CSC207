package service;

import dao.*;
import manager.TimeManager;
import entity.Card;
import entity.ChildrenDiscount;
import entity.TripSegment;
import entity.GeneralTrip;
import factories.ChargingStrategyFactory;
import factories.PricingStrategyFactory;
import exception.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import manager.PastTripManager;
import manager.TripInfoNotifier;
import strategies.ChargingStrategy;
import strategies.PricingStrategy;

@AllArgsConstructor
public class TripSegmentService {
    @Getter
    private final TimeManager timeManager;
    private final SystemInfoDao systemInfoDao;
    private final TripSegmentDao tripSegmentDao;
    private final PastTripManagerDao pastTripManagerDao;
    private final PastTripManagerService pastTripManagerService;
    private final PricingStrategyFactory pricingStrategyFactory;
    private final ChargingStrategyFactory chargingStrategyFactory;


    public TripSegment createNewTripSegment(TripInfoNotifier tripInfoNotifier) {
        TripSegment savedTripSegment = tripSegmentDao.save();
        savedTripSegment.setTripInfoNotifier(tripInfoNotifier);
        return savedTripSegment;
    }


    public void enterStation(TripSegment tripSegment, String station, Card card, String trafficMode)
            throws InvalidStrategyException {
        TripSegment updatedTripSegment = setEnterInformation(tripSegment, station, trafficMode, card);
        if (card.isActive()) {
            PastTripManager pastTripManager = pastTripManagerDao.get(card.getPastTripManagerID());
            GeneralTrip lastTrip = pastTripManagerService.getLastTrip(pastTripManager);
            updatedTripSegment.getChargingStrategy().getCommonStrategyFeatures().handleNewSegment(lastTrip, card);
            if (updatedTripSegment.getPayAtEntrance()) {
                BigDecimal fare = updatedTripSegment.getChargingStrategy().calculateFare();
                updatedTripSegment.getTripInfoNotifier().notifyBalanceChange(fare,  "update revenue");
            }
        } else {
            System.out.println("This card is invalid. ");
        }
    }

    public void exitStation(TripSegment tripSegment, String station) {
        TripSegment updatedTripSegment = setExitInformation(tripSegment, station);
        ChargingStrategy chargingStrategy = updatedTripSegment.getChargingStrategy();
        BigDecimal cost = chargingStrategy.calculateCost();
        if (!updatedTripSegment.getPayAtEntrance()) {
            BigDecimal fare = (chargingStrategy).calculateFare();
            updatedTripSegment.getTripInfoNotifier().notifyBalanceChange(fare, "update revenue");
        }
        updatedTripSegment.getTripInfoNotifier().notifyBalanceChange(cost,  "update cost");
        updatedTripSegment.getTripInfoNotifier().updateDestination();
    }


    private TripSegment setEnterInformation(TripSegment tripSegment, String station, String trafficMode, Card card) throws InvalidStrategyException {
        tripSegment.setCardID(card.getCardID());
        tripSegment.setEnterStation(station);
        String city = systemInfoDao.getCityOfCorrespondingStation(station);
        tripSegment.setCity(city);
        tripSegment.setTrafficMode(trafficMode);
        tripSegment.setChargingStrategy(chargingStrategyFactory.createNewStrategy(systemInfoDao.getChargingStrategy(city)));
        tripSegment.getChargingStrategy().setTripSegment(tripSegment);
        tripSegment.setPricingStrategyForRevenue(pricingStrategyFactory.createNewStrategy(systemInfoDao.getPricingStrategy(city, trafficMode, true)));
        tripSegment.setPricingStrategyForCost(pricingStrategyFactory.createNewStrategy(systemInfoDao.getPricingStrategy(city, trafficMode, false)));
        tripSegment.getPricingStrategyForRevenue().setTripSegment(tripSegment);
        tripSegment.getPricingStrategyForCost().setTripSegment(tripSegment);
        tripSegment.setPayAtEntrance(systemInfoDao.getChargingTime(city, trafficMode));
        TripSegment updatedTripSegment = tripSegmentDao.updateInformationAtEntrance(tripSegment);
        updatedTripSegment.getTripInfoNotifier().setTripSegment(updatedTripSegment);
        return updatedTripSegment;
    }

    private TripSegment setExitInformation(TripSegment tripSegment, String station) {
        tripSegment.setExitStation(station);
        TripSegment updatedTripSegment = setStopsOrDistance(tripSegment);
        tripSegmentDao.updateInformationAtExit(updatedTripSegment);
        return updatedTripSegment;
    }

    private TripSegment setStopsOrDistance(TripSegment tripSegment) {
        String strategyNameOne = tripSegment.getPricingStrategyForRevenue().getClass().getSimpleName();
        String strategyNameTwo = tripSegment.getPricingStrategyForCost().getClass().getSimpleName();
        if (strategyNameOne.equals("StopStrategy") || strategyNameTwo.equals("StopStrategy")) {
            tripSegment.setStopsTraveled(new BigDecimal("5"));
        } else if (strategyNameOne.equals("DistanceStrategy") || strategyNameTwo.equals("DistanceStrategy")) {
            tripSegment.setStopsTraveled( new BigDecimal("5"));
        }
        return tripSegment;
    }
}