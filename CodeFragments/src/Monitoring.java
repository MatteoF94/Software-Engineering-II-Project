import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 02/12/16.
 */
public class Monitoring {
    private Car monitoredCar;
    private List<User> monitoringUsers;

    public Monitoring(Car monitoredCar, User firstMonitorer) {
        this.monitoredCar = monitoredCar;
        monitoringUsers = new ArrayList<>();
        monitoringUsers.add(firstMonitorer);
    }

    public Car getCar() {
        String monitoredCarId = monitoredCar.getSerialID();
        return new Car(monitoredCarId);
    }

    public void addUser(User newMonitorer) throws AlreadyRequestedException {
        if(monitoringUsers.contains(newMonitorer)) throw new AlreadyRequestedException();
        else monitoringUsers.add(newMonitorer);
    }

    public void removeUser(User oldMonitorer) {
        monitoringUsers.remove(oldMonitorer);
    }

    public boolean isMonitoring(User doubtedMonitorer) {
        return monitoringUsers.contains(doubtedMonitorer);
    }
}
