package table;

import lombok.Data;
import java.util.ArrayList;

@Data
public class TransitPassExpirationDate {
    private int ID;
    private int adminUserID;
    private String expirationDate;
    ArrayList<Integer>  transitPassIDs;
}