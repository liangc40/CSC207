package edu.toronto.group0162.view;

import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.Node;
import edu.toronto.group0162.entity.Station;
import edu.toronto.group0162.service.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Represents a transit route selection page.
 * A user can choose Subway Line 1, Subway Line 2, Bus Line 1, Bus Line 2.
 */
public class RouteFrame extends JFrame {

    private CityFrame cityFrame;

    private CardService cardService;
    private TripSegmentService tripSegmentService;
    private GeneralTripService generalTripService;
    private TimeService timeService;
    private StationService stationService;

    @Getter
    @Setter
    private StationDao stationDao;
    @Getter
    @Setter
    private TripSegmentDao tripSegmentDao;
    @Getter
    @Setter
    private GeneralTripDao generalTripDao;
    @Getter
    @Setter
    private UserDao userDao;

    private JButton btnSubway1;
    private JButton btnSubway2;
    private JButton btnBus1;
    private JButton btnBus2;
    private JButton btnBack;

    TripFrame tripFrame;
    @Getter
    @Setter
    private int currentLogInUid;
    /** Initialize Route Frame */
    public RouteFrame(String title) {

        super( title );                      // invoke the JFrame constructor
        setSize( 800, 250 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        setLayout( new FlowLayout() );       // set the layout manager

        btnSubway1 = new JButton ("Subway Line 1");
        btnSubway2 = new JButton ("Subway Line 2");
        btnBus1 = new JButton ("Bus Line 1");
        btnBus2 = new JButton("Bus Line 2");
        btnBack = new JButton("Back");

        add (btnSubway1);
        add (btnSubway2);
        add(btnBus1);
        add(btnBus2);
        add(btnBack);
        
        btnSubway1.addActionListener((ActionEvent e) -> this.onClick1(e));
        btnSubway2.addActionListener((ActionEvent e) -> this.onClick2(e));
        btnBus1.addActionListener((ActionEvent e) -> this.onClick3(e));
        btnBus2.addActionListener((ActionEvent e) -> this.onClick4(e));
        btnBack.addActionListener((ActionEvent e) -> this.onClickBack(e));
    }

    /**
     * Adds action listener to button of Back.
     *
     */
    private void onClickBack(ActionEvent e) {

        this.setVisible(false);
        this.cityFrame.setVisible(true);
    }
    /**
     * Adds action listener to button of clicking Subway Line 1.
     *
     */
    private void onClick1(ActionEvent e) {

        setFrame();

        if(null == this.tripFrame.boxEntrance && null == this.tripFrame.boxExit){setStationInfo("Subway Line 1");}
        this.tripFrame.boxEntrance.removeAllItems();
        this.tripFrame.boxExit.removeAllItems();
        setStationInfo("Subway Line 1");

        if(null == this.tripFrame.boxCard){setCardInfo();}
        this.tripFrame.boxCard.removeAllItems();
        setCardInfo();
    }

    /**
     * Adds action listener to button of clicking Subway Line 2.
     *
     */
    private void onClick2(ActionEvent e) {
        setFrame();
        if(null == this.tripFrame.boxEntrance && null == this.tripFrame.boxExit){setStationInfo("Subway Line 2");}
        this.tripFrame.boxEntrance.removeAllItems();
        this.tripFrame.boxExit.removeAllItems();
        setStationInfo("Subway Line 2");

        if(null == this.tripFrame.boxCard){setCardInfo();}
        this.tripFrame.boxCard.removeAllItems();
        setCardInfo();
    }

    /**
     * Adds action listener to button of clicking Bus Line 1.
     *
     */
    private void onClick3(ActionEvent e) {
        setFrame();

        if(null == this.tripFrame.boxEntrance && null == this.tripFrame.boxExit){setStationInfo("Bus Line 1");}
        this.tripFrame.boxEntrance.removeAllItems();
        this.tripFrame.boxExit.removeAllItems();
        setStationInfo("Bus Line 1");

        if(null == this.tripFrame.boxCard){setCardInfo();}
        this.tripFrame.boxCard.removeAllItems();
        setCardInfo();
    }

    /**
     * Adds action listener to button of clicking Bus Line 2.
     *
     */
    private void onClick4(ActionEvent e) {
        setFrame();

        if(null == this.tripFrame.boxEntrance && null == this.tripFrame.boxExit){setStationInfo("Bus Line 2");}
            this.tripFrame.boxEntrance.removeAllItems();
        this.tripFrame.boxExit.removeAllItems();
        setStationInfo("Bus Line 2");

            if(null == this.tripFrame.boxCard){setCardInfo();}
            this.tripFrame.boxCard.removeAllItems();
            setCardInfo();


    }
    /**
     * Sets Dao classes injection from postgresSQL JDBC connection.
     *
     */
    public void setDaos(UserDao userDao,StationDao stationDao, TripSegmentDao tripSegmentDao, GeneralTripDao generalTripDao){
        this.userDao = userDao;
        this.stationDao = stationDao;
        this.tripSegmentDao = tripSegmentDao;
        this.generalTripDao = generalTripDao;

    }
    /**
     * Sets CityFrame injection.
     *
     */
    public void setCityFrame(CityFrame cityFrame){
        this.cityFrame = cityFrame;
    }
    /**
     * Sets Service classes injection from postgresSQL JDBC connection.
     *
     */
    public void setServices(CardService cardService, TripSegmentService tripSegmentService, GeneralTripService generalTripService, TimeService timeService,StationService stationService){
        this.cardService = cardService;
        this.tripSegmentService = tripSegmentService;
        this.generalTripService = generalTripService;
        this.timeService = timeService;
    this.stationService = stationService;}
    /**
     * Sets up RouteFrame helper to initialize
     *
     */
    public void setFrame(){
        this.setVisible(false);
        this.tripFrame= new TripFrame("Trip");
        this.tripFrame.setVisible(true);
        this.tripFrame.setRouteFrame(this);
        this.tripFrame.setDaos(userDao,stationDao,tripSegmentDao,generalTripDao);
        this.tripFrame.setServices(cardService, tripSegmentService,generalTripService,timeService,stationService);
        this.tripFrame.setCurrentLogInUid(this.currentLogInUid);
    }

    /**
     * Sets up Card information initialization
     *
     */
    public void setCardInfo(){
        this.tripFrame.setCurrentLogInUid(this.currentLogInUid);
        List<Card> listCards = this.cardService.getCardDao().getByUid(this.currentLogInUid);
        Iterator cardsList = listCards.iterator();
        while(cardsList.hasNext()) {
            Card cardGet = (Card) cardsList.next();
            this.tripFrame.boxCard.addItem(cardGet.getCid());
        }
    }
    /**
     * Sets up Transit Stations information initialization
     *
     */
    public void setStationInfo(String transitName){

        this.tripFrame.setTitle(transitName);
        this.tripFrame.setTransitLine(transitName);

        List<Station> listStations = this.stationDao.getStations(transitName);

        Iterator stationsList = listStations.iterator();
        while(stationsList.hasNext()) {
            Station staionGet = (Station) stationsList.next();
            this.tripFrame.boxEntrance.addItem(staionGet.getName());
            this.tripFrame.boxExit.addItem(staionGet.getName());
        }
    }

}
