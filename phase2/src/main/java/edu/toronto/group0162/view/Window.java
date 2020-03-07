package edu.toronto.group0162.view;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;

import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.service.*;

/**
 * Represents LogIn and SignUp page.
 *
 */
public class Window extends JFrame {

  private final JButton btnLogin = new JButton("Log In");

  private final JButton btnRegister = new JButton("Sign Up");

  private final JButton btnExit = new JButton("Exit");

  private UserService userService;

  private CardService cardService;

  private StationService stationService;

  private TripSegmentService tripSegmentService;

  private TimeService timeService;
  private GeneralTripService generalTripService;
  private ArchiveService archiveService;

  private UserDao userDao;

  private CardDao cardDao;

  private NodeDao nodeDao;

  private EdgeDao edgeDao;

  private StationDao stationDao;

  private TripSegmentDao tripSegmentDao;

  private GeneralTripDao generalTripDao;


  /**
   * initialize dao and service from JDBC PostgreSQL connection
   * @param connection
   */
  private void initializeDependency(final Connection connection) {
    this.userDao = new UserDao(connection);
    this.cardDao = new CardDao(connection);
    this.nodeDao = new NodeDao(connection);
    this.edgeDao = new EdgeDao(connection);
    this.stationDao = new StationDao(connection);
    this.tripSegmentDao = new TripSegmentDao(connection);
    this.generalTripDao = new GeneralTripDao(connection);
    this.userService = new UserService(this.userDao);
    this.cardService = new CardService(this.cardDao,this.userDao);
    this.timeService = new TimeService();
    this.stationService = new StationService(this.stationDao);
    this.archiveService = new ArchiveService(generalTripDao,tripSegmentDao);
    this.generalTripService = new GeneralTripService(this.generalTripDao,this.tripSegmentService,this.tripSegmentDao);
    this.tripSegmentService = new TripSegmentService(this.tripSegmentDao, this.cardService, this.stationService);
  }


  /**
   * initialize LogIn Dialogue
   *
   */
  private void initializeComponent() {
    btnLogin.addActionListener((ActionEvent e) -> {
      edu.toronto.group0162.view.LoginDialog loginDlg =
              new edu.toronto.group0162.view.LoginDialog
                      (this,this.userService,this.cardService,this.tripSegmentService,this.generalTripService,this.timeService,
                              this.stationService,this.archiveService,this.userDao, this.nodeDao,this.edgeDao,this.stationDao,this.tripSegmentDao, this.generalTripDao);
      loginDlg.setWindow(this);
      this.setVisible(false);
      loginDlg.setVisible(true);
    });

    /**
     * initialize SignUp Dialogue
     *
     */
    btnRegister.addActionListener((ActionEvent e) -> {
      edu.toronto.group0162.view.SignUpDialog signUpDialog = new edu.toronto.group0162.view.SignUpDialog(this, this.userService);
      signUpDialog.setVisible(true);

    });

    /**
     * Set up Exit the system
     *
     */
    btnExit.addActionListener((ActionEvent e) -> {
      System.exit(0);
    });

    //set up Window page size
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    super.setSize(300, 150);
    super.setLayout(new FlowLayout());
    super.getContentPane().add(btnLogin);
    super.getContentPane().add(btnRegister);
    super.getContentPane().add(btnExit);
  }

  /**
   * Set up JDBC connection to the Window page
   *
   */
  public Window(final Connection connection) throws HeadlessException {
    super("Transit System");
    this.initializeDependency(connection);
    this.initializeComponent();
  }

}
