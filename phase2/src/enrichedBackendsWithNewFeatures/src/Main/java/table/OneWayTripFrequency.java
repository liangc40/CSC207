package table;

import lombok.Data;

@Data
public class OneWayTripFrequency {
    private int ID;
    private int pastTripManagerID;
    private String routeName;
    private int times;
}
