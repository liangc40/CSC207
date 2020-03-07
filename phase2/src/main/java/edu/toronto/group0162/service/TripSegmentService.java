package edu.toronto.group0162.service;


import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.TripSegment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TripSegmentService {

    @Getter
    private final TripSegmentDao tripSegmentDao;
//    private final GeneralTripDao generalTripDao;
    private final CardService cardService;
    private final StationService staionService;

    private final double BUS_FARE = 2;
    private final double SUBWAY_FARE = 0.5;
    private final double CAP = 6;

    /**
     * Inputting a tripSegmrnt and Record and update it and update card balance. and then return that tripSegment.
     *
     * @param tripSegment: a tripSegment.
     * @param card: a class called card.
     * @param transitLine: a transitLine.
     * @return a TripSegment.
     */
    public TripSegment updateEnter(TripSegment tripSegment, Card card, String transitLine){
        if(transitLine.contains("Bus")){
            //update tripSegment's cap
            double capUpdated = tripSegment.getCap();
            if(0.0<=capUpdated && capUpdated <=BUS_FARE){tripSegment.setCap(0);}
            if(capUpdated >BUS_FARE){
            tripSegment.setCap(capUpdated-BUS_FARE);}
        //update(reduce) user's card balance
        this.cardService.addBalance(card,-BUS_FARE);
        //update tripSegment tables' fare with tsid only
        tripSegment.setFare(BUS_FARE);
        this.tripSegmentDao.updateEnterBus(tripSegment);}
        return tripSegment;
    }

    /**
     * Inputting a tripSegment and card, update tripSegment, and update cardBalance, and then return the TripSegment.
     *
     * @param tripSegment: a tripSegment.
     * @param card: a class called card.
     * @param transitLine: a transitLine.
     * @return a TripSegment.
     */
    public TripSegment updateExit(TripSegment tripSegment, Card card, String transitLine){
        if(transitLine.contains("Bus")){
            this.tripSegmentDao.updateExitBus(tripSegment);}
        if(transitLine.contains("Subway")){
            double capUpdated = tripSegment.getCap();
            double expectedCharge = (this.staionService.calculatePoints(transitLine,tripSegment.getEntrance(),tripSegment.getExit()))*SUBWAY_FARE;
            //long tripDuration = this.timeService.getHourDifference(tripSegment.getEnterTime(),tripSegment.getExitTime());
            if(expectedCharge <= CAP){
                if(expectedCharge<=capUpdated){
                tripSegment.setFare(expectedCharge);
                tripSegment.setCap(capUpdated-expectedCharge);
                this.cardService.addBalance(card,-expectedCharge);}
                else{
                    tripSegment.setFare(capUpdated);
                    tripSegment.setCap(0);
                    this.cardService.addBalance(card,-capUpdated);
                }
            }
            if(expectedCharge >= CAP){
                tripSegment.setFare(CAP);
                tripSegment.setCap(0);
                this.cardService.addBalance(card,-CAP);
            }
            this.tripSegmentDao.updateExitSubway(tripSegment);
        }
        return tripSegment;
    }

    /**
     * Give a string representation of the daily revenue.
     *
     * @return a string representing the info about daily revenue.
     */
    public String showDailyRevenue(){
        Map<String, Double> dailyRevenueMap = new HashMap<String, Double>();
        String report ="";
        List<TripSegment> listTripSegments = this.tripSegmentDao.order();
        Iterator tripSegments = listTripSegments.iterator();
        String currentDate = ""; //date for current loop
        String newDate; //date for next tripSegment
        double currentDailyRevenue = 0;

        while(tripSegments.hasNext()){
            TripSegment tripSegmentGet = (TripSegment) tripSegments.next();
                newDate = tripSegmentGet.getExitTime().substring(0,10);
            if(currentDate.equals("") || currentDate.equals(newDate)){
                currentDate = newDate;
                currentDailyRevenue += tripSegmentGet.getFare();
                dailyRevenueMap.put(currentDate,currentDailyRevenue);
            }
            else{
                currentDate = newDate;
                currentDailyRevenue = 0;
                currentDailyRevenue += tripSegmentGet.getFare();
                dailyRevenueMap.put(currentDate,currentDailyRevenue);
            }
        }
        for(String day : dailyRevenueMap.keySet()){
            report += day+" revenue: "+dailyRevenueMap.get(day)+"\n";
        }
        return report;
    }

    /**
     * Give a string representation about the monthly revenue.
     *
     * @param user: A identifier of a admin user.
     * @return string representing about the monthly revenue.
     */
    public String showMonthlyRevenue(String user){
        Map<String, Double> monthlyRevenueMap = new HashMap<String, Double>();
        String report ="";
        List<TripSegment> listTripSegments = this.tripSegmentDao.order();
        Iterator tripSegments = listTripSegments.iterator();
        String currentMonth = ""; //month for current loop
        String newMonth; //month for next tripSegment
        double currentMonthlyRevenue = 0;

        while(tripSegments.hasNext()){
            TripSegment tripSegmentGet = (TripSegment) tripSegments.next();
            newMonth = tripSegmentGet.getExitTime().substring(0,7);
            if(currentMonth.equals("") || currentMonth.equals(newMonth)){
                currentMonth = newMonth;
                currentMonthlyRevenue += tripSegmentGet.getFare();
                monthlyRevenueMap.put(currentMonth,currentMonthlyRevenue);
            }
            else{
                currentMonth = newMonth;
                currentMonthlyRevenue = 0;
                currentMonthlyRevenue += tripSegmentGet.getFare();
                monthlyRevenueMap.put(currentMonth,currentMonthlyRevenue);
            }
        }
        for(String month : monthlyRevenueMap.keySet()){
            if(user.equals("Admin")){
                report += month+" revenue: "+monthlyRevenueMap.get(month)+"\n";
            }
        }
        return report;
    }

    /**
     * Give a string representation about the yearly revenue.
     *
     * @return string representing about the yearly revenue.
     */
    public String showYearlyRevenue(){
        Map<String, Double> yearlyRevenueMap = new HashMap<String, Double>();
        String report ="";
        List<TripSegment> listTripSegments = this.tripSegmentDao.order();
        Iterator tripSegments = listTripSegments.iterator();
        String currentYear = ""; //month for current loop
        String newYear; //month for next tripSegment
        double currentYearlyRevenue = 0;

        while(tripSegments.hasNext()){
            TripSegment tripSegmentGet = (TripSegment) tripSegments.next();
            newYear = tripSegmentGet.getExitTime().substring(0,4);
            if(currentYear.equals("") || currentYear.equals(newYear)){
                currentYear = newYear;
                currentYearlyRevenue += tripSegmentGet.getFare();
                yearlyRevenueMap.put(currentYear,currentYearlyRevenue);
            }
            else{
                currentYear = newYear;
                currentYearlyRevenue = 0;
                currentYearlyRevenue += tripSegmentGet.getFare();
                yearlyRevenueMap.put(currentYear,currentYearlyRevenue);
            }
        }
        for(String year : yearlyRevenueMap.keySet()){
            report += year+" revenue: "+yearlyRevenueMap.get(year)+"\n";
        }
        return report;
    }

    /**
     * string representation about the a monthly cost of specific user.
     *
     * @param uid: uid
     * @return string representing about the a monthly cost of specific user.
     */
    public String showUserMonthlyCost(int uid){
        Map<String, Double> monthlyRevenueMap = new HashMap<String, Double>();
        String report ="";

        List<TripSegment> listTripSegments = this.tripSegmentDao.order(uid);
        if(listTripSegments.size() == 0){return "No trips stored and Zero Cost now";}
        else{
        Iterator tripSegments = listTripSegments.iterator();
        String currentMonth = ""; //month for current loop
        String newMonth; //month for next tripSegment
        double currentMonthlyRevenue = 0;

        while(tripSegments.hasNext()){
            TripSegment tripSegmentGet = (TripSegment) tripSegments.next();
            newMonth = tripSegmentGet.getExitTime().substring(0,7);
            if(currentMonth.equals("") || currentMonth.equals(newMonth)){
                currentMonth = newMonth;
                currentMonthlyRevenue += tripSegmentGet.getFare();
                monthlyRevenueMap.put(currentMonth,currentMonthlyRevenue);
            }
            else{
                currentMonth = newMonth;
                currentMonthlyRevenue = 0;
                currentMonthlyRevenue += tripSegmentGet.getFare();
                monthlyRevenueMap.put(currentMonth,currentMonthlyRevenue);
            }
        }
        double userTotalCost = 0;
        for(String month : monthlyRevenueMap.keySet()){
            report += month+" total cost: "+monthlyRevenueMap.get(month)+"\n";
            userTotalCost += monthlyRevenueMap.get(month);
        }
report += "\n"+"Average Monthly Transit Cost: "+userTotalCost/monthlyRevenueMap.size();
        return report;
    }}





}
