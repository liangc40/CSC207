package manager;

import dao.PastTripManagerDao;
import lombok.Getter;
import lombok.Data;
import entity.GeneralTrip;
import dao.GeneralTripDao;
import service.PastTripManagerService;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

@Data
public class PastTripManager implements Observer {
    private int pastTripManagerID;
    private ArrayList<Integer> pastTripIDs = new ArrayList<>();

    @Getter private GeneralTripDao generalTripDao;
    private PastTripManagerService pastTripManagerService;
    private PastTripManagerDao pastTripManagerDao;


    // LC.....................
    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("add new trip")) {
            GeneralTrip newTrip = ((TripInfoNotifier) o).getNewTrip();
            generalTripDao.update(newTrip);
            pastTripIDs.add(newTrip.getGeneralTripID());
            pastTripManagerDao.update(this);
        } else if (arg.equals("update trip's destination")) {
            ArrayList<Integer> pastTripIDs = pastTripManagerDao.get(pastTripManagerID).getPastTripIDs();
            GeneralTrip lastTrip = generalTripDao.get(pastTripIDs.get(pastTripIDs.size()-1));
            if (lastTrip.getExitStation() != null) {
                PastTripManagerService.decreaseFrequency(this, lastTrip);
            }
            PastTripManagerService.increaseFrequency(this, lastTrip, ((TripInfoNotifier) o).getTripSegment());
        }
    }
}
