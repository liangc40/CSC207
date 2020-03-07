package edu.toronto.group0162.view;

import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.User;
import edu.toronto.group0162.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Represents a user main page.
 * A user can open card management page, trip page, personal info page from it.
 */
@Slf4j
public class UserFrame extends JFrame {

  private JButton btnCardMgt;
  private JButton btnTripManagement;
  private JButton btnPersonalInfo;
  private JButton btnAdmin;
  private JButton btnLogOut;
  private Window window;

  private UserService userService;
  private CardService cardService;
  private TripSegmentService tripSegmentService;
  private TimeService timeService;
  private GeneralTripService generalTripService;
  private StationService stationService;
  private ArchiveService archiveService;
  private UserDao userDao;
  private NodeDao nodeDao;
  private EdgeDao edgeDao;
  private StationDao stationDao;
  private TripSegmentDao tripSegmentDao;
  private GeneralTripDao generalTripDao;

  @Getter @Setter private int currentLogInUid;

  CityFrame cityFrame = new CityFrame("City Selection");
  CardMgtFrame cardMgtFrame;
  AdminFrame adminFrame;
  PersonalInfoFrame personalInfoFrame;

  private JTextField t;


  /**
   * Sets up user main page.
   *
   */
  public UserFrame(String title) {

    super(title); // invoke the JFrame constructor
    setSize(1000, 250);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setLayout(new FlowLayout()); // set the layout manager

    btnCardMgt = new JButton("Card Management");
    btnTripManagement = new JButton("Trip Management");
    btnPersonalInfo = new JButton("Personal Info");
    btnAdmin = new JButton("Admin");
    btnLogOut = new JButton("Back");
    t = new JTextField(15);

    add(btnCardMgt);
    add(btnTripManagement);
    add(btnPersonalInfo);
    add(btnAdmin);
    add(btnLogOut);
    add(t);

    btnCardMgt.addActionListener((ActionEvent e) -> this.onClickCardMgt(e));
    btnLogOut.addActionListener((ActionEvent e) -> this.onClickLogOut(e));
    btnTripManagement.addActionListener((ActionEvent e) -> this.onClickTrip(e));
    btnAdmin.addActionListener((ActionEvent e) -> this.onClickAdmin(e));
    btnPersonalInfo.addActionListener((ActionEvent e) -> this.onClickPersonalInfo(e));
  }

  /**
   * Adds action listener to button of Personal Info.
   *
   */
  private void onClickPersonalInfo(ActionEvent e) {
    this.setVisible(false);
    this.personalInfoFrame= new PersonalInfoFrame("Personal Information");
    this.personalInfoFrame.setVisible(true);
    this.personalInfoFrame.setUserFrame(this);
    this.personalInfoFrame.setUserService(this.userService);
    this.personalInfoFrame.setCurrentLogInUid(this.currentLogInUid);
    User registeredUser = this.userService.getUserDao().get(this.currentLogInUid);

    String email = registeredUser.getEmail();
    Date date = new Date(registeredUser.getCreateAt()*1000);
    String password  = registeredUser.getPassword();
    String name = registeredUser.getName();

    this.personalInfoFrame.personalInfo.setText("Email: "
            +email+", User registered at: "+date
    );
    this.personalInfoFrame.personalInfo.setEditable(false);
    this.personalInfoFrame.name.setText(name);
    this.personalInfoFrame.name.setEditable(false);
    this.personalInfoFrame.password.setText(password);
    this.personalInfoFrame.password.setEditable(false);

  }

  /**
   * Adds action listener to button of Admin Page.
   *
   */
  private void onClickAdmin(ActionEvent e) {
    if(this.userService.getUserDao().get(currentLogInUid).isAdmin() == true){
    this.adminFrame = new AdminFrame();
    this.adminFrame.setUserFrame(this);
    this.setVisible(false);
    this.adminFrame.frame.setVisible(true);
    this.adminFrame.setCurrentLogInUid(this.currentLogInUid);
    this.adminFrame.setDaos(nodeDao, edgeDao, stationDao, tripSegmentDao, generalTripDao,userDao);
    this.adminFrame.setServices(
        cardService,
        tripSegmentService,
        generalTripService,
        timeService,
        stationService,
        archiveService);}

        else {JOptionPane.showMessageDialog(UserFrame.this,
            "You are not authorized to open admin page",
            "Admin Denied",
            JOptionPane.ERROR_MESSAGE);}
  }

