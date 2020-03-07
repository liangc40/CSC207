package edu.toronto.group0162.view;

import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;

@Slf4j
/**
 * Represents an admin page.
 * An admin can check daily,monthly,yearly revenue and archive old data
 *
 */
public class AdminFrame {


    @Getter
    @Setter
    private int currentLogInUid;

    @Getter
    @Setter
    private UserDao userDao;
    private NodeDao nodeDao;
    private EdgeDao edgeDao;
    private StationDao stationDao;
    private TripSegmentDao tripSegmentDao;
    private GeneralTripDao generalTripDao;

    private JButton btnDailyRevenue;
    private JButton btnMonthlyRevenue;
    private JButton btnYearlyRevenue;
    private JButton btnArchive;
    private JButton btnBack;


    JFrame frame;
    JTextArea statisticsInfo;

    private UserFrame userFrame;
    private CardService cardService;
    private TripSegmentService tripSegmentService;
    private ArchiveService archiveService;
    private TimeService timeService;
    private GeneralTripService generalTripService;
    private StationService stationService;

    /**
     * Sets up admin frame page.
     *
     */
    public AdminFrame() {
        frame = new JFrame("Admin Page");

        frame.setSize( 1400, 1000);

        frame.setLayout( new FlowLayout(FlowLayout.LEFT,20,40) );       // set the layout manager

        btnDailyRevenue = new JButton ("DailyRevenue");
        btnMonthlyRevenue = new JButton("MonthlyRevenue");
        btnYearlyRevenue = new JButton("YearlyRevenue");
        btnArchive = new JButton("Archive");
        btnBack = new JButton("Back");
        statisticsInfo = new JTextArea(25, 95);
        statisticsInfo.setLineWrap(true);
        statisticsInfo.setVisible(true);
        statisticsInfo.setEditable(false);
        frame.add(btnDailyRevenue);
        frame.add(btnMonthlyRevenue);
        frame.add(btnYearlyRevenue);
        frame.add(btnArchive);
        frame.add(btnBack);
        frame.add(statisticsInfo);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        btnDailyRevenue.addActionListener((ActionEvent e) -> this.onClickDaily(e));
        btnMonthlyRevenue.addActionListener((ActionEvent e) -> this.onClickMonthly(e));
        btnYearlyRevenue.addActionListener((ActionEvent e) -> this.onClickYearly(e));
        btnBack.addActionListener((ActionEvent e) -> this.onClickBack(e));
        btnArchive.addActionListener((ActionEvent e) -> this.onClickArchive(e));

    }

    /**
     * Adds action listener to button of Archive.
     *
     */
    private void onClickArchive(ActionEvent e) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
        this.archiveService.archiveTripSegments(year);
            this.archiveService.archiveGeneralTrips(year);
            log.info("Admin User "+this.userDao.get(currentLogInUid).getName()+" archives and deletes data later than 2018");

    }
    /**
     * Adds action listener to button of Back.
     *
     */
    private void onClickBack(ActionEvent e) {
        this.frame.setVisible(false);
        this.userFrame.setVisible(true);
    }
    /**
     * Adds action listener to button of Clicking Daily Report.
     *
     */
    private void onClickDaily(ActionEvent e) {
        statisticsInfo.setText(this.tripSegmentService.showDailyRevenue());
        log.info("Admin User "+this.userDao.get(currentLogInUid).getName()+" check daily revenue");
    }
    /**
     * Adds action listener to button of Clicking Monthly Report.
     *
     */
    private void onClickMonthly(ActionEvent e) {
        statisticsInfo.setText(this.tripSegmentService.showMonthlyRevenue("Admin"));
        log.info("Admin User "+this.userDao.get(currentLogInUid).getName()+" check monthly revenue");
    }
    /**
     * Adds action listener to button of Clicking Yearly Report.
     *
     */
    private void onClickYearly(ActionEvent e) {
        statisticsInfo.setText(this.tripSegmentService.showYearlyRevenue());
        log.info("Admin User "+this.userDao.get(currentLogInUid).getName()+" check yearly revenue");
    }

    public void setUserFrame(UserFrame userFrame)
    {
        this.userFrame = userFrame;
    }

    /**
     * Set Dao classes injection from postgreSQL JDBC connection
     *
     */
    public void setDaos(NodeDao nodeDao, EdgeDao edgeDao, StationDao stationDao,
                        TripSegmentDao tripSegmentDao, GeneralTripDao generalTripDao,UserDao userDao){
        this.nodeDao = nodeDao;
        this.edgeDao = edgeDao;
        this.stationDao = stationDao;
        this.tripSegmentDao = tripSegmentDao;
        this.generalTripDao = generalTripDao;
        this.userDao = userDao;
    }

    /**
     * Set Service classes injection from postgreSQL JDBC connection
     *
     */
    public void setServices(CardService cardService, TripSegmentService tripSegmentService,
                            GeneralTripService generalTripService, TimeService timeService,
                            StationService stationService, ArchiveService archiveService){
        this.cardService = cardService;
        this.tripSegmentService = tripSegmentService;
        this.generalTripService = generalTripService;
        this.timeService = timeService;
        this.stationService = stationService;
    this.archiveService = archiveService;}
}
