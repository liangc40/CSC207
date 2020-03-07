package entity;

import lombok.Data;
import logger.UserLogger;
import java.util.ArrayList;

/** A User of this transit system. */
@Data
public class User {
    /** ID in database. */
    private int userID;
    /** ID of Card of this User. */
    private ArrayList<Integer> cardIDs = new ArrayList<>();
    /** name of this User. */
    private String name;
    /** email of this User. */
    private String email;
    /** password of this User. */
    private String password;
    /** registrationTime of this User. */
    private long registrationTime;
    /** birthday of this User. */
    private String birthday;
    /** trafficStationNearHome of this User. */
    private String trafficStationNearHome;
    /** trafficStationNearWork of this User. */
    private String trafficStationNearWork;
    /** pastTripManagerID of this User. */
    private int pastTripManagerID;
    /** userMoneyManagerID of this User. */
    private int userMoneyManagerID;
    /** userLogger of this User. */
    private UserLogger userLogger;
}