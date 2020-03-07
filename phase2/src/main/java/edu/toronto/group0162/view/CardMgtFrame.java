package edu.toronto.group0162.view;

import edu.toronto.group0162.dao.UserDao;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.User;
import lombok.Getter;
import lombok.Setter;
import edu.toronto.group0162.service.CardService;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.Observable;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Represents a card management page.
 * A user can open card management page to add card, delete card, load money and show card information.
 *
 */
@Slf4j
public class CardMgtFrame  {


    private int[] moneyLoadSelection = {10,20,50};

    @Getter
    @Setter
    private int currentLogInUid;

    private JButton btnLogOut;
    private JButton btnAddCard;
    private JButton btnDelete;
    private JButton btnLoad;
    private JButton btnShow;

    private UserFrame userFrame;

    JFrame frame;

    private CardService cardService;
    private UserDao userDao;

    JComboBox moneyLoadSelectionBox;
    JComboBox cardNumBox;
    JTextArea cardInfo;
    JTextArea costInfo;
    /**
     * Sets up card management page.
     *
     */
    public CardMgtFrame() {
        frame = new JFrame("Card Management");

        frame.setSize( 1400, 1000);

        frame.setLayout( new FlowLayout(FlowLayout.LEFT,20,40) );       // set the layout manager

        btnLogOut = new JButton ("Back");
        btnAddCard = new JButton("Add Card");
        btnDelete = new JButton("Delete Card");
        btnLoad = new JButton("Load Money");
        moneyLoadSelectionBox = new JComboBox();
        cardNumBox = new JComboBox();
        btnShow = new JButton("Show");

        cardInfo = new JTextArea(10, 30);
        cardInfo.setLineWrap(true);
        cardInfo.setVisible(true);
        cardInfo.setEditable(false);

        costInfo = new JTextArea(20, 50);
        costInfo.setLineWrap(true);
        costInfo.setVisible(true);
        costInfo.setEditable(false);

        frame.add(btnLogOut);
        frame.add(btnAddCard);
        frame.add(btnDelete);
        frame.add(btnLoad);
        frame.add(moneyLoadSelectionBox);
        frame.add(cardNumBox);
        frame.add(cardInfo);
        frame.add(btnShow);
        frame.add(costInfo);

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        btnLogOut.addActionListener((ActionEvent e) -> this.onClickLogOut(e));
        btnAddCard.addActionListener((ActionEvent e) -> this.onClickAddCard(e));
        btnDelete.addActionListener((ActionEvent e) -> this.onClickDeleteCard(e));
        btnLoad.addActionListener((ActionEvent e) -> this.onClickLoadMoney(e));
        btnShow.addActionListener((ActionEvent e) -> this.onClickShow(e));


        int count = 0;
        for (int i = 0; i < 3; i++)
            moneyLoadSelectionBox.addItem(moneyLoadSelection[count++]);

    }

    /**
     * Adds action listener to button of showing Card information.
     *
     */
    private void onClickShow(ActionEvent e) {

        if(null ==cardNumBox.getSelectedItem()){
            cardInfo.setText("There is no card registered!" + "\n"+"You can add card now");

            return;
        }

        if(! (null ==cardNumBox.getSelectedItem())){

        cardInfo.setText(this.cardService.printCardInfo((Integer) cardNumBox.getSelectedItem()));}
    }


    /**
     * Adds action listener to button of deleting card.
     *
     */
    private void onClickDeleteCard(ActionEvent e) {
        Card cardDelete = this.cardService.getCardDao().get((Integer) cardNumBox.getSelectedItem());
        int cardID = cardDelete.getCid();
        this.cardService.deleteCard(cardDelete);
        this.cardNumBox.removeItem(cardID);
        log.info("user: "+this.userDao.get(currentLogInUid).getName()+" delete card "+cardID);

        JOptionPane.showMessageDialog(this.frame,
                "Card "+ cardDelete.getCid()+" has been deleted",
                "Card Deleted",
                JOptionPane.INFORMATION_MESSAGE);
    }



    public void setUserFrame(UserFrame userFrame)
    {
        this.userFrame = userFrame;
    }

    /**
     * Adds action listener to button of loging out.
     *
     */
    private void onClickLogOut(ActionEvent e) {
        this.frame.setVisible(false);
        this.userFrame.setVisible(true);
    }

    /**
     * Adds action listener to button of loading money.
     *
     */
    private void onClickLoadMoney(ActionEvent e) {
        Card cardGet = this.cardService.getCardDao().get((Integer) cardNumBox.getSelectedItem());
        this.cardService.addBalance(cardGet,(Integer) moneyLoadSelectionBox.getSelectedItem());
        log.info("user "+this.userDao.get(currentLogInUid).getName()+" load "+moneyLoadSelectionBox.getSelectedItem()+
        " into Card "+ cardGet.getCid());
        JOptionPane.showMessageDialog(this.frame,
                (Integer) moneyLoadSelectionBox.getSelectedItem()+" has been loaded into Card "+ cardGet.getCid(),
                "Load Money",
                JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Adds action listener to button of adding card.
     *
     */
    private void onClickAddCard(ActionEvent e) {
        Card newCard = new Card();
        newCard.setCreateAt(Instant.now().getEpochSecond());
        Card savedCard = this.cardService.addCard(this.getCurrentLogInUid(),newCard);

        this.cardNumBox.addItem(savedCard.getCid());
        log.info("user "+this.userDao.get(currentLogInUid).getName()+" add Card "+savedCard.getCid());

        JOptionPane.showMessageDialog(this.frame,
                "Card "+ savedCard.getCid()+" has been added",
                "New Card Added",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void setCardService(CardService cardService){
        this.cardService = cardService;
    }
    public void setUserDao(UserDao userDao){this.userDao = userDao;}

}
