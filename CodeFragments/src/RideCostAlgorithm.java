/**
 * Created by Matteo on 06/12/16.
 */
public class RideCostAlgorithm {
    private final float NORMAL_FEE = (float) 2.99;
    private final float STOPBY_FEE = (float) 1.39;
    private final float PASSENGER_BONUS = (float) -0.1;
    private final float HIGH_BATTERY_BONUS = (float) -0.2;
    private final float LOW_BATTERY_OVERCHARGE = (float) 0.3;
    private final float PLUGGING_BONUS = (float) -0.3;
    private final float LONG_DISTANCE_OVERCHARGE = (float) 0.3;
    
    public float calculateRideCost(Ride ride) {
        float standardCost = (ride.getDurationOfRide()-
                                ride.getDurationOfStopBy()-
                                ride.getDurationOfStopBySpecial())
                              *NORMAL_FEE;
        float stopByCost = ride.getDurationOfStopBy()*STOPBY_FEE;
        /* There is no need to consider the special stopby,
        because the fee is 0 due to the car being in a special area */

        float passengerBonusContribute = calculatePassengerBonus(ride);
        float batteryContribute = calculateBatteryBonus(ride);
        float pluggingContribute = calculatePluggingBonus(ride);
        float distanceContribute = calculateDistanceBonus(ride);

        return (standardCost+stopByCost)*
                            (1+passengerBonusContribute+batteryContribute+pluggingContribute+distanceContribute);
    }

    private float calculatePassengerBonus(Ride ride) {
        if(ride.getDurationPassengers() > ride.getDurationOfRide()/2) return PASSENGER_BONUS;
        else return 0;
    }

    private float calculateBatteryBonus(Ride ride) {
        float battery = ride.getCar().getBatteryCharge();
        if(battery >= 50.0) return HIGH_BATTERY_BONUS;
        else if(battery < 20.0) return LOW_BATTERY_OVERCHARGE;
        else return 0;
    }

    private float calculatePluggingBonus(Ride ride) {
        if(ride.getCar().isPlugged()) return PLUGGING_BONUS;
        else return 0;
    }

    private float calculateDistanceBonus(Ride ride) {
        Position carPosition = ride.getCar().getPosition();
        if(ServiceCore.isMoreThan3kmDistant(carPosition)) return LONG_DISTANCE_OVERCHARGE;
        else return 0;
    }
}
