package edu.sdsmt.pq2_johnson_na;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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

    private void init(Context context){
        mainActivity = (MainActivity)getContext();

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
        canvas.drawRect(0,0, width, height, backGroundPaint);

        //mark A area
        textPaint.setColor(Color.BLACK);
        canvas.drawText(mainActivity.getString(R.string.x),
                textHeight / 2.0f, textHeight, textPaint);

        //mark B area
        backGroundPaint.setColor(Color.GRAY);
        canvas.drawRect(width * 0.66f,0, width, height, backGroundPaint);
        canvas.drawText(mainActivity.getString(R.string.y),
                width * 0.66f + textHeight / 2.0f, height * 0.33f + textHeight, textPaint);

        //mark C area
        backGroundPaint.setColor(Color.BLACK);
        textPaint.setColor(Color.WHITE);
        canvas.drawRect(width * 0.66f,0, width, height * 0.33f, backGroundPaint);
        canvas.drawText(mainActivity.getString(R.string.z),
                width * 0.66f + textHeight / 2.0f, textHeight, textPaint);
    }

    /**
     * A helper function to determine if a motion event is in the X area.
     * @param event the motion event
     * @param id which id in the motion event to check
     * @return true if in the X area
     */
    private boolean inXArea(MotionEvent event, int id){
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            if( id == event.getPointerId(i)){
                float x = event.getX(i);
                float y = event.getY(i);
                if(x < getWidth()*0.66f)
                    return true;
            }
        }
        return false;
    }

    /**
     * A helper function to determine if a motion event is in the Y area.
     * @param event the motion event
     * @param id which id in the motion event to check
     * @return true if in the Y area
     */
    private boolean inYArea(MotionEvent event, int id){
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            if( id == event.getPointerId(i)) {
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
     * @param event the motion event
     * @param id which id in the motion event to check
     * @return true if in the Z area
     */
    private boolean inZArea(MotionEvent event, int id){
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            if( id == event.getPointerId(i)) {
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

        //TODO x4
        return false;
    }
}
