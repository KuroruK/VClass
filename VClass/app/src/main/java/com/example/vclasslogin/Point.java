// CODE REFERENCE : https://github.com/googlearchive/AndroidDrawing
// Code modified to fit our requirements

package com.example.vclasslogin;

public class Point {
    //point class for whiteboard lines
    int x;
    int y;

    // Required default constructor for Firebase serialization / deserialization
    @SuppressWarnings("unused")
    private Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
