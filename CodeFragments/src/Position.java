/**
 * Created by Matteo on 06/12/16.
 */
public class Position implements  FixedPosition{
    private int x,y,z;

    public Position() {
    }

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void changePosition (int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        Position pos = (Position) obj;
        return x == pos.getX() && y == pos.getY() && z == pos.getZ();
    }
}
