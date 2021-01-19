package pl.edu.agh.gg.model;

import java.util.Objects;

public final class Coordinates {

    private static final double OFFSET_X = 0.8;
    private static final double OFFSET_Y = -1.5;
    private static final double EPSILON = 0.0005;

    private double x;
    private double y;
    private double originalX;
    private double originalY;
    private double level;

    public static Coordinates createCoordinatesWithOffset(double x, double y, double level) {
        return new Coordinates(x + level * OFFSET_X, y + level * OFFSET_Y, level);
    }

    public static Coordinates createCoordinatesWithoutOffset(double x, double y, double level) {
        return new Coordinates(x, y, level);
    }

    public Coordinates(double x, double y, double level) {
        this.x = x;
        this.y = y;
        this.level = level;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getOriginalX() {
        return originalX;
    }

    public double getOriginalY() {
        return originalY;
    }

    public void setOriginalX(double originalX) {
        this.originalX = originalX;
    }

    public void setOriginalY(double originalY) {
        this.originalY = originalY;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double distance(Coordinates rhs) {
        return Coordinates.distance(this, rhs);
    }

    public static double distance(Coordinates p1, Coordinates p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Coordinates middlePoint(Coordinates rhs) {
        return Coordinates.middlePoint(this, rhs);
    }

    public static Coordinates middlePoint(Coordinates p1, Coordinates p2) {
        double xs = p1.getX() + p2.getX();
        double ys = p1.getY() + p2.getY();
        return new Coordinates(xs / 2d, ys / 2d, p1.level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates coordinates = (Coordinates) o;
        return ((((originalX != 0 && originalY != 0) || (coordinates.originalX != 0 && coordinates.originalY != 0))
                && ((Math.abs(coordinates.originalX - originalX) < EPSILON && Math.abs(coordinates.originalY - originalY) < EPSILON)
                || (Math.abs(coordinates.originalX - x) < EPSILON && Math.abs(coordinates.originalY - y) < EPSILON)
                || (Math.abs(coordinates.x - originalX) < EPSILON && Math.abs(coordinates.y - originalY) < EPSILON)))
                || (Math.abs(coordinates.x - x) < EPSILON && Math.abs(coordinates.y - y) < EPSILON))
                && coordinates.level == level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, level);
    }

    @Override
    public String toString() {
        return String.format("new Coordinates(%s,%s,%s)", x, y, level);
    }
}
