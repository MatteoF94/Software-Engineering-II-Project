/**
 * Created by Matteo on 02/12/16.
 */
public interface SysMonitoringInterface {
    void addMonitoring(Monitoring monitoring);
    void addReservation(Reservation reservation);
    boolean monitoringExistance (Car monitoredCar);
    boolean reservationExistance (User monitoringUser) throws ReservationAlreadyDoneException;
    Monitoring getMonitoringByCar(Car monitoredCar);
}
