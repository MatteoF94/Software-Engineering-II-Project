import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 02/12/16.
 */
public class Model implements SysMonitoringInterface{
    List<Monitoring> monitorings;
    List<Reservation> reservations;

    public Model() {
        monitorings = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public void addMonitoring(Monitoring monitoring) {
        monitorings.add(monitoring);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public boolean monitoringExistance (Car monitoredCar) {
        for (Monitoring mon: monitorings) {
            if (mon.getCar().equals(monitoredCar)) return true;
        }
        return false;
    }

    public boolean reservationExistance (User monitoringUser) throws ReservationAlreadyDoneException {
        for (Reservation res: reservations) {
            if (res.getUser().equals(monitoringUser)) throw new ReservationAlreadyDoneException();
        }
        return false;
    }

    public Monitoring getMonitoringByCar(Car monitoredCar) {
        Monitoring monitoring = null;
        for (Monitoring mon: monitorings) {
            if(mon.getCar().equals(monitoredCar)) {
                monitoring = mon;
                break;
            }
        }
        return monitoring;
    }

    public List<Monitoring> retrieveMonitorings() {
        return monitorings;
    }

    public List<Reservation> retrieveReservations() {
        return reservations;
    }
}
