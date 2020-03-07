package service;

import java.math.BigDecimal;

import dao.SystemInfoDao;
import entity.ChildrenDiscount;
import entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.TimeManager;
import java.util.ArrayList;

public class ChildrenDiscountService {

    @AllArgsConstructor
    public class ChildrenDiscount {
        private BigDecimal discountRate;

        @Getter
        /** Expiry date of this TransitPass */
        private String expirationDate;
        private TimeManager timeManager;
        private SystemInfoDao systemInfoDao;

        public void createNewChildrenDiscount(String type, String city, User user) {
            switch (type) {
                case "child discount":
                    BigDecimal ageLimitForChildPass = systemInfoDao.getChildPassAgeLimit(city, "child pass");
                    String turningJunior =
                            timeManager.find_nth_birthday(user, ageLimitForChildPass.intValue());
                    discountRate = systemInfoDao.getChildPassDiscountRate(city, "child pass");
                    expirationDate = timeManager.increaseDate(turningJunior, 1);
                    break;
                case "junior discount":
                    BigDecimal ageLimitForJuniorPass = systemInfoDao.getChildPassAgeLimit(city, "junior pass");
                    String turningAdult =
                            timeManager.find_nth_birthday(user, ageLimitForJuniorPass.intValue());
                    discountRate = systemInfoDao.getChildPassDiscountRate(city, "junior pass");
                    expirationDate = timeManager.increaseDate(turningAdult, 1);
                    break;
            }
        }

        String getExpirationDate() {
            return expirationDate;
        }

        BigDecimal getDiscountRate() {
            return discountRate;
        }
    }

//  public void addChildrenDiscount(String type, String city, String time, User user) {
//    if (systemInfoDao.getChildrenPassApplicability(city).equals(true)) {
//      //            ChildrenDiscount childrenDiscount = new ChildrenDiscount();
//      //            childrenDiscount.setExpirationDate(findExpirationDate(type, city, user));
//      //            childrenDiscount.setDiscountRate(systemInfoDao.getChildPassDiscountRate(city,
//      // type));
//      String expirationDate = findExpirationDate(type, city, user);
//      if (timeManager.isValid(time, expirationDate)) {
//        entity.ChildrenDiscount childrenDiscount = new entity.ChildrenDiscount();
//        childrenDiscount.setExpirationDate(expirationDate);
//        childrenDiscount.setDiscountRate(systemInfoDao.getChildPassDiscountRate(city, type));
//        childrenDiscountDao.save(childrenDiscount);
//      }
//    }
//     }

}