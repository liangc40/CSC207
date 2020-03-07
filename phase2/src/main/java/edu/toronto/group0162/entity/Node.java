package edu.toronto.group0162.entity;

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
    private int nid;
    private String name;
    private String transit_type;
}
