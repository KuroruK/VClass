// CODE REFERENCE : https://github.com/googlearchive/AndroidDrawing
// Code modified to fit our requirements

package com.example.vclasslogin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrawingView extends View {

    public static final int PIXEL_SIZE = 4;

    private Paint mPaint;
    private int mLastX;
    private int mLastY;
    private Canvas mBuffer;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Firebase mFirebaseRef;
    private ChildEventListener mListener;
    private int mCurrentColor = 0xFFFF0000;
    private Path mPath;
    private Set<String> mOutstandingSegments;
    private Segment mCurrentSegment;
    private float mScale = 1.0f;
    private int mCanvasWidth;
    private int mCanvasHeight;

    private int minWidth = 10;
    private int midWidth = 30;
    private int maxWidth = 50;
    private int selectedWidth = minWidth;
    private String userType;

    public DrawingView(Context context, Firebase ref) {
        this(context, ref, 1.0f);
    }

    public DrawingView(Context context, Firebase ref, int width, int height, int strokeWidth, String user_type) {
        this(context, ref);
        this.setBackgroundColor(Color.DKGRAY);
        mCanvasWidth = width;
        mCanvasHeight = height;
        this.selectedWidth = strokeWidth;
        userType = user_type;
    }

    public DrawingView(Context context, Firebase ref, float scale) {
        super(context);

        mOutstandingSegments = new HashSet<String>();
        mPath = new Path();
        this.mFirebaseRef = ref;
        this.mScale = scale;

        mListener = ref.addChildEventListener(new ChildEventListener() {
            /**
             * @param dataSnapshot The data we need to construct a new Segment
             * @param previousChildName Supplied for ordering, but we don't really care about ordering in this app
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String name = dataSnapshot.getKey();
                // To prevent lag, we draw our own segments as they are created. As a result, we need to check to make
                // sure this event is a segment drawn by another user before we draw it
                if (!mOutstandingSegments.contains(name)) {
                    // Deserialize the data into our Segment class
                    Segment segment = dataSnapshot.getValue(Segment.class);
                    drawSegment(segment, paintFromColor(segment.getColor(), segment.getWidth()));
                    // Tell the view to redraw itself
                    invalidate();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // No-op
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // No-op
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // No-op
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(selectedWidth);// width of current paint stroke

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    //set width of a line/stroke
    public void setStrokeWidth(int width) {
        mPaint.setStrokeWidth(width);
        selectedWidth = width;
        if (mCurrentSegment != null)
            mCurrentSegment.setWidth(selectedWidth);
    }

    public void cleanup() {
        mFirebaseRef.removeEventListener(mListener);
    }

    //return current color selected for pen
    public int getmCurrentColor() {
        return mCurrentColor;
    }

    //set color for pen
    public void setColor(int color) {
        mCurrentColor = color;
        mPaint.setColor(color);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        mScale = Math.min(1.0f * w / mCanvasWidth, 1.0f * h / mCanvasHeight);

        mBitmap = Bitmap.createBitmap(Math.round(mCanvasWidth * mScale), Math.round(mCanvasHeight * mScale), Bitmap.Config.ARGB_8888);
        mBuffer = new Canvas(mBitmap);
        Log.i("AndroidDrawing", "onSizeChanged: created bitmap/buffer of " + mBitmap.getWidth() + "x" + mBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.DKGRAY);
        canvas.drawRect(0, 0, mBitmap.getWidth(), mBitmap.getHeight(), paintFromColor(Color.WHITE, Paint.Style.FILL_AND_STROKE, minWidth));

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        canvas.drawPath(mPath, mPaint);
    }

    //paint a line with a color and width
    public static Paint paintFromColor(int color, int width) {
        return paintFromColor(color, Paint.Style.STROKE, width);
    }

    public static Paint paintFromColor(int color, Paint.Style style, int width) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setColor(color);
        p.setStyle(style);

        p.setStrokeWidth(width);//store segment width
        return p;
    }


    public static Path getPathForPoints(List<Point> points, double scale) {
        Path path = new Path();
        scale = scale * PIXEL_SIZE;
        Point current = points.get(0);
        path.moveTo(Math.round(scale * current.x), Math.round(scale * current.y));
        Point next = null;
        for (int i = 1; i < points.size(); ++i) {
            next = points.get(i);
            path.quadTo(
                    Math.round(scale * current.x), Math.round(scale * current.y),
                    Math.round(scale * (next.x + current.x) / 2), Math.round(scale * (next.y + current.y) / 2)
            );
            current = next;
        }
        if (next != null) {
            path.lineTo(Math.round(scale * next.x), Math.round(scale * next.y));
        }
        return path;
    }

    //draw a segment for a line
    private void drawSegment(Segment segment, Paint paint) {
        if (mBuffer != null) {
            mBuffer.drawPath(getPathForPoints(segment.getPoints(), mScale), paint);
        }
    }

    //function for when finger is placed on screen
    private void onTouchStart(float x, float y) {
        //reset line path
        mPath.reset();

        //place the current coordinates as start of path
        mPath.moveTo(x, y);

        //new line segment for line
        mCurrentSegment = new Segment(mCurrentColor);
        mCurrentSegment.setWidth(selectedWidth);
        mLastX = (int) x / PIXEL_SIZE;
        mLastY = (int) y / PIXEL_SIZE;

        //add the points for the line segment
        mCurrentSegment.addPoint(mLastX, mLastY);
    }

    //function for when finger is moved on screen
    private void onTouchMove(float x, float y) {

        int x1 = (int) x / PIXEL_SIZE;
        int y1 = (int) y / PIXEL_SIZE;

        float dx = Math.abs(x1 - mLastX);
        float dy = Math.abs(y1 - mLastY);

        //adds the current moved coordinates to the path of the line
        if (dx >= 1 || dy >= 1) {
            mPath.quadTo(mLastX * PIXEL_SIZE, mLastY * PIXEL_SIZE, ((x1 + mLastX) * PIXEL_SIZE) / 2, ((y1 + mLastY) * PIXEL_SIZE) / 2);
            mLastX = x1;
            mLastY = y1;

            //add points to current line segment
            mCurrentSegment.addPoint(mLastX, mLastY);
        }
    }

    //function for when finger is lifted off of screen
    private void onTouchEnd() {

        mPath.lineTo(mLastX * PIXEL_SIZE, mLastY * PIXEL_SIZE);
        mBuffer.drawPath(mPath, mPaint);
        mPath.reset();

        //push the segment to firebase to be stored
        Firebase segmentRef = mFirebaseRef.push();
        final String segmentName = segmentRef.getKey();
        mOutstandingSegments.add(segmentName);

        // create a scaled version of the segment, so that it matches the size of the board
        Segment segment = new Segment(mCurrentSegment.getColor());
        for (Point point : mCurrentSegment.getPoints()) {
            segment.addPoint((int) Math.round(point.x / mScale), (int) Math.round(point.y / mScale));
        }
        segment.setWidth(mCurrentSegment.getWidth());

        // Save our segment into Firebase. This will let other clients see the data and add it to their own canvases.
        // Also make a note of the outstanding segment name so we don't do a duplicate draw in our onChildAdded callback.
        // We can remove the name from mOutstandingSegments once the completion listener is triggered, since we will have
        // received the child added event by then.
        segmentRef.setValue(segment, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError error, Firebase firebaseRef) {
                if (error != null) {
                    Log.e("AndroidDrawing", error.toString());
                    throw error.toException();
                }
                mOutstandingSegments.remove(segmentName);
            }
        });
    }

    //manages different types of touch events, finger placed, moved and removed from screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (userType.equals("student"))
            return false;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                onTouchEnd();
                invalidate();
                break;
        }
        return true;
    }

}
