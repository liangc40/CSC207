package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import manager.CardMoneyManager;
import manager.PastTripManager;


@Data
public class Card {

    private int cardID;

    private int userID;

    private int pastTripManagerID;

    private int cardMoneyManagerID;

    private ArrayList<Integer> transitPassesIDs = new ArrayList<>();

    private boolean isActive;

    private long registrationTime;
}