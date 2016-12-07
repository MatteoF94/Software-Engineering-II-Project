import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
/**
 * Created by Matteo on 07/12/16.
 */
@RunWith(value = Parameterized.class)
public class RideCostTest {
    private RideCostAlgorithm algorithm;
    private Model m0;
    private ServiceCore sc0;
    private Ride r0;
    private Car c0;
    private SafeAreas s0;
    private FixedPosition p0,p1,p2;

    private int normalTimeStep, stopByStep, passengerStep;
    private int carX, carY, carZ, carBattery;
    private boolean carPlugged;
    private float expectedCost;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {20, 3, 0, 1, 2, 3, 40, false, (float) 55.0},       //No bonuses or overcharge
                {25, 0, 13, 1, 2, 3, 33, false, (float) 67.275},    //Passenger bonus
                {10, 5, 2, 2, 2, 2, 55, false, (float) 17.52},      //High battery bonus
                {10, 5, 4, 2, 2, 2, 15, false, (float) 28.47},      //Low battery overcharge
                {10, 5, 0, 10, 10, 10, 40, false, (float) 28.47},   //Distance overcharge
                {20, 0, 9, 1, 2, 1, 35, true, (float) 41.86},       //Plugged car bonus

                {40, 0, 35, 1, 1, 1, 80, true, (float) 47.84},      //Virtuous user, all bonuses
                {40, 0, 0, 10, 10, 10, 15, false, (float) 191.36},  //Not virtuous user, all overcharges
                {40, 0, 22, 9, 4, 7, 15, true, (float) 107.64}      //Mixed ride
        });
    }

    public RideCostTest(int normalTimeStep, int stopByStep, int passengerStep,
                        int carX, int carY, int carZ, int carBattery, boolean carPlugged,
                        float expectedCost) {
        this.normalTimeStep = normalTimeStep;
        this.stopByStep = stopByStep;
        this.passengerStep = passengerStep;
        this.carX = carX;
        this.carY = carY;
        this.carZ = carZ;
        this.carBattery = carBattery;
        this.carPlugged = carPlugged;
        this.expectedCost = expectedCost;
    }

    @Before
    public void setup() {
        algorithm = new RideCostAlgorithm();
        m0 = new Model();
        sc0 = new ServiceCore(m0);
        c0 = new Car("L009", new Position());
        r0 = new Ride(null, c0, 10);

        s0 = new SafeAreas();
        p0 = new Position(10,2,7);
        p1 = new Position(1,1,1);
        p2 = new Position(3,20,15);
        s0.addSafeArea(Arrays.asList(p0,p1,p2),false);
        m0.setSafeAreas(s0);
    }

    @Test public void calculateRideCost() {
        r0.increaseDurationOfRide(normalTimeStep);
        r0.increaseDurationOfStopBy(stopByStep);
        r0.increaseDurationOfPassengers(passengerStep);
        c0.changeCurrentCharge(carBattery);
        c0.changePosition(new Position(carX, carY, carZ));
        if(carPlugged) c0.plug();
        else c0.unplug();

        assertEquals(expectedCost, algorithm.calculateRideCost(r0), 0.001);
    }
}
