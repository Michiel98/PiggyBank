package com.piggebank.registrationPackage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private GestureDetector gestureDetector;
    private boolean enabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            if (this.gestureDetector != null)
                this.gestureDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
        }

        return false;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            this.gestureDetector.onTouchEvent(event);
            return super.onInterceptTouchEvent(event);
        }
        return false;

    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean returnEnabled(){
        return enabled;
    }
}
