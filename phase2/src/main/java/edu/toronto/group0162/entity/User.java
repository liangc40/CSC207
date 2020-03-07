package edu.toronto.group0162.entity;

import lombok.Data;


/**
 * A class user that have uid, name, email, isAdmin, password.
 */
@Data
public class User {

  private int uid;
  private String name;
  private String email;

  private boolean isAdmin;
  private String password;

  private long createAt;

}
