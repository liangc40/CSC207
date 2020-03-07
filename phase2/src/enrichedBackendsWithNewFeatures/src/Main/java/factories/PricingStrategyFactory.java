package factories;
import dao.*;
import entity.TripSegment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.TimeManager;
import strategies.ChargingStrategy;
import strategies.PricingStrategy;
import impl.CommonStrategyFeatures;
import impl.StopStrategy;
import impl.DistanceStrategy;
import impl.FixedPriceStrategy;

import exception.*;

@AllArgsConstructor
public class PricingStrategyFactory {
    @Getter
    private SystemInfoDao systemInfoDao;

    /**
     *
     * @param strategyName
     * @return
     * @throws InvalidStrategyException
     */
    public PricingStrategy createNewStrategy(String strategyName) throws InvalidStrategyException {
        if (strategyName.equalsIgnoreCase("stop strategy")) {
            return new StopStrategy(systemInfoDao);
        } else if (strategyName.equalsIgnoreCase("distance strategy")) {
            return  new DistanceStrategy(systemInfoDao);
        } else if (strategyName.equalsIgnoreCase("fixed price strategy")) {
            return new FixedPriceStrategy(systemInfoDao);
        } else {
            throw new InvalidStrategyException("Invalid strategy");
        }
    }
}