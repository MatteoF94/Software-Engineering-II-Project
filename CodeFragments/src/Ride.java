/**
 * Created by Luca on 02/12/16.
 */
public class Ride {
    private User user;
    private CarRidePOV car;
    private int updateInterval;

    private int durationPassengers;
    private int durationOfStopBy;
    private int durationOfStopBySpecial;
    private int durationOfRide;
    
    public Ride(User user, Car car, int updateInterval) {
        this.user = user;
        this.car = car;
        this.updateInterval = updateInterval;

        durationPassengers = 0;
        durationOfStopBy = 0;
        durationOfStopBySpecial = 0;
        durationOfRide = 0;
    }

    /* Ride modification methods, overloaded to simulate the forced update (event occurred)
    or normal update (fixed timer on car expires) */
    public void increaseDurationOfStopBy() {
        durationOfStopBy += updateInterval;
    }

    public void increaseDurationOfStopBy(int realInterval) {
        durationOfStopBy += realInterval;
    }
    
    public void increaseDurationOfRide() {
        durationOfRide += updateInterval;
    }

    public void increaseDurationOfRide(int realInterval) {
        durationOfRide += realInterval;
    }

    public void increaseDurationOfPassengers() {
        durationPassengers += updateInterval;
    }

    public void increaseDurationOfPassengers(int realInterval) {
        durationPassengers += realInterval;
    }

    public void increaseDurationOfStopBySpecial() {
        durationOfStopBySpecial += updateInterval;
    }

    public void increaseDurationOfStopBySpecial(int realInterval) {
        durationOfStopBySpecial += realInterval;
    }

    /* Getter methods */
    public int getDurationPassengers() {
        return durationPassengers;
    }

    public int getDurationOfStopBy() {
        return durationOfStopBy;
    }

    public int getDurationOfRide() {
        return durationOfRide;
    }

    public int getDurationOfStopBySpecial() {
        return durationOfStopBySpecial;
    }

    // It gives just the needed information about the car, just a little few of the exposed methods...
    public CarRidePOV getCar() {
        return car;
    }
}
