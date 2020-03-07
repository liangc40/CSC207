package table;

import lombok.Data;

@Data
public class TripFrequencyBetweenStations {
    private int ID;
    private int pastTripManagerID;
    private String routeName;
    private int times;
}