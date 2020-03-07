package edu.toronto.group0162.view;

import com.sun.tools.javah.Gen;
import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.GeneralTrip;
import edu.toronto.group0162.entity.TripSegment;
import edu.toronto.group0162.entity.User;
import edu.toronto.group0162.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Represents a TripFrame pag.
 * A user can take a trip by tapping in and tapping out.
 *
 */
@Slf4j
public class TripFrame extends JFrame {


    private boolean tapInClicked = false;
    private boolean tapOutClicked = false;
    private int tapInClikedCount = 0;
    private int tapOutClikedCount = 0;

    private TripSegment tripSegmentBuild;
    private GeneralTrip newGeneralTrip;
    private GeneralTrip oldGeneralTrip;
    private Card cardUsing;


    private RouteFrame routeFrame;

    @Getter
    @Setter
    private String transitLine;

    JComboBox boxEntrance;
    JComboBox boxExit;
    JComboBox boxCard;
    JTextField enterTime;
    JTextField exitTime;
    JTextArea warnInfo;

    @Getter
    @Setter
    private int currentLogInUid;
    @Getter
    @Setter
    private int tsid;


    private TripSegmentService tripSegmentService;
    private TimeService timeService;
    private GeneralTripService generalTripService;
    private CardService cardService;
    private StationService stationService;


    private UserDao userDao;
    private StationDao stationDao;
    private TripSegmentDao tripSegmentDao;
    private GeneralTripDao generalTripDao;

    /**
     * Sets up trip frame page.
     *
     * @param title
     */
    public TripFrame(String title) {
        super( title );
        setSize( 1000, 300  );
        addComponentsToPane(this.getContentPane());

    }

