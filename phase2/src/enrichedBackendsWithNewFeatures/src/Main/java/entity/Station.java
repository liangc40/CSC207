package entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * A class have uid, point, next_distance, name, line.
 */
@Data
public class Station {

    @Getter
    @Setter

    private int sid;
    private int point;
    private double next_distance;
    private String name;
    private String line;

}