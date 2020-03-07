package edu.toronto.group0162.service;

import edu.toronto.group0162.dao.CardDao;
import edu.toronto.group0162.dao.GeneralTripDao;
import edu.toronto.group0162.dao.TripSegmentDao;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.GeneralTrip;
import edu.toronto.group0162.entity.TripSegment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
public class GeneralTripService {

    @Getter
    private final GeneralTripDao generalTripDao;
    private final TripSegmentService tripSegmentService;
    private final TripSegmentDao tripSegmentDao;

    //唯一uid
    public String sort(Integer uid){

        String trackInfo ="";

        List<GeneralTrip> listGeneralTrips = this.generalTripDao.getLatest(uid);
        Iterator generalTripsList = listGeneralTrips.iterator();

        int generalTripCount = 1;
        while(generalTripsList.hasNext()) {
            GeneralTrip generalTripGet = (GeneralTrip) generalTripsList.next();
            int gtid = generalTripGet.getGtid();

            List<TripSegment> listTripSegments = this.tripSegmentDao.getTripSegments(uid,gtid);
            Iterator recentTripSegments = listTripSegments.iterator();

            int whileCount = 0;
            while(recentTripSegments.hasNext()){
                if(whileCount == 0)
                {trackInfo += "Trip "+generalTripCount+": "+"\n";}
                TripSegment tripSegmentGet = (TripSegment) recentTripSegments.next();
                int cardID = tripSegmentGet.getCid();
                String entrance = tripSegmentGet.getEntrance();
                String exit = tripSegmentGet.getExit();
                trackInfo += tripSegmentGet.toString(cardID,entrance,exit);
            }
            generalTripCount += 1;
        }
        return trackInfo;
    }


}
