package service;

import dao.*;
import entity.TripSegment;
import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import manager.PastTripManager;
import entity.GeneralTrip;
import table.OneWayTripFrequency;
import table.TripFrequencyBetweenStations;
// import table.TripFrequencyBetweenStations;

@AllArgsConstructor
public class PastTripManagerService {

    @Getter private static GeneralTripDao generalTripDao;
    private static  OneWayTripFrequencyDao oneWayTripFrequencyDao;
    private static PastTripManagerDao pastTripManagerDao;
    private static TripFrequencyBetweenStationsDao tripFrequencyBetweenStationsDao;

    public PastTripManagerService(GeneralTripDao generalTripDao, OneWayTripFrequencyDao oneWayTripFrequencyDao, TripFrequencyBetweenStationsDao tripFrequencyBetweenStationsDao, PastTripManagerDao pastTripManagerDao) {
        PastTripManagerService.oneWayTripFrequencyDao = oneWayTripFrequencyDao;
        PastTripManagerService.tripFrequencyBetweenStationsDao = tripFrequencyBetweenStationsDao;
        PastTripManagerService.pastTripManagerDao = pastTripManagerDao;
        PastTripManagerService.generalTripDao = generalTripDao;
    }

    public static PastTripManager packagePastTripManager(PastTripManager pastTripManager) {
        pastTripManager.setGeneralTripDao(generalTripDao);
        pastTripManager.setPastTripManagerDao(pastTripManagerDao);
        return pastTripManager;
    }

    public GeneralTrip getLastTrip(PastTripManager pastTripManager) {
        int sizeOfPastTrips = pastTripManager.getPastTripIDs().size();
        if (sizeOfPastTrips != 0) {
            return generalTripDao.get(pastTripManager.getPastTripIDs().get(sizeOfPastTrips-1));
        }
        return null;
    }


    public static void decreaseFrequency(PastTripManager pastTripManager, GeneralTrip lastTrip) {
        String tripRoute = lastTrip.getEnterStation() + "-" + lastTrip.getExitStation();
        String equivalentTripRoute = lastTrip.getExitStation() + "-" + lastTrip.getEnterStation();
        Integer decreasedFrequency = decreaseOneWayFrequency(pastTripManager, tripRoute);
        decreaseFrequencyBetweenStations(
                pastTripManager, tripRoute, equivalentTripRoute, decreasedFrequency);
    }

    private static Integer decreaseOneWayFrequency(PastTripManager pastTripManager, String tripRoute) {
        OneWayTripFrequency oneWayTripFrequency =
                oneWayTripFrequencyDao.get(pastTripManager.getPastTripManagerID(), tripRoute);
        Integer decreasedFrequency = oneWayTripFrequency.getTimes() - 1;
        //        tripFrequencyOneWay.put(tripRoute, decreasedFrequency);
        oneWayTripFrequency.setTimes(decreasedFrequency);
        oneWayTripFrequencyDao.update(oneWayTripFrequency);
        return decreasedFrequency;
    }

    private static void decreaseFrequencyBetweenStations(
            PastTripManager pastTripManager,
            String tripRoute,
            String equivalentTripRoute,
            Integer decreasedFrequency) {
        Integer newFrequency;
        OneWayTripFrequency oneWayTripFrequencyEquivalentRoute =
                oneWayTripFrequencyDao.get(pastTripManager.getPastTripManagerID(), equivalentTripRoute);
        if (oneWayTripFrequencyEquivalentRoute != null) {
            newFrequency = oneWayTripFrequencyEquivalentRoute.getTimes() + decreasedFrequency;
        } else {
            newFrequency = decreasedFrequency;
        }
        TripFrequencyBetweenStations tripFrequencyBetweenStations =
                tripFrequencyBetweenStationsDao.get(pastTripManager.getPastTripManagerID(), tripRoute);
        if (tripFrequencyBetweenStations != null) {
            tripFrequencyBetweenStations.setTimes(newFrequency);
            tripFrequencyBetweenStationsDao.update(tripFrequencyBetweenStations);
        } else {
            TripFrequencyBetweenStations newTripFrequencyBetweenStations =
                    new TripFrequencyBetweenStations();
            newTripFrequencyBetweenStations.setRouteName(equivalentTripRoute);
            newTripFrequencyBetweenStations.setPastTripManagerID(pastTripManager.getPastTripManagerID());
            newTripFrequencyBetweenStations.setTimes(newFrequency);
            tripFrequencyBetweenStationsDao.save(newTripFrequencyBetweenStations);
        }
    }

