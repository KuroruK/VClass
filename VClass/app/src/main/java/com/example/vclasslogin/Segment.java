package com.example.vclasslogin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author greg
 * @since 6/26/13
 */
public class Segment {

    private List<Point> points = new ArrayList<Point>();
    private int color;
    private int width=10;

    // Required default constructor for Firebase serialization / deserialization
    @SuppressWarnings("unused")
    private Segment() {
    }

    public Segment(int color) {
        this.color = color;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void addPoint(int x, int y) {
        Point p = new Point(x, y);
        points.add(p);
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getColor() {
        return color;
    }
}
