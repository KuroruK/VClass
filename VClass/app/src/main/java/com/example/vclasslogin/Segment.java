
// CODE REFERENCE : https://github.com/googlearchive/AndroidDrawing
// Code modified to fit our requirements

package com.example.vclasslogin;

import java.util.ArrayList;
import java.util.List;

public class Segment {
    //this class stores the segments of a line in the form of a list of points

    private List<Point> points = new ArrayList<Point>();
    private int color;//color of the line segment
    private int width=10;//thickness of the line segment, default at 10

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