    public static void increaseFrequency(
            PastTripManager pastTripManager, GeneralTrip lastTrip, TripSegment tripSegment) {
        lastTrip.setExitStation(tripSegment.getExitStation());
        String tripRoute = lastTrip.getEnterStation() + "-" + lastTrip.getExitStation();
        String equivalentTripRoute = lastTrip.getExitStation() + "-" + lastTrip.getEnterStation();
        Integer increasedFrequency = increaseOneWayFrequency(pastTripManager, tripRoute);
        increasedFrequencyBetweenTwoStations(
                pastTripManager, tripRoute, equivalentTripRoute, increasedFrequency);
    }

    private static Integer increaseOneWayFrequency(PastTripManager pastTripManager, String tripRoute) {
        Integer increasedFrequency;
        OneWayTripFrequency oneWayTripFrequency =
                oneWayTripFrequencyDao.get(pastTripManager.getPastTripManagerID(), tripRoute);
        if (oneWayTripFrequency != null) {
            increasedFrequency = oneWayTripFrequency.getTimes() + 1;
            oneWayTripFrequency.setTimes(increasedFrequency);
            oneWayTripFrequencyDao.update(oneWayTripFrequency);
        } else {
            increasedFrequency = 1;
            OneWayTripFrequency newOneWayTripFrequency = new OneWayTripFrequency();
            newOneWayTripFrequency.setTimes(increasedFrequency);
            newOneWayTripFrequency.setRouteName(tripRoute);
            newOneWayTripFrequency.setPastTripManagerID(pastTripManager.getPastTripManagerID());
            oneWayTripFrequencyDao.save(newOneWayTripFrequency);
        }
        return increasedFrequency;
    }

    private static void increasedFrequencyBetweenTwoStations(
            PastTripManager pastTripManager,
            String tripRoute,
            String equivalentTripRoute,
            Integer increasedFrequency) {
        Integer newFrequency;

        OneWayTripFrequency oneWayTripFrequency =
                oneWayTripFrequencyDao.get(pastTripManager.getPastTripManagerID(), equivalentTripRoute);
        if (oneWayTripFrequency != null) {
            newFrequency = oneWayTripFrequency.getTimes() + increasedFrequency;
        } else {
            newFrequency = increasedFrequency;
        }

        TripFrequencyBetweenStations tripFrequencyBetweenStationsOne =
                tripFrequencyBetweenStationsDao.get(pastTripManager.getPastTripManagerID(), tripRoute);
        TripFrequencyBetweenStations tripFrequencyBetweenStationsTwo =
                tripFrequencyBetweenStationsDao.get(
                        pastTripManager.getPastTripManagerID(), equivalentTripRoute);
        if (tripFrequencyBetweenStationsOne == null && tripFrequencyBetweenStationsTwo == null) {
            TripFrequencyBetweenStations tripFrequencyBetweenStations =
                    new TripFrequencyBetweenStations();
            tripFrequencyBetweenStations.setTimes(newFrequency);
            tripFrequencyBetweenStations.setPastTripManagerID(pastTripManager.getPastTripManagerID());
            tripFrequencyBetweenStations.setRouteName(tripRoute);
            tripFrequencyBetweenStationsDao.save(tripFrequencyBetweenStations);
        } else if (tripFrequencyBetweenStationsOne != null) {
            tripFrequencyBetweenStationsOne.setTimes(newFrequency);
            tripFrequencyBetweenStationsDao.update(tripFrequencyBetweenStationsOne);
        } else {
            tripFrequencyBetweenStationsTwo.setTimes(newFrequency);
            tripFrequencyBetweenStationsDao.update(tripFrequencyBetweenStationsTwo);
        }
    }

    public PastTripManager createNewPastTripManager() {
        PastTripManager pastTripManager = new PastTripManager();
        return pastTripManagerDao.save(pastTripManager);
    }
}
