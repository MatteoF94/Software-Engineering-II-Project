import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Matteo on 02/12/16.
 */
public class MonitoringTest {
    private MonitoringAlgorithm algorithm;
    private Model mo0;
    private User u0, u1;
    private Car c0,c1;

    @Before
    public void setup() {
        mo0 = new Model();
        algorithm = new MonitoringAlgorithm(mo0);

        u0 = new User("Lilith","gravure", "1152");
        u1 = new User("Mitch", "wife", "2333");
        c0 = new Car("LD111");
        c1 = new Car("LS122");
    }

    @Test public void correctRequest() throws ReservationAlreadyDoneException, AlreadyRequestedException {
        algorithm.monitoringRequest(u0,c0);
        assertTrue(mo0.getMonitoringByCar(c0).isMonitoring(u0));
        algorithm.monitoringRequest(u1,c0);
        assertTrue(mo0.getMonitoringByCar(c0).isMonitoring(u1));
        algorithm.monitoringRequest(u1,c1);
        assertTrue(mo0.getMonitoringByCar(c1).isMonitoring(u1));
    }


    @Test (expected = ReservationAlreadyDoneException.class)
    public void userWithReservation() throws ReservationAlreadyDoneException, AlreadyRequestedException {
        mo0.addReservation(new Reservation(c0,u0));
        algorithm.monitoringRequest(u1,c1);
        algorithm.monitoringRequest(u0,c1);
    }

    @Test (expected = AlreadyRequestedException.class)
    public void alreadyMonitoring() throws AlreadyRequestedException, ReservationAlreadyDoneException {
        algorithm.monitoringRequest(u0,c0);
        algorithm.monitoringRequest(u0,c0);
    }
}
