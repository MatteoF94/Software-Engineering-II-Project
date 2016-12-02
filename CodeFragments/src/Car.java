/**
 * Created by Matteo on 02/12/16.
 */
public class Car {
    private String serialID;

    public Car(String serialID) {
        this.serialID = serialID;
    }

    public String getSerialID() {
        return serialID;
    }

    @Override
    public boolean equals(Object obj) {
        Car car = (Car) obj;
        if(car.serialID == serialID) return true;
        else return false;
    }
}
