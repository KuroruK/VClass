package com.example.vclasslogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class DrawingActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener {
    public static final int THUMBNAIL_SIZE = 256;

    private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int CLEAR_MENU_ID = COLOR_MENU_ID + 1;
    private static final int PIN_MENU_ID = CLEAR_MENU_ID + 1;
    public static final String TAG = "AndroidDrawing";

    private DrawingView mDrawingView;
    private Firebase mFirebaseRef; // Firebase base URL
    private Firebase mMetadataRef;
    private Firebase mSegmentsRef;
    private ValueEventListener mConnectedListener;
    private String mBoardId;
    private int mBoardWidth;
    private int mBoardHeight;
    private static int minWidth=10;
    private int midWidth=30;
    private int maxWidth=50;

    private static final int ERASER_MENU_ID = PIN_MENU_ID + 1;
    private static final int THIN_MENU_ID = ERASER_MENU_ID + 1;
    private static final int NORMAL_MENU_ID = THIN_MENU_ID + 1;
    private static final int THICK_MENU_ID = NORMAL_MENU_ID + 1;
    private String selected="Thin Pen";
    private static int selectedWidth=minWidth;
    private int colorSelected=-1;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String url = intent.getStringExtra("FIREBASE_URL");
      //  final String boardId = intent.getStringExtra("BOARD_ID");
        final String boardId = "-MOm2vvp3pHoIWP7Rh9T";
        Log.i(TAG, "Adding DrawingView on "+url+" for boardId "+boardId);
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(url);
        mBoardId = boardId;
        mMetadataRef = mFirebaseRef.child("boardmetas").child(boardId);
        mSegmentsRef = mFirebaseRef.child("boardsegments").child(mBoardId);
        mMetadataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mDrawingView != null) {
                    ((ViewGroup) mDrawingView.getParent()).removeView(mDrawingView);
                    mDrawingView.cleanup();
                    mDrawingView = null;
                }
                Map<String, Object> boardValues = (Map<String, Object>) dataSnapshot.getValue();
                if (boardValues != null && boardValues.get("width") != null && boardValues.get("height") != null) {
                    mBoardWidth = ((Long) boardValues.get("width")).intValue();
                    mBoardHeight = ((Long) boardValues.get("height")).intValue();
                    mDrawingView = new DrawingView(DrawingActivity.this, mFirebaseRef.child("boardsegments").child(boardId), mBoardWidth, mBoardHeight,selectedWidth);
                    setContentView(mDrawingView);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set up a notification to let us know when we're connected or disconnected from the Firebase servers
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(DrawingActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DrawingActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        // Clean up our listener so we don't have it attached twice.
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        if (mDrawingView != null) {
            mDrawingView.cleanup();
        }
       // this.updateThumbnail(mBoardWidth, mBoardHeight, mSegmentsRef, mMetadataRef);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // getMenuInflater().inflate(R.menu.menu_drawing, menu);

        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c').setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, CLEAR_MENU_ID, 2, "Clear").setShortcut('5', 'x');
    //    menu.add(0, PIN_MENU_ID, 3, "Keep in sync").setShortcut('6', 's').setIcon(android.R.drawable.ic_lock_lock)
    //            .setCheckable(true).setChecked(SyncedBoardManager.isSynced(mBoardId));

        menu.add(0, ERASER_MENU_ID, 4, "Eraser");
        menu.add(0, THIN_MENU_ID, 5, "Thin Pen");
        menu.add(0, NORMAL_MENU_ID, 6, "Normal Pen");
        menu.add(0, THICK_MENU_ID, 7, "Thick Pen");

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == COLOR_MENU_ID) {
            new ColorPickerDialog(this, this, 0xFFFF0000).show();
            return true;
        } else if (item.getItemId() == CLEAR_MENU_ID) {
            mDrawingView.cleanup();
            mSegmentsRef.removeValue(new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        throw firebaseError.toException();
                    }
                    mDrawingView = new DrawingView(DrawingActivity.this, mFirebaseRef.child("boardsegments").child(mBoardId), mBoardWidth, mBoardHeight,selectedWidth);
                    setContentView(mDrawingView);
                    //mDrawingView.clear();
                }
            });

            return true;
        } else if (item.getItemId() == PIN_MENU_ID) {
            SyncedBoardManager.toggle(mFirebaseRef.child("boardsegments"), mBoardId);
            item.setChecked(SyncedBoardManager.isSynced(mBoardId));
            return true;
        } else if (item.getItemId() == ERASER_MENU_ID) {
            //isErase = !isErase;
            //item.setChecked(isErase);
            selectedWidth=maxWidth;
            colorSelected=mDrawingView.getmCurrentColor();
            mDrawingView.setColor(0xFFFFFFFF);
            mDrawingView.setStrokeWidth(selectedWidth);
            selected = "Eraser";
            Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
            return true;
        }  else if (item.getItemId() == THIN_MENU_ID) {

            //isErase = !isErase;
            //item.setChecked(isErase);
            if(colorSelected!=-1)
                mDrawingView.setColor(colorSelected);
            selected = "Thin Pen";
            selectedWidth=minWidth;
            mDrawingView.setStrokeWidth(selectedWidth);
            Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
            return true;
        }  else if (item.getItemId() == NORMAL_MENU_ID) {
            //isErase = !isErase;
            //item.setChecked(isErase);
            if(colorSelected!=-1)
                mDrawingView.setColor(colorSelected);
            selected = "Normal Pen";
            selectedWidth=midWidth;
            mDrawingView.setStrokeWidth(selectedWidth);
            Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
            return true;
        }  else if (item.getItemId() == THICK_MENU_ID) {
            //isErase = !isErase;
            //item.setChecked(isErase);
            selected = "Marker";
            if(colorSelected!=-1)
                mDrawingView.setColor(colorSelected);
            selectedWidth=maxWidth;
            mDrawingView.setStrokeWidth(selectedWidth);
            Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
/*
    public static void updateThumbnail(int boardWidth, int boardHeight, Firebase segmentsRef, final Firebase metadataRef) {
        final float scale = Math.min(1.0f * THUMBNAIL_SIZE / boardWidth, 1.0f * THUMBNAIL_SIZE / boardHeight);
        final Bitmap b = Bitmap.createBitmap(Math.round(boardWidth * scale), Math.round(boardHeight * scale), Bitmap.Config.ARGB_8888);
        final Canvas buffer = new Canvas(b);

        buffer.drawRect(0, 0, b.getWidth(), b.getHeight(), DrawingView.paintFromColor(Color.WHITE, Paint.Style.FILL_AND_STROKE,selectedWidth));
        Log.i(TAG, "Generating thumbnail of " + b.getWidth() + "x" + b.getHeight());

        segmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot segmentSnapshot : dataSnapshot.getChildren()) {
                    Segment segment = segmentSnapshot.getValue(Segment.class);
                    buffer.drawPath(
                            DrawingView.getPathForPoints(segment.getPoints(), scale),
                            DrawingView.paintFromColor(segment.getColor(),segment.getWidth())
                    );
                }
                String encoded = encodeToBase64(b);
                metadataRef.child("thumbnail").setValue(encoded, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.e(TAG, "Error updating thumbnail", firebaseError.toException());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
*/
    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = com.firebase.client.utilities.Base64.encodeBytes(b);

        return imageEncoded;
    }
    public static Bitmap decodeFromBase64(String input) throws IOException {
        byte[] decodedByte = com.firebase.client.utilities.Base64.decode(input);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void colorChanged(int newColor) {
        mDrawingView.setColor(newColor);
    }
}
