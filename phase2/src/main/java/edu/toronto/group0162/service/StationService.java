package edu.toronto.group0162.service;

import edu.toronto.group0162.dao.CardDao;
import edu.toronto.group0162.dao.StationDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.lang.Math.abs;

@AllArgsConstructor
public class StationService {

    @Getter
    private final StationDao stationDao;

    public int calculatePoints(String transitName, String start, String stop){
    System.out.println(
            this.stationDao.getStation(transitName,start).getPoint()
       );
        return abs(this.stationDao.getStation(transitName,start).getPoint() - this.stationDao.getStation(transitName,stop).getPoint());
    }
}
