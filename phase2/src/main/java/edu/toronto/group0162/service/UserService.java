package edu.toronto.group0162.service;

import edu.toronto.group0162.dao.CardDao;
import edu.toronto.group0162.dao.UserDao;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
public class UserService {

  @Getter
  private final UserDao userDao;
//  private final CardDao cardDao;

  /**
   * Save and return a user object.
   *
   * @param user: a user.
   * @return a user object that saved.
   */
  public User registerUser(User user) {
    return this.userDao.save(user);
  }

  /**
   * Return whether a specific email in a UserService.
   *
   * @param email: a string representation of an email.
   * @return a boolean.
   */
  public boolean checkEmailAvailability(String email) {
    return this.userDao.get(email) == null;
  }

  /**
   * Return whether UserService has a admin.
   *
   * @return boolean tells you does this UserSerivice has admin.
   */
  public boolean hasAdmin(){

    return this.userDao.getAdmin()!= null;
  }
}
