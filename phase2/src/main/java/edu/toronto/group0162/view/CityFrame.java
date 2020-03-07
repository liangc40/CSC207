package edu.toronto.group0162.view;

import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.entity.Edge;
import edu.toronto.group0162.entity.Node;
import edu.toronto.group0162.entity.User;
import edu.toronto.group0162.service.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import java.util.List;


/**
 * Represents a trip function selection page.
 * A user can open city frame page to take transit, look map searching shortest distance, view most recent trips.
 *
 */
public class CityFrame extends JFrame {

    @Getter
    @Setter
    private UserDao userDao;
    private NodeDao nodeDao;
    private EdgeDao edgeDao;
    private StationDao stationDao;
    private TripSegmentDao tripSegmentDao;
    private GeneralTripDao generalTripDao;

    private JButton btnToronto;
    private JButton btnBeginTrip;
    private JButton btnBack;
    private JButton btnTrack;
    MapFrame mapFrame = new MapFrame("Toronto Transit Map");
    RouteFrame routeFrame = new RouteFrame("Route Selection");

    @Getter
    @Setter
    private int currentLogInUid;

    private UserFrame userFrame;
    private CardService cardService;
    private TripSegmentService tripSegmentService;
    private TimeService timeService;
    private GeneralTripService generalTripService;
    private StationService stationService;

    JTextArea tripInfo;

    /**
     * Sets up city frame page.
     *
     */
    public CityFrame(String title) {

        super( title );                      // invoke the JFrame constructor
        setSize( 1400, 600 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        setLayout( new FlowLayout() );       // set the layout manager

        btnToronto = new JButton ("Toronto Map");
        btnBeginTrip = new JButton ("Begin Trip");
        btnBack = new JButton("Back");
        btnTrack = new JButton("View Recent Trips");

        tripInfo = new JTextArea(25, 95);
        tripInfo.setLineWrap(true);
        tripInfo.setVisible(true);
        tripInfo.setEditable(false);

        add (btnToronto);
        add (btnBeginTrip);
        add(btnTrack);
        add(btnBack);
        add(tripInfo);

        btnToronto.addActionListener((ActionEvent e) -> this.onClickToronto(e));
        btnBeginTrip.addActionListener((ActionEvent e) -> this.onClickBeginTrip(e));
        btnBack.addActionListener((ActionEvent e) -> this.onClickBack(e));
        btnTrack.addActionListener((ActionEvent e) -> this.onClickTrack(e));
    }

    /**
     * Adds action listener to button of Tracking.
     *
     */
    private void onClickTrack(ActionEvent e) {
        tripInfo.setText(this.generalTripService.sort(this.currentLogInUid));
    }

    /**
     * Adds action listener to button of back.
     *
     */
    private void onClickBack(ActionEvent e) {
        this.setVisible(false);
        this.userFrame.setVisible(true);
    }

    /**
     * Adds action listener to button of checking Toronto Map.
     *
     */
    private void onClickToronto(ActionEvent e) {
        this.setVisible(false);
        this.mapFrame.setVisible(true);
        this.mapFrame.setTitle("Toronto Transit Map");
        this.mapFrame.setCurrentLogInUid(this.currentLogInUid);

        List<Node> listNodes = this.nodeDao.getNodes();
        List<Edge> listEdges = this.edgeDao.getEdges();

        Iterator nodesList = listNodes.iterator();
        while(nodesList.hasNext()) {
            Node nodeGet = (Node) nodesList.next();
            this.mapFrame.entranceBox.addItem(nodeGet.getName());
            this.mapFrame.exitBox.addItem(nodeGet.getName());
        }

        Graph torontoGraph = new Graph(listNodes, listEdges);
        int[][] path = new int[torontoGraph.vertexArray.length][torontoGraph.vertexArray.length];
        double[][] floy = new double[torontoGraph.vertexArray.length][torontoGraph.vertexArray.length];

        torontoGraph.floyd(path, floy);

        this.mapFrame.setTransitGraph(torontoGraph);
        this.mapFrame.setAlgorithm(path, floy, listNodes);
        this.mapFrame.setCityFrame(this);

    }

    /**
     * Adds action listener to button of begining.
     *
     */
    private void onClickBeginTrip(ActionEvent e) {
        this.setVisible(false);
        this.routeFrame.setVisible(true);
        this.routeFrame.setTitle("Route Selection");
        this.routeFrame.setDaos(userDao, stationDao, tripSegmentDao, generalTripDao);
        this.routeFrame.setCityFrame(this);
        this.routeFrame.setCurrentLogInUid(this.currentLogInUid);
        this.routeFrame.setServices(cardService, tripSegmentService,generalTripService,timeService,stationService);
    }
    /**
     * Sets up Dao classes injection from postgreSQL JDBC connection
     *
     */
    public void setDaos(UserDao userDao, NodeDao nodeDao, EdgeDao edgeDao, StationDao stationDao,
                        TripSegmentDao tripSegmentDao, GeneralTripDao generalTripDao){
        this.userDao = userDao;
        this.nodeDao = nodeDao;
        this.edgeDao = edgeDao;
        this.stationDao = stationDao;
        this.tripSegmentDao = tripSegmentDao;
        this.generalTripDao = generalTripDao;
    }

    public void setUserFrame(UserFrame userFrame){
        this.userFrame = userFrame;
    }

    /**
     * Sets up Service classes injection from postgreSQL JDBC connection
     *
     */
    public void setServices(CardService cardService, TripSegmentService tripSegmentService, GeneralTripService generalTripService, TimeService timeService,StationService stationService){
        this.cardService = cardService;
        this.tripSegmentService = tripSegmentService;
        this.generalTripService = generalTripService;
        this.timeService = timeService;
    this.stationService = stationService;}

}
