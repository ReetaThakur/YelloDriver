package com.app.yellodriver.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.app.yellodriver.util.listener.OnSwipeCompleteListener;

public class SwipeController extends AppCompatSeekBar {

    private Drawable thumb;
    private OnSwipeCompleteListener listener_forward_reverse;
    private SwipeView swipe__view;

    public SwipeController(Context context) {
        super(context);
    }

    public SwipeController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setThumb(Drawable thumb) {
        this.thumb = thumb;
        super.setThumb(thumb);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (thumb.getBounds().contains((int) event.getX(), (int) event.getY())) {
                // This fixes an issue where the parent view (e.g ScrollView) receives
                // touch events along with the SlideView
                getParent().requestDisallowInterceptTouchEvent(true);
                super.onTouchEvent(event);
            } else {
                return false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getProgress() > 85) {
                if(swipe__view.swipe_both_direction){
                    if (listener_forward_reverse != null) {
                        if (!swipe__view.swipe_reverse) {
                            swipe__view.swipe_reverse = true;
                            swipe__view.setreverse_180();
                            listener_forward_reverse.onSwipe_Forward(swipe__view);
                        }
                        else{
                            swipe__view.setreverse_0();
                            swipe__view.swipe_reverse = false;
                            listener_forward_reverse.onSwipe_Reverse(swipe__view);
                        }
                    }
                }
                else {
                    if (listener_forward_reverse != null) {
                        if (!swipe__view.swipe_reverse) {
                            listener_forward_reverse.onSwipe_Forward(swipe__view);
                        }
                        if (swipe__view.swipe_reverse) {
                            listener_forward_reverse.onSwipe_Reverse(swipe__view);
                        }
                    }
                }
            }
            getParent().requestDisallowInterceptTouchEvent(false);
            setProgress(0);
        }
        else
            super.onTouchEvent(event);

        return true;
    }

    public void setOnSwipeCompleteListener_forward_reverse(OnSwipeCompleteListener listener_forward_reverse, SwipeView swipe__view) {
        this.listener_forward_reverse = listener_forward_reverse;
        this.swipe__view = swipe__view;
    }

    @Override
    public Drawable getThumb() {
        // getThumb method was added in SDK 16 but our minSDK is 14
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return super.getThumb();
        } else {
            return thumb;
        }
    }
}
