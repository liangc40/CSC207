package table;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalFareByCity {
    private int ID;
    private String city;
    private int userID;
    private BigDecimal totalFareByCity;
}