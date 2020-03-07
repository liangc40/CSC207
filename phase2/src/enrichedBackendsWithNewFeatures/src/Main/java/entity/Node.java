package entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * A node class that have nid, name, transit_type.
 */
@Data
public class Node {
    @Getter
    @Setter
    /** ID in database. */
    private int nid;
    /** ID in database. */
    private String name;
    /** City of this Node. */
    private String transit_type;
}
