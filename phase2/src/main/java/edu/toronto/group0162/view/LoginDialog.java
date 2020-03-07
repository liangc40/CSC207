package edu.toronto.group0162.view;

import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.entity.GeneralTrip;
import edu.toronto.group0162.entity.User;
import edu.toronto.group0162.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/** Represents a Log In Dialogue . A user can register name, password and email on it. */
@Slf4j
public class LoginDialog extends JDialog {

  @Getter @Setter private int currentLogInUid;

  private final UserService userService;
  private final CardService cardService;
  private final TripSegmentService tripSegmentService;
  private final GeneralTripService generalTripService;
  private final TimeService timeService;
  private final StationService stationService;
  private ArchiveService archiveService;
  private final UserDao userDao;
  private final NodeDao nodeDao;
  private final EdgeDao edgeDao;
  private final StationDao stationDao;
  private TripSegmentDao tripSegmentDao;
  private GeneralTripDao generalTripDao;

  private JTextField tfEmail;
  private JPasswordField pfPassword;
  private JLabel lbEmail;
  private JLabel lbPassword;
  private JButton btnLogin;
  private JButton btnBack;

  UserFrame userFrame;

  private Window window;

  /** Initialize LogIn Dialogue */
  public LoginDialog(
      Frame parent,
      UserService userService,
      CardService cardService,
      TripSegmentService tripSegmentService,
      GeneralTripService generalTripService,
      TimeService timeService,
      StationService stationService,
      ArchiveService archiveService,
      UserDao userDao,
      NodeDao nodeDao,
      EdgeDao edgeDao,
      StationDao stationDao,
      TripSegmentDao tripSegmentDao,
      GeneralTripDao generalTripDao) {

    super(parent, "Login", true);
    this.userService = userService;
    this.cardService = cardService;
    this.tripSegmentService = tripSegmentService;
    this.generalTripService = generalTripService;
    this.timeService = timeService;
    this.stationService = stationService;
    this.archiveService = archiveService;
    this.userDao = userDao;
    this.nodeDao = nodeDao;
    this.edgeDao = edgeDao;
    this.stationDao = stationDao;
    this.tripSegmentDao = tripSegmentDao;
    this.generalTripDao = generalTripDao;

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints cs = new GridBagConstraints();

    cs.fill = GridBagConstraints.HORIZONTAL;

    lbEmail = new JLabel("Email: ");
    cs.gridx = 0;
    cs.gridy = 0;
    cs.gridwidth = 1;
    panel.add(lbEmail, cs);

    tfEmail = new JTextField(20);
    cs.gridx = 1;
    cs.gridy = 0;
    cs.gridwidth = 2;
    panel.add(tfEmail, cs);

    lbPassword = new JLabel("Password: ");
    cs.gridx = 0;
    cs.gridy = 1;
    cs.gridwidth = 1;
    panel.add(lbPassword, cs);

    pfPassword = new JPasswordField(20);
    cs.gridx = 1;
    cs.gridy = 1;
    cs.gridwidth = 2;
    panel.add(pfPassword, cs);
    panel.setBorder(new LineBorder(Color.GRAY));

    btnLogin = new JButton("Login");
    btnBack = new JButton("Back");

    btnLogin.addActionListener((ActionEvent e) -> this.onClickLogIn(e));
    btnBack.addActionListener((ActionEvent e) -> this.onClickBack(e));

    JPanel jPanel = new JPanel();
    jPanel.add(btnLogin);
    jPanel.add(btnBack);

    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().add(jPanel, BorderLayout.PAGE_END);

    pack();
    setResizable(false);
    setLocationRelativeTo(parent);
  }

  /**
   * Adds action listener to button of Back.
   *
   */
  private void onClickBack(ActionEvent e) {
    this.setVisible(false);
    this.window.setVisible(true);
  }

  /**
   * Show Error Dialogue Message.
   *
   */
  private void showErrorMessage() {
    JOptionPane.showMessageDialog(
        LoginDialog.this,
        "Wrong email/password or Account does not exit",
        "Wong LogIn",
        JOptionPane.ERROR_MESSAGE);
    // reset username and password
    tfEmail.setText("");
    pfPassword.setText("");
  }

  /**
   * Adds action listener to button of Click In.
   *
   */
  private void onClickLogIn(ActionEvent e) {

    if (this.userService.getUserDao().authenticate(this.getEmail(), this.getPassword()) == false) {
      this.showErrorMessage();
      return; // 提前结束 - early exit
    }

    int userID = this.userService.getUserDao().get(this.getEmail()).getUid();

    log.info("user: " + this.userDao.get(userID).getName() + " log in");

    this.setVisible(false);
    this.userFrame = new UserFrame("User Main Page");

    this.userFrame.setVisible(true);
    this.userFrame.setWindow(window);
    this.userFrame.setCurrentLogInUid(this.userService.getUserDao().get(this.getEmail()).getUid());
    this.userFrame.setTextField(
        "Welcome " + this.userService.getUserDao().get(this.getEmail()).getName());
    this.userFrame.setServices(
        userService,
        cardService,
        tripSegmentService,
        generalTripService,
        timeService,
        stationService,
        archiveService);
    this.userFrame.setDaos(
        this.userDao,
        this.nodeDao,
        this.edgeDao,
        this.stationDao,
        this.tripSegmentDao,
        this.generalTripDao);
  }
  //get password form textfield
  public String getPassword() {
    return new String(pfPassword.getPassword());
  }
  //get email form textfield
  public String getEmail() {
    return tfEmail.getText().trim();
  }
  //get username form textfield
  public void setWindow(Window window) {
    this.window = window;
  }
}
