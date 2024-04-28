package org.Domain;

public class Vector {
    private double x, y;
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public Vector scale(double scalar) {
        return new Vector(x * scalar, y * scalar);
    }

    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector subtract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }
}
