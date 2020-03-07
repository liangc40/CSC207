package edu.toronto.group0162.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.LineBorder;

import edu.toronto.group0162.entity.User;
import edu.toronto.group0162.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a Sign Up Dialogue .
 * A user can register name, password and email on it.
 */

@Slf4j
public class SignUpDialog extends JDialog {

  private final UserService userService;

  private final JTextField tfUsername;

  private final JPasswordField pfPassword;

  private final JTextField tfEmail;

  private final JLabel lbUsername;

  private final JLabel lbPassword;

  private final JLabel lbEmail;

  private JCheckBox adminCheck;

  private final JButton btnRegister;

  private final JButton btnCancel;

  /**
   * Validate email regex
   *
   *@param capturedEmail email input
   */
  public static boolean validate (String capturedEmail){
    Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(capturedEmail);

    return matcher.find();
  }

  /**
   * Adds action listener to button of Register.
   *
   */
  private void onClickRegister(ActionEvent e) {

    User user = new User();
    user.setEmail(this.getEmail());
    user.setName(this.getUsername());
    user.setPassword(this.getPassword());
    user.setAdmin(adminCheck.isSelected());
    user.setCreateAt(Instant.now().getEpochSecond());
    //check if there is admin user registered
    if(adminCheck.isSelected()&&this.userService.hasAdmin()){
              JOptionPane.showMessageDialog(SignUpDialog.this,
                "Admin User is registered and should only be one!",
                "Admin Authority",
                JOptionPane.ERROR_MESSAGE);

              return;
    }
    //check if the email is registered
    if (
            !validate(user.getEmail())||
            !this.userService.checkEmailAvailability(user.getEmail())) {
      JOptionPane.showMessageDialog(SignUpDialog.this,
                                    "Invalid email or email existed",
                                    "Register",
                                    JOptionPane.ERROR_MESSAGE);
      // reset username and password
      tfUsername.setText("");
      pfPassword.setText("");

      return;
    }
    this.userService.registerUser(user);

    if(user.isAdmin() == false){
    JOptionPane.showMessageDialog(SignUpDialog.this,
                                  "You have successfully registered.",
                                  "Register",
                                  JOptionPane.INFORMATION_MESSAGE);
    log.info("user: "+getUsername()+" registered");

    dispose();
  }
  else if(user.isAdmin() == true){
      JOptionPane.showMessageDialog(SignUpDialog.this,
              "You have successfully registered as Admin User",
              "Register",
              JOptionPane.INFORMATION_MESSAGE);
      log.info("admin user: "+getUsername()+" registered");

      dispose();
    }

  }

  /**
   * Initialize SignUp Dialogue
   *
   */
  public SignUpDialog(Frame parent, UserService userService) {
    super(parent, "Register", true);
    this.userService = userService;
    //
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints cs = new GridBagConstraints();

    cs.fill = GridBagConstraints.HORIZONTAL;

    lbUsername = new JLabel("Name: ");
    cs.gridx = 0;
    cs.gridy = 0;
    cs.gridwidth = 1;
    panel.add(lbUsername, cs);

    tfUsername = new JTextField(15);
    cs.gridx = 1;
    cs.gridy = 0;
    cs.gridwidth = 2;
    panel.add(tfUsername, cs);

    lbPassword = new JLabel("Password: ");
    cs.gridx = 0;
    cs.gridy = 1;
    cs.gridwidth = 1;
    panel.add(lbPassword, cs);

    pfPassword = new JPasswordField(15);
    cs.gridx = 1;
    cs.gridy = 1;
    cs.gridwidth = 2;
    panel.add(pfPassword, cs);
    panel.setBorder(new LineBorder(Color.GRAY));

    lbEmail = new JLabel("Email: ");
    cs.gridx = 0;
    cs.gridy = 2;
    cs.gridwidth = 1;
    panel.add(lbEmail, cs);

    tfEmail = new JTextField(15);
    cs.gridx = 1;
    cs.gridy = 2;
    cs.gridwidth = 2;
    panel.add(tfEmail, cs);

    adminCheck = new JCheckBox("Admin User");
    cs.gridx = 0;
    cs.gridy = 3;
    cs.gridwidth = 1;
    panel.add(adminCheck,cs);

    btnRegister = new JButton("Register");
    btnRegister.addActionListener((ActionEvent e) -> this.onClickRegister(e));

    //back to LogIn Page
    btnCancel = new JButton("Cancel");
    btnCancel.addActionListener((ActionEvent e) -> {
      dispose();
    });
    JPanel bp = new JPanel();
    bp.add(btnRegister);
    bp.add(btnCancel);

    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().add(bp, BorderLayout.PAGE_END);

    pack();
    setResizable(false);
    setLocationRelativeTo(parent);
  }

  //get username form textfield
  public String getUsername() {
    return tfUsername.getText().trim();
  }
  //get password form textfield
  public String getPassword() {
    return new String(pfPassword.getPassword());
  }
  //get email form textfield
  public String getEmail() {
    return tfEmail.getText().trim();
  }


}
