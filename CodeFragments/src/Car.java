/**
 * Created by Matteo on 02/12/16.
 */
public class Car implements CarRidePOV{
    private String serialID;
    private Position position;

    private float batteryCharge;
    private boolean isPlugged;

    public Car(String serialID, Position position) {
        this.serialID = serialID;
        this.position = position;
        batteryCharge = 100;
    }

    public String getSerialID() {
        return serialID;
    }

    public void changePosition(Position position) {
        int newX = position.getX();
        int newY = position.getY();
        int newZ = position.getZ();
        this.position.changePosition(newX, newY, newZ);
    }

    public void changeCurrentCharge(int charge) {
        batteryCharge = charge;
    }

    public float getBatteryCharge() {
        return batteryCharge;
    }

    public void plug() {
        isPlugged = true;
    }

    public void unplug() {
        isPlugged = false;
    }

    public boolean isPlugged() {
        return isPlugged;
    }

    public Position getPosition() {
        Position position = new Position();
        position.changePosition(this.position.getX(), this.position.getY(), this.position.getZ());
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        Car car = (Car) obj;
        if(car.serialID == serialID) return true;
        else return false;
    }
}
