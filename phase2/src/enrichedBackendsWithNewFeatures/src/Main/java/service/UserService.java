package service;

import entity.AdminUser;
import entity.TransitPass;
import dao.CardDao;
import dao.UserDao;
import dao.UserMoneyManagerDao;
import entity.Card;
import entity.User;
import logger.UserLogger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.PastTripManager;
import manager.UserMoneyManager;
import java.util.Iterator;

@AllArgsConstructor
public class UserService {

    @Getter private UserDao userDao;

    private CardDao cardDao;
    private UserMoneyManagerDao userMoneyManagerDao;

    public User createNewUser(String name, String email, String password, String birthday, String trafficStationNearWork, String trafficStationNearHome, UserMoneyManager userMoneyManager, PastTripManager pastTripManager) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setBirthday(birthday);
        user.setTrafficStationNearHome(trafficStationNearHome);
        user.setTrafficStationNearWork(trafficStationNearWork);
        user.setUserMoneyManagerID(userMoneyManager.getUserMoneyManagerID());
        user.setPastTripManagerID(pastTripManager.getPastTripManagerID());
        user.setUserLogger(new UserLogger());
        User savedUser = userDao.save(user);
        userMoneyManager.setUserID(savedUser.getUserID());
        userMoneyManagerDao.update(userMoneyManager);
        return  savedUser;
    }

    public void addCard(User user, Card card) {
        if (user.getClass().getSimpleName().equals("AdminUser")) {
            User packagedUser = userDao.get(user.getUserID());
            updateCardList(packagedUser, card);
            card.setUserID(packagedUser.getUserID());
        } else {
            updateCardList(user, card);
            card.setUserID(user.getUserID());
        }
        cardDao.update(card);
    }

    private void updateCardList(User user, Card card) {
        user.getCardIDs().add(card.getCardID());
        userDao.update(user);
    }

    public void deleteCard(User user, Card card) {
        User packagedUser;
        if (user.getClass().getSimpleName().equals("AdminUser")) {
            packagedUser = packageToCommonUser((AdminUser) user);
        } else {
            packagedUser = user;
        }
        Iterator iterator = packagedUser.getCardIDs().iterator();
        while (iterator.hasNext())
        {
            int id = (Integer)iterator.next();
            if (id == card.getCardID())
                iterator.remove();
        }
        userDao.update(packagedUser);
        cardDao.delete(card.getCardID());
    }

    public static User packageUser(User user) {
        user.setUserLogger(new UserLogger());
        return user;
    }

    public User packageToCommonUser(AdminUser adminUser) {
        return userDao.get(adminUser.getUserID());
    }
}
