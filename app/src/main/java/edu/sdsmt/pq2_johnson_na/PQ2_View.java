package edu.sdsmt.pq2_johnson_na;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PQ2_View extends View {

    private MainActivity mainActivity;
    private Paint textPaint;
    private Paint backGroundPaint;
    private int textHeight;
    private int yDownCount;
    private int crossCount;
    private int releaseCount;
    private int doubleDownCount;
    private int textSizeInDPI = 80; //about half an inch


    private boolean isTouching1; // Flag to track if the first touch is active
    private boolean isTouching2; // Flag to track if the second touch is active
    private float touch1X, touch1Y; // Coordinates of the first touch position
    private float touch2X, touch2Y; // Coordinates of the second touch position


    public PQ2_View(Context context) {
        super(context);
        init(context);
    }

    public PQ2_View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PQ2_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PQ2_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public int getYDownCount() {
        return yDownCount;
    }

    public void setYDownCount(int yDownCount) {
        this.yDownCount = yDownCount;
    }

    public int getCrossCount() {
        return crossCount;
    }

    public void setCrossCount(int crossCount) {
        this.crossCount = crossCount;
    }

    public int getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(int releaseCount) {
        this.releaseCount = releaseCount;
    }

    public int getDoubleDownCount() {
        return doubleDownCount;
    }

    public void setDoubleDownCount(int doubleDownCount) {
        this.doubleDownCount = doubleDownCount;
    }

    private void init(Context context) {
        mainActivity = (MainActivity) getContext();

        backGroundPaint = new Paint();

        //setup text paint and sizing
        textPaint = new Paint();
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        textPaint.setTextSize(metric.densityDpi * textSizeInDPI / 160.0f);
        Rect bounds = new Rect();
        String x = context.getString(R.string.x);
        textPaint.getTextBounds(x, 0, x.length(), bounds);
        textHeight = bounds.height();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        //mark full area
        backGroundPaint.setColor(Color.LTGRAY);
        canvas.drawRect(0, 0, width, height, backGroundPaint);

        //mark A area
        textPaint.setColor(Color.BLACK);
        canvas.drawText(mainActivity.getString(R.string.x),
                textHeight / 2.0f, textHeight, textPaint);

        //mark B area
        backGroundPaint.setColor(Color.GRAY);
        canvas.drawRect(width * 0.66f, 0, width, height, backGroundPaint);
        canvas.drawText(mainActivity.getString(R.string.y),
                width * 0.66f + textHeight / 2.0f, height * 0.33f + textHeight, textPaint);

        //mark C area
        backGroundPaint.setColor(Color.BLACK);
        textPaint.setColor(Color.WHITE);
        canvas.drawRect(width * 0.66f, 0, width, height * 0.33f, backGroundPaint);
        canvas.drawText(mainActivity.getString(R.string.z),
                width * 0.66f + textHeight / 2.0f, textHeight, textPaint);

    }

    /**
     * A helper function to determine if a motion event is in the X area.
     *
     * @param event the motion event
     * @param id    which id in the motion event to check
     * @return true if in the X area
     */
    private boolean inXArea(MotionEvent event, int id) {
        for (int i = 0; i < event.getPointerCount(); i++) {

            // Get the pointer id
            if (id == event.getPointerId(i)) {
                float x = event.getX(i);
                float y = event.getY(i);
                if (x < getWidth() * 0.66f)
                    return true;
            }
        }
        return false;
    }

    /**
     * A helper function to determine if a motion event is in the Y area.
     *
     * @param event the motion event
     * @param id    which id in the motion event to check
     * @return true if in the Y area
     */
    private boolean inYArea(MotionEvent event, int id) {
        for (int i = 0; i < event.getPointerCount(); i++) {

            // Get the pointer id
            if (id == event.getPointerId(i)) {
                float x = event.getX(i);
                float y = event.getY(i);
                if (x > getWidth() * 0.66f && y > getHeight() * 0.33f)
                    return true;
            }
        }
        return false;
    }

    /**
     * A helper function to determine if a motion event is in the Z area.
     *
     * @param event the motion event
     * @param id    which id in the motion event to check
     * @return true if in the Z area
     */
    private boolean inZArea(MotionEvent event, int id) {
        for (int i = 0; i < event.getPointerCount(); i++) {

            // Get the pointer id
            if (id == event.getPointerId(i)) {
                float x = event.getX(i);
                float y = event.getY(i);
                if (x > getWidth() * 0.66f && y < getHeight() * 0.33f) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked(); // Get the type of action
        int pointerIndex = event.getActionIndex(); // Get the index of the pointer performing this action
        int pointerId = event.getPointerId(pointerIndex); // Get the pointer id associated with this action

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (inXArea(event, pointerId)) {
                    if (!isTouching1) {
                        isTouching1 = true;
                        touch1X = event.getX(pointerIndex);
                        touch1Y = event.getY(pointerIndex);
                    } else if (!isTouching2) {
                        isTouching2 = true;
                        touch2X = event.getX(pointerIndex);
                        touch2Y = event.getY(pointerIndex);
                        if (Math.abs(touch1X - touch2X) < getWidth() * 0.66f) {
                            // Two touches within the X area
                            mainActivity.setDoubleDownCount(mainActivity.getDoubleDownCount() + 1);
                        }
                    }
                } else if (inYArea(event, pointerId)) {
                    // Single touch in the Y area
                    mainActivity.setYDownCount(mainActivity.getYDownCount() + 1);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (inZArea(event, pointerId)) {
                    mainActivity.setYDownCount(0);
                    mainActivity.setDoubleDownCount(0);
                    // I couldn't tell if you wanted a release in Z to clear the release in X count also or not...so I made it reset it
                    mainActivity.setReleaseCount(0);
                } else if (inXArea(event, pointerId)) {
                    mainActivity.setReleaseCount(mainActivity.getReleaseCount() + 1);
                }
                if (pointerId == 0) {
                    isTouching1 = false;
                } else if (pointerId == 1) {
                    isTouching2 = false;
                }
                break;
        }

        return true;
    }


}

/* MY PREP CODE

-----------------------------------------------------------------------
Updating x / y / z touch counts


@Override
public boolean onTouchEvent(MotionEvent event) {
    int action = event.getActionMasked(); // Get the type of action

    switch (action) {
        case MotionEvent.ACTION_DOWN:
            // Check if any pointer is in X, Y, or Z areas and update counts accordingly
            if (inXArea(event, event.getActionIndex())) {
                yDownCount++;
            } else if (inYArea(event, event.getActionIndex())) {
                crossCount++;
            } else if (inZArea(event, event.getActionIndex())) {
                releaseCount++;
            }
            break;

        case MotionEvent.ACTION_POINTER_DOWN:
            // Check if any additional pointer goes down in X area and update counts
            if (inXArea(event, event.getActionIndex())) {
                doubleDownCount++;
            }
            break;

        case MotionEvent.ACTION_POINTER_UP:
        case MotionEvent.ACTION_UP:
            // Update counts when pointers go up
            if (inXArea(event, event.getActionIndex())) {
                yDownCount--;
            } else if (inYArea(event, event.getActionIndex())) {
                crossCount--;
            } else if (inZArea(event, event.getActionIndex())) {
                releaseCount--;
            }
            break;
    }

    // Redraw the view
    invalidate();

    // Return true to indicate that the event has been consumed
    return true;
}


---------------------------------------------------------------------------
 */
