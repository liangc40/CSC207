package edu.toronto.group0162.entity;

import lombok.Data;

/**
 * A card class that stores the card id, the balance, isActive, uid, createActive, deleted.
 */
@Data
public class Card {

  private int cid;

  private double balance = 19;

  private boolean isActive = true;

  private int uid;

  private long createAt;

  private boolean deleted = false;

}
