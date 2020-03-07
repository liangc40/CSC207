package edu.toronto.group0162.view;

import edu.toronto.group0162.dao.EdgeDao;
import edu.toronto.group0162.dao.NodeDao;
import edu.toronto.group0162.entity.Node;
import edu.toronto.group0162.service.Graph;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Represents a map page.
 * A user can select any two stations and search the shortest path and shortest distance.
 *
 */
public class MapFrame extends JFrame {

    private CityFrame cityFrame;

    private Graph transitGraph;

    private JButton btnSearch;
    private JButton btnConfirm;
    private JButton btnBack;

    JComboBox entranceBox;
    JComboBox exitBox;

    private JLabel lbEntrance;
    private JLabel lbExit;


    JTextArea info;

    @Getter
    @Setter
    private int currentLogInUid;

    //int matrix represents path
    private  int[][] path;
    //double matrix represents shortest distance
    private double[][] floy;
    //List of Nodes
    private List<Node> listNodes;

    /**
     * Sets up map frame page.
     *
     */
    public MapFrame(String title) {
        super( title );                      // invoke the JFrame constructor
        setSize( 1400, 1000 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        setLayout( new FlowLayout(FlowLayout.LEFT,20,40) );       // set the layout manager

        btnSearch = new JButton ("search route");
        btnConfirm = new JButton ("confirm");
        btnBack = new JButton("Back");

        entranceBox = new JComboBox();
        exitBox = new JComboBox();

        lbEntrance = new JLabel("Enter");
        lbExit = new JLabel("Exit");

        info = new JTextArea(10, 25);
        info.setLineWrap(true);
        info.setVisible(true);
        info.setEditable(false);

        add(btnSearch);
        add(btnConfirm);
        add(lbEntrance);
        add(entranceBox);
        add(lbExit);
        add(exitBox);
        add(info);
        add(btnBack);

        btnSearch.addActionListener((ActionEvent e) -> this.onClickSearch(e));
        btnConfirm.addActionListener((ActionEvent e) -> this.onClickConfirm(e));
        btnBack.addActionListener((ActionEvent e) -> this.onClickBack(e));

    }


    /**
     * Adds action listener to button of clicking search shortest path and distance
     * @param e ActionEvent
     */
    private void onClickSearch(ActionEvent e) {

             this.transitGraph.GetShortestPath((String) entranceBox.getSelectedItem(),(String) exitBox.getSelectedItem(),
                        this.floy,this.path,this.listNodes);

        info.setText("Shortest Route: " +this.transitGraph.getTripInfo());


//                cardService.getCardDao().get((Integer)((JComboBox) e.getSource()).getSelectedItem()).getBalance());


        //                        c.getSelectedIndex() + "   ");
                //+ ((JComboBox) e.getSource()).getSelectedItem());


    }

    /**
     * Adds action listener to button of clicking confirm
     * @param e ActionEvent
     */
    private void onClickConfirm(ActionEvent e) {
    }

    /**
     * Sets up Transit Graph
     * @param graph Graph
     */
    public void setTransitGraph(Graph graph){
        this.transitGraph = graph;
    }

    /**
     * Sets up Transit Graph Floyd Algorithm
     * @param path matrix of shortest path
     * @param floy matrix of shortest distance
     * @param listNodes
     */
    public void setAlgorithm(int[][]path, double[][]floy, List<Node>listNodes){
        this.path = path;
        this.floy = floy;
        this.listNodes = listNodes;
    }
    /**
     * Adds action listener to button of clicking back
     * @param e ActionEvent
     */
    private void onClickBack(ActionEvent e) {
        this.setVisible(false);
        this.cityFrame.setVisible(true);
    }
    /**
     * Sets city frame page
     * @param cityFrame CityFrame
     */
    public void setCityFrame(CityFrame cityFrame){
        this.cityFrame = cityFrame;
    }


}