  public void setWindow(Window window) {
    this.window = window;
  }

  /**
   * Adds action listener to button of Card Management Page.
   *
   */
  private void onClickCardMgt(ActionEvent e) {

    this.cardMgtFrame = new CardMgtFrame();

    this.cardMgtFrame.setUserFrame(this);

    this.setVisible(false);

    this.cardMgtFrame.frame.setVisible(true);

    this.cardMgtFrame.setCurrentLogInUid(this.currentLogInUid);

    this.cardMgtFrame.setCardService(this.cardService);

    this.cardMgtFrame.setUserDao(this.userDao);

    this.cardMgtFrame.costInfo.setText(this.tripSegmentService.showUserMonthlyCost(currentLogInUid));
    if (null == this.cardMgtFrame.cardNumBox) {
      List<Card> listCards = this.cardService.getCardDao().getByUid(this.currentLogInUid);
      Iterator cardsList = listCards.iterator();
      while (cardsList.hasNext()) {
        Card cardGet = (Card) cardsList.next();
        this.cardMgtFrame.cardNumBox.addItem(cardGet.getCid());
      }
    } else {
      this.cardMgtFrame.cardNumBox.removeAllItems();
      List<Card> listCards = this.cardService.getCardDao().getByUid(this.currentLogInUid);
      Iterator cardsList = listCards.iterator();
      while (cardsList.hasNext()) {
        Card cardGet = (Card) cardsList.next();
        this.cardMgtFrame.cardNumBox.addItem(cardGet.getCid());
      }
    }
  }

  /**
   * Adds action listener to button of Click Out.
   *
   */
  private void onClickLogOut(ActionEvent e) {
    this.setVisible(false);
    this.window.setVisible(true);
    log.info("User: "+this.userDao.get(currentLogInUid).getName()+" logs out the system");
  }

  /**
   * Adds action listener to button of Trip Page.
   *
   */
  private void onClickTrip(ActionEvent e) {
    this.setVisible(false);
    this.cityFrame.setVisible(true);
    this.cityFrame.setCurrentLogInUid(this.currentLogInUid);
    this.cityFrame.setDaos(userDao, nodeDao, edgeDao, stationDao, tripSegmentDao, generalTripDao);
    this.cityFrame.setUserFrame(this);
    this.cityFrame.setCurrentLogInUid(this.currentLogInUid);
    this.cityFrame.setServices(
        cardService, tripSegmentService, generalTripService, timeService, stationService);
  }

  /**
   * Sets JTextField of username show.
   *
   *@param username
   */
  public void setTextField(String username) {
    this.t.setText(username);
  }

  /**
   * Injects Service classes from JDBC PostgreSQL connection.
   *
   */
  public void setServices (
      UserService userService,
      CardService cardService,
      TripSegmentService tripSegmentService,
      GeneralTripService generalTripService,
      TimeService timeService,
      StationService stationService,
      ArchiveService archiveService) {
    this.userService = userService;
    this.cardService = cardService;
    this.tripSegmentService = tripSegmentService;
    this.generalTripService = generalTripService;
    this.timeService = timeService;
    this.stationService = stationService;
    this.archiveService = archiveService;
  }

  /**
   * Injects Dao classes from JDBC PostgreSQL connection.
   *
   */
  public void setDaos (
          UserDao userDao,
      NodeDao nodeDao,
      EdgeDao edgeDao,
      StationDao stationDao,
      TripSegmentDao tripSegmentDao,
      GeneralTripDao generalTripDao) {
    this.userDao = userDao;
    this.nodeDao = nodeDao;
    this.edgeDao = edgeDao;
    this.stationDao = stationDao;
    this.tripSegmentDao = tripSegmentDao;
    this.generalTripDao = generalTripDao;
  }
}
