package org.Domain;

public class Coordinate {
    // Overview: The Coordinate class in the org.Domain package is designed to represent a two-dimensional
    // coordinate with x and y values. It provides basic functionality to get and set these coordinates
    // and is used in movement mechanics and positioning of our domain objects.

    // RI(c): 0 ≤ c.x ≤ 1000 && 0 ≤ c.y ≤ 500 && c.x, c.y is of type integer
    // AF(c): A Coordinate object c is represented as a point (c.x, c.y) where 0 ≤ c.x ≤ 1000 and 0 ≤ c.y ≤ 500.
    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return this.getX() + "," + this.getY();
    }

    public boolean repOk() {
        return (x >= 0 && x <= 1000) && (y >= 0 && y <= 500);
    }

}
