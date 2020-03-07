package edu.toronto.group0162.view;



import edu.toronto.group0162.entity.User;
import edu.toronto.group0162.service.UserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
/**
 * Represents a Personal Information page.
 * A user can change name and password in it.
 *
 */
@Slf4j
public class PersonalInfoFrame extends JFrame {

    private UserFrame userFrame;
    private UserService userService;
    @Getter
    @Setter
    private int currentLogInUid;

    JTextField personalInfo;
    JTextField name;
    JTextField password;

    boolean nameChangeClicked;
    boolean nameSaveClicked;

    boolean passwordChangeClicked;
    boolean passwordSaveClicked;

    public PersonalInfoFrame(String title) {
        super( title );
        setSize( 1200, 300  );
        addComponentsToPane(this.getContentPane());

    }

    /**
     * Set Dao classes injection from postgreSQL JDBC connection
     *
     */
    public void addComponentsToPane(Container pane) {
        JButton button;
        JLabel label;

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        personalInfo = new JTextField(45);
        c.gridx = 0;
        c.gridy = 0;
        pane.add(personalInfo, c);

        label = new JLabel("Name");
        c.gridx = 0;
        c.gridy = 2;
        pane.add(label, c);

        name = new JTextField(15);
        c.gridx = 1;
        c.gridy = 2;
        pane.add(name, c);

        button = new JButton("Change");
        button.addActionListener((ActionEvent e) -> this.onClickChangerName(e));
        c.gridx = 3;
        c.gridy = 2;
        pane.add(button, c);

        button = new JButton("Save");
        button.addActionListener((ActionEvent e) -> this.onClickSaveName(e));
        c.gridx = 4;
        c.gridy = 2;
        pane.add(button, c);

        label = new JLabel("Password");
        c.gridx = 0;
        c.gridy = 3;
        pane.add(label, c);

        password = new JTextField(15);
        c.gridx = 1;
        c.gridy = 3;
        pane.add(password, c);

        button = new JButton("Change");
        button.addActionListener((ActionEvent e) -> this.onClickChangerPassword(e));
        c.gridx = 3;
        c.gridy = 3;
        pane.add(button, c);

        button = new JButton("Save");
        button.addActionListener((ActionEvent e) -> this.onClickSavePassword(e));
        c.gridx = 4;
        c.gridy = 3;
        pane.add(button, c);

        button = new JButton("Back");
        button.addActionListener((ActionEvent e) -> this.onClickBack(e));
        c.gridx = 0;
        c.gridy = 5;
        pane.add(button, c);

    }

    /**
     * Adds action listener to button of saving password.
     *
     */
    private void onClickSavePassword(ActionEvent e) {
        User currentUser = this.userService.getUserDao().get(currentLogInUid);
        currentUser.setPassword(getPassword());
        this.userService.getUserDao().updatePassword(currentUser);
        this.password.setEditable(false);
        JOptionPane.showMessageDialog(PersonalInfoFrame.this,
                "You have changed the password",
                "Personal Info Change",
                JOptionPane.INFORMATION_MESSAGE);
        log.info("User: "+this.userService.getUserDao().get(currentLogInUid).getName() + " changed login password");
        this.passwordSaveClicked = true;
        this.passwordChangeClicked = false;
    }

    /**
     * Adds action listener to button of saving name.
     *
     */
    private void onClickSaveName(ActionEvent e) {
        User currentUser = this.userService.getUserDao().get(currentLogInUid);
        currentUser.setName(getUsername());
        this.userService.getUserDao().updateName(currentUser);
        this.name.setEditable(false);
        JOptionPane.showMessageDialog(PersonalInfoFrame.this,
                "You have changed the name",
                "Personal Info Change",
                JOptionPane.INFORMATION_MESSAGE);
        log.info("User: "+this.userService.getUserDao().get(currentLogInUid).getName() + " changed name");
        this.nameSaveClicked = true;
        this.nameChangeClicked = false;
    }

    /**
     * Adds action listener to button of changing password.
     *
     */
    private void onClickChangerPassword(ActionEvent e) {
        this.passwordSaveClicked = false;
        this.password.setEditable(true);
        this.passwordChangeClicked = true;
    }
    /**
     * Adds action listener to button of changing name.
     *
     */
    private void onClickChangerName(ActionEvent e) {
        this.nameSaveClicked = false;
        this.name.setEditable(true);
        this.nameChangeClicked = true;
    }

    /**
     * Adds action listener to button of clicking back.
     *
     */
    private void onClickBack(ActionEvent e) {
        if(nameChangeClicked == true && nameSaveClicked==false){
            JOptionPane.showMessageDialog(PersonalInfoFrame.this,
                    "You didn't save your changed name",
                    "Personal Info Change",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(passwordChangeClicked == true && passwordSaveClicked==false){
            JOptionPane.showMessageDialog(PersonalInfoFrame.this,
                    "You didn't save your changed password",
                    "Personal Info Change",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
        this.setVisible(false);
        this.userFrame.setVisible(true);}}

    public void setUserFrame(UserFrame userFrame){
        this.userFrame = userFrame;
    }
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    public String getUsername() {
        return name.getText().trim();
    }
    public String getPassword() {
        return password.getText().trim();
    }

}
