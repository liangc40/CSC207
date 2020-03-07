package factories;

import dao.*;
import entity.ChildrenDiscount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import manager.TimeManager;
import manager.UserMoneyManager;
import strategies.ChargingStrategy;
import impl.CommonStrategyFeatures;
import impl.CapStrategy;
import impl.TransferStrategy;
import exception.*;

@AllArgsConstructor
public class ChargingStrategyFactory {

  @Getter private UserDao userDao;
  private CardDao cardDao;
  private SystemInfoDao systemInfoDao;
  private GeneralTripDao generalTripDao;
  private TripSegmentDao tripSegmentDao;
  private TransitPassDao transitPassDao;
  private UserMoneyManagerDao userMoneyManagerDao;
  private ChildrenDiscountDao childrenDiscountDao;
  private TimeManager timeManager;

  /**
   * A create a new Charging Strategy with name of the strategy given.
   *
   * @param strategyName
   * @return a charging strategy which could either be a transfer strategy, a cap strategy or a
   *     non-trannsfer strategy
   * @throws InvalidStrategyException
   */
  public ChargingStrategy createNewStrategy(String strategyName) throws InvalidStrategyException {
    CommonStrategyFeatures commonStrategyFeatures =
        new CommonStrategyFeatures(
            userDao,
            cardDao,
            systemInfoDao,
            timeManager,
            generalTripDao,
            transitPassDao,
            userMoneyManagerDao,
            childrenDiscountDao);
    if (strategyName.equalsIgnoreCase("Cap Strategy")) {
      return new CapStrategy(commonStrategyFeatures, generalTripDao, systemInfoDao, timeManager);
    } else if (strategyName.equalsIgnoreCase("Transfer Strategy")) {
      return new TransferStrategy(commonStrategyFeatures);
    } else {
      throw new InvalidStrategyException("Invalid strategy exception");
    }
  }
}
