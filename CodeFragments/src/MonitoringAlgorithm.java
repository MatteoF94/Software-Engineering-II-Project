/**
 * Created by Matteo on 02/12/16.
 */
public class MonitoringAlgorithm {
    SysMonitoringInterface model;

    public MonitoringAlgorithm(Model system) {
        this.model = system;
    }

    void monitoringRequest(User user, Car car) throws ReservationAlreadyDoneException, AlreadyRequestedException {

        /**
         * Check if the user has already made a reservation
         */
        if(model.reservationExistance(user)) return;

        /**
         * Server procedure to add the monitoring of the user
         */
        // Check if the system already contains a monitoring on that car
        if(!model.monitoringExistance(car))
            model.addMonitoring(new Monitoring(car, user));
        else
            model.getMonitoringByCar(car).addUser(user);
    }
}