    /**
     * Builds up trip frame page.
     *
     * @param pane
     */
    public void addComponentsToPane(Container pane) {
        JButton button;
        JLabel label;
        JTextField textField;

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        label = new JLabel("Enter");
        c.gridx = 0;
        c.gridy = 0;
        pane.add(label, c);

        boxEntrance = new JComboBox();
        c.gridx = 1;
        c.gridy = 0;
        pane.add(boxEntrance, c);

        label = new JLabel("Enter Time");
        c.gridx = 3;
        c.gridy = 0;
        pane.add(label, c);

        enterTime = new JTextField(15);
        c.gridx = 4;
        c.gridy = 0;
        pane.add(enterTime, c);

        button = new JButton("Tap In");
        button.setSize(100,100);
        button.addActionListener((ActionEvent e) -> {
            try {
                this.onClickTapIn(e);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });
        c.gridx = 7;
        c.gridy = 0;
        pane.add(button, c);

        label = new JLabel("Exit");
        c.gridx = 0;
        c.gridy = 3;
        pane.add(label, c);

        boxExit = new JComboBox();
        c.gridx = 1;
        c.gridy = 3;
        pane.add(boxExit, c);

        label = new JLabel("Exit Time");
        c.gridx = 3;
        c.gridy = 3;
        pane.add(label, c);

        exitTime = new JTextField(15);
        c.gridx = 4;
        c.gridy = 3;
        pane.add(exitTime, c);

        button = new JButton("Tap Out");
        button.setSize(100,100);
        button.addActionListener((ActionEvent e) -> this.onClickTapOut(e));
        c.gridx = 7;
        c.gridy = 3;
        pane.add(button, c);

        label = new JLabel("Card");
        c.gridx = 0;
        c.gridy = 5;
        pane.add(label, c);

        boxCard = new JComboBox();
        c.gridx = 1;
        c.gridy = 5;
        pane.add(boxCard, c);

        button = new JButton("Back");
        button.setSize(100,100);
        button.addActionListener((ActionEvent e) -> this.onClickBack(e));
        c.gridx = 0;
        c.gridy = 9;
        pane.add(button, c);

        warnInfo = new JTextArea(3,20);
        c.gridx = 0;
        c.gridy = 10;
        pane.add(warnInfo,c);

    }



    /**
     * Adds action listener to button of clicking tap in
     *
     * @param e ActionEvent
     */
    private void onClickTapIn(ActionEvent e) throws ParseException {

        //Card Box cannot be empty
        if(null == boxCard.getSelectedItem()){
            JOptionPane.showMessageDialog(TripFrame.this,
                    "Please buy a card to take transit",
                    "Tap In Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //get Card using for this trip
        this.cardUsing = this.cardService.getCardDao().get(this.getCardNumber());

            //user can't click tap in more than one time
        if(this.tapInClikedCount == 0 && this.tapInClicked == false){

            //user can't input enter time which violates the simple date format
        if(!validateTime(this.getEnterTime())){
        JOptionPane.showMessageDialog(TripFrame.this,
                "Please input correct enter time",
                "Time Format Error",
                JOptionPane.ERROR_MESSAGE);
        return;}

            //user can't input exit time which violates the simple date format
            if(!validateTime(getExitTime())){
                JOptionPane.showMessageDialog(TripFrame.this,
                        "Please input correct exit time",
                        "Time Format Error",
                        JOptionPane.ERROR_MESSAGE);}

            //user can't input exit time which is early than the enter time
            if(validateExitTime(getEnterTime(),getExitTime()) || getEnterTime().equals(getExitTime())){
                JOptionPane.showMessageDialog(TripFrame.this,
                        "Exit time should late than Enter Time!",
                        "Time Error",
                        JOptionPane.ERROR_MESSAGE);
                this.checkExitTime = true;
                return;
            }

            //user cannot use a card to tap in of which the balance is negative until the balance is added
        if(cardUsing.isActive() == false){
            JOptionPane.showMessageDialog(TripFrame.this,
                    "Card is not active due to negative balance"+"\n"+"Please load money!",
                    "Card Balance Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

            if(getExit().equals(getEntrance())){JOptionPane.showMessageDialog(TripFrame.this,
                    "you have to choose different entrance and exit",
                    "Entrance Exit Exception",
                    JOptionPane.ERROR_MESSAGE);
                this.checkSameStaion = true;
                return;}

        else{

            TripSegment tripSegment = new TripSegment();
            tripSegment.setUid(this.currentLogInUid);
            tripSegment.setTransitLine(this.getTransitLine());
            tripSegment.setEntrance(this.getEntrance());
            tripSegment.setEnterTime(this.getEnterTime());
            tripSegment.setCid(this.getCardNumber());

            //continues trip check
            if (null == this.generalTripService.getGeneralTripDao().get(currentLogInUid,false)){
            GeneralTrip generalTrip = new GeneralTrip();
            generalTrip.setStartTime(this.getEnterTime());
            generalTrip.setUid(this.currentLogInUid);
            this.newGeneralTrip = this.generalTripDao.save(generalTrip);
                tripSegment.setGtid(newGeneralTrip.getGtid());
            }
            else{
                this.oldGeneralTrip = this.generalTripService.getGeneralTripDao().get(currentLogInUid,false);

                boolean condition1 = (this.timeService.getHourDifference(tripSegment.getEnterTime(),oldGeneralTrip.getStartTime()) <= 2.0);

//          System.out.println(this.timeService.getHourDifference(tripSegment.getEnterTime(),oldGeneralTrip.getStartTime()));
//          System.out.println(condition1);

                boolean condition2 = (this.tripSegmentService.getTripSegmentDao().getLatest(currentLogInUid).getExit().equals(tripSegment.getEntrance()));

                if(condition1 && condition2){tripSegment.setGtid(oldGeneralTrip.getGtid());} //SET CAP!!!
                else {oldGeneralTrip.setFinished(true);
                this.generalTripService.getGeneralTripDao().updateFinished(oldGeneralTrip);

                    GeneralTrip generalTrip = new GeneralTrip();
                    generalTrip.setStartTime(this.getEnterTime());
                    generalTrip.setUid(this.currentLogInUid);
                    this.newGeneralTrip = this.generalTripDao.save(generalTrip);
                    tripSegment.setGtid(newGeneralTrip.getGtid());
                }


            }

            //store tripsegment enter info into database
            this.tripSegmentBuild = this.tripSegmentDao.save(tripSegment);
            //store tsid into this trip frame
            this.setTsid(tripSegmentBuild.getTsid());

            //update user's card balance, user trip's info (enter, fare)
            this.tripSegmentService.updateEnter(tripSegmentBuild,cardUsing,this.getTransitLine());

            // user will receive balance warning dialog if the using card's balance is less than
            // zero after one taping in/taping out's charge
            if(cardUsing.getBalance()<=0){
                cardUsing.setActive(false);
                this.cardService.getCardDao().update(cardUsing);
//                JOptionPane.showMessageDialog(TripFrame.this,
//                        "Card "+cardUsing.getCid()+" is negative balance now"+"\n Please load money",
//                        "Card Negative Balance",
//                        JOptionPane.INFORMATION_MESSAGE);
                this.warnInfo.setText("Card "+cardUsing.getCid()+" is negative balance now"+"\n Please load money");
            }

            if(this.getTransitLine().contains("Bus")){
            JOptionPane.showMessageDialog(TripFrame.this,
                "you enter "+this.getEntrance()+" at "+this.getEnterTime()+" at "+this.getTransitLine()+
                        "\n with charge of "+tripSegmentBuild.getFare(),
                "Tap In",
                JOptionPane.INFORMATION_MESSAGE);
            log.info("User: "+this.userDao.get(currentLogInUid).getName()+
            " enter "+this.getEntrance()+" at "+this.getEnterTime()+" on "+this.getTransitLine()+
                        " with charge of "+tripSegmentBuild.getFare());}

            if(this.getTransitLine().contains("Subway")){
            JOptionPane.showMessageDialog(TripFrame.this,
                    "you enter "+this.getEntrance()+" at "+this.getEnterTime()+" at "+this.getTransitLine(),
                    "Tap In",
                    JOptionPane.INFORMATION_MESSAGE);
                log.info("User: "+this.userDao.get(currentLogInUid).getName()+" enter "+this.getEntrance()+" at "+this.getEnterTime()+" on "+this.getTransitLine());}

        this.tapInClicked = true;
        this.tapInClikedCount = 1;
        }}

        //user can't click tap in more than one time
        else{JOptionPane.showMessageDialog(TripFrame.this,
                "you cannot click tap in twice",
                "Tap In Error",
                JOptionPane.ERROR_MESSAGE);}

    }

    boolean checkSameStaion = false;
    boolean checkExitTime = false;

    /**
     * Adds action listener to button of clicking tap out
     *
     * @param e ActionEvent
     */
    private void onClickTapOut(ActionEvent e) {

        //Card Box cannot be empty
        if(null == boxCard.getSelectedItem()){
            JOptionPane.showMessageDialog(TripFrame.this,
                    "Please buy a card to take transit",
                    "Tap In Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        //tap in and out at the same station: not counted stored as a trip/a dialogue pop up
        // to show user has to choose different entrance and exit
        if(getExit().equals(getEntrance())){JOptionPane.showMessageDialog(TripFrame.this,
                "you have to choose different entrance and exit",
                "Entrance Exit Exception",
                JOptionPane.ERROR_MESSAGE);
        this.checkSameStaion = true;
        return;}

                //user can't only click tap out without clicking tap in first
                if(null == this.tripSegmentBuild){JOptionPane.showMessageDialog(TripFrame.this,
                        "you didn't tap in for this trip!",
                        "Tap Out Warning",
                        JOptionPane.ERROR_MESSAGE);
        return;}

                if(checkSameStaion == false){

        if(this.tapOutClikedCount == 0 && this.tapOutClicked == false){

//            //user can't input exit time which violates the simple date format
//            if(!validateTime(getExitTime())){
//                JOptionPane.showMessageDialog(TripFrame.this,
//                        "Please input correct exit time",
//                        "Time Format Error",
//                        JOptionPane.ERROR_MESSAGE);}

//            //user can't input exit time which is early than the enter time
//            if(validateExitTime(getEnterTime(),getExitTime()) || getEnterTime().equals(getExitTime())){
//                JOptionPane.showMessageDialog(TripFrame.this,
//                        "Exit time should late than Enter Time",
//                        "Tap Out Error",
//                        JOptionPane.ERROR_MESSAGE);
//                this.checkExitTime = true;
//                return;
//            }


                this.tripSegmentBuild.setExit(this.getExit());
                this.tripSegmentBuild.setExitTime(this.getExitTime());

          System.out.println(tripSegmentBuild.getTsid());
                System.out.println(tripSegmentBuild.getEntrance());
                System.out.println(tripSegmentBuild.getEntrance());
                System.out.println(tripSegmentBuild.getExit());
          System.out.println(cardUsing.getCid());
          System.out.println(this.getTransitLine());

                this.tripSegmentService.updateExit(tripSegmentBuild,cardUsing,this.getTransitLine());

                // user will receive balance warning dialog if the using card's balance is less than
                // zero after one taping in/taping out's charge
                if(cardUsing.getBalance()<=0){
                    cardUsing.setActive(false);
                    this.cardService.getCardDao().update(cardUsing);
//                    JOptionPane.showMessageDialog(TripFrame.this,
//                            "Card "+cardUsing.getCid()+" is negative balance now"+"\n Please load money",
//                            "Card Negative Balance",
//                            JOptionPane.INFORMATION_MESSAGE);
                    this.warnInfo.setText("Card "+cardUsing.getCid()+" is negative balance now"+"\n Please load money");

                }

                if(this.getTransitLine().contains("Subway")){
                JOptionPane.showMessageDialog(TripFrame.this,
                        "you exit "+this.getExit()+" at "+this.getExitTime()+" on "+this.getTransitLine()+
                        "\n with charge of "+tripSegmentBuild.getFare(),
                        "Tap Out",
                        JOptionPane.INFORMATION_MESSAGE);
                    log.info("User: "+this.userDao.get(currentLogInUid).getName()+
                            " exit "+this.getExit()+" at "+this.getExitTime()+" on "+this.getTransitLine()+
                            " with charge of "+tripSegmentBuild.getFare());
                }

                if(this.getTransitLine().contains("Bus")){
                    JOptionPane.showMessageDialog(TripFrame.this,
                            "you exit "+this.getExit()+" at "+this.getExitTime()+" at "+this.getTransitLine(),
                            "Tap Out",
                            JOptionPane.INFORMATION_MESSAGE);
                    log.info("User: "+this.userDao.get(currentLogInUid).getName()+
                            " exit "+this.getExit()+" at "+this.getExitTime()+" at "+this.getTransitLine());}
                this.tapOutClicked = true;
                this.tapOutClikedCount = 1;
            }}

        //user can't click tap out more than one time
        else{JOptionPane.showMessageDialog(TripFrame.this,
                "you cannot click tap in twice",
                "Tap In Error",
                JOptionPane.ERROR_MESSAGE);
        return;}


    }

    public void setRouteFrame(RouteFrame routeFrame){
        this.routeFrame = routeFrame;
    }

    private void onClickBack(ActionEvent e) {
        //user can click back if neither tap in nor tap out button clicked
        // or both of tap in and tap out button clicked with correct information input
        if(backCondition1 || backCondition2){
        this.setVisible(false);
        this.routeFrame.setVisible(true);}
        //user can't only click tap in and then click back
        else{
            JOptionPane.showMessageDialog(TripFrame.this,
                    "you cannot only tap in or tap out",
                    "Back Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //user can click back if both of tap in and tap out button clicked with correct information input
    boolean backCondition1 = this.tapInClicked == true && this.tapOutClicked == true;
    //user can click back if neither tap in nor tap out button clicked
    boolean backCondition2 = this.tapInClicked == false && this.tapOutClicked == false && this.tapInClikedCount == 0 && this.tapOutClikedCount  == 0;

    public String getEnterTime() {
        return enterTime.getText().trim();
    }

    public String getExitTime() {
        return exitTime.getText().trim();
    }

    public int getCardNumber(){return (Integer) boxCard.getSelectedItem();}

    public String getEntrance(){return (String)boxEntrance.getSelectedItem();}

    public String getExit(){return (String)boxExit.getSelectedItem();}

    public void setDaos(UserDao userDao, StationDao stationDao,
                        TripSegmentDao tripSegmentDao, GeneralTripDao generalTripDao){
        this.userDao = userDao;
        this.stationDao = stationDao;
        this.tripSegmentDao = tripSegmentDao;
        this.generalTripDao = generalTripDao;
    }

    public void setServices(CardService cardService, TripSegmentService tripSegmentService, GeneralTripService generalTripService, TimeService timeService,StationService stationService){
        this.cardService = cardService;
    this.tripSegmentService = tripSegmentService;
    this.generalTripService = generalTripService;
    this.timeService = timeService;
    this.stationService = stationService;}

    /**
     * Validate time regex
     * @param timeInput
     * @return
     */
    public static boolean validateTime (String timeInput){
        Pattern VALID_SIMPLE_DATE_REGEX =
                Pattern.compile("\\d{4}-[01]\\d-[0-3]\\d\\s[0-2]\\d((:[0-5]\\d)?){2}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_SIMPLE_DATE_REGEX .matcher(timeInput);
        return matcher.find();
    }

    /**
     * Validate exitTime
     * @param enterTime
     * @param exitTime
     *
     * @return boolean validated time
     */
    public boolean validateExitTime(String enterTime, String exitTime){
        double timeDifference = this.timeService.getHourDifferenceWithoutAbs(enterTime, exitTime);
    System.out.println(timeDifference);
        return Double.valueOf(timeDifference)<0;
    }


}
