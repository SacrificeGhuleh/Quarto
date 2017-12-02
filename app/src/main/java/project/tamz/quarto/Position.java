package project.tamz.quarto;

/**
 * Created by Richard Zvonek on 02/12/2017.
 */

public class Position {

    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(Position p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public Position get() {
        return new Position(getX(), getY());
    }
}
