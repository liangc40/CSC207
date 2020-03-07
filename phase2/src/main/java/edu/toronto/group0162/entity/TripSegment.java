package edu.toronto.group0162.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * A TripSegment that have tsid, uid, gtid, cid, enterTime, exitTime, entrance, exit, transitLine, fare, cap.
 */
@Data
public class TripSegment {

    @Getter
    @Setter
    private int tsid;
    private int uid;
    private int gtid;
    private int cid;
    private String enterTime;
    private String exitTime;
    private String entrance;
    private String exit;

    private String transitLine;
    private double fare;
    private double cap = 6;

    /**
     * A method toString that make a string representation of the TripSegment.
     *
     * @param cid: card id.
     * @param entrance: string representation of entrance.
     * @param exit: string representation of exit.
     * @return a string representation of the TripSegment.
     */
    public String toString(int cid, String entrance, String exit){
        return "Card "+ cid+" was used "+"with charged of "+fare+" on "+transitLine+"\n"+
                "Enter at "+entrance+" on "+enterTime+" and Exit at "+exit+" on "+exitTime +"\n";
    }
}
