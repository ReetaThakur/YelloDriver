package com.app.yellodriver.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.app.yellodriver.R;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.listener.OnSwipeCompleteListener;

public class SwipeView extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {

    protected SwipeController swipe_controller;
    protected Drawable swipe_bg_drawable;
    protected Drawable thumb_bg_drawable;
    protected Drawable thumbImage;
    protected TextView swipeTextView;
    protected LayerDrawable swipeLayers;
    protected int strokeColor;
    public boolean swipe_reverse = false;
    public boolean swipe_both_direction = false;
    protected float swipeTextSize = Utils.spToPx(28, getContext());
    protected String swipeText;
    protected boolean animateSwipeText;
    protected int ThumbImageId, swipe_thumb_bg_color, swipe_bg_color, swipeTextColor;

    public SwipeView(Context context) {
        super(context);
        set_default(null, 0);
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        set_default(attrs, 0);
    }

    public SwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set_default(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwipeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        set_default(attrs, defStyleAttr);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (animateSwipeText) {
            swipeTextView.setAlpha(1 - (progress / 100f));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    void set_default(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_swipe_view, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.swipe_view_bg));
        } else {
            setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.swipe_view_bg));
        }

        swipeTextView = findViewById(R.id.sb_text);
        swipe_controller = findViewById(R.id.sb_swipe);
        swipe_controller.setOnSeekBarChangeListener(this);
        swipe_bg_drawable = getBackground();
        swipeLayers = (LayerDrawable) swipe_controller.getThumb();
        thumb_bg_drawable = swipeLayers.findDrawableByLayerId(R.id.thumb_background);

        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeButtonView,
                defStyle, defStyle);
        try {
            getAttributes(attributes);
        } finally {
            attributes.recycle();
        }
    }

    private void getAttributes(TypedArray attributes) {

        swipe_reverse = attributes.getBoolean(R.styleable.SwipeButtonView_sb_swipe_reverse, false);
        swipe_both_direction = attributes.getBoolean(R.styleable.SwipeButtonView_sb_swipe_both_direction, false);

        swipeText = attributes.getString(R.styleable.SwipeButtonView_sb_swipe_text);
        swipeTextColor = attributes.getColor(R.styleable.SwipeButtonView_sb_swipe_text_color, ContextCompat.getColor(getContext(), R.color.sb_text_color_default));
        animateSwipeText = attributes.getBoolean(R.styleable.SwipeButtonView_sb_swipe_animate_text, true);
        swipeTextSize = attributes.getDimension(R.styleable.SwipeButtonView_sb_swipe_text_size, swipeTextSize);
        swipeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, swipeTextSize);
        setText(swipeText);
        setTextColor(swipeTextColor);

//        ThumbImageId = attributes.getResourceId(R.styleable.SwipeButtonView_sb_thumb_image, R.drawable.ic_forward);
        ThumbImageId = attributes.getResourceId(R.styleable.SwipeButtonView_sb_thumb_image, R.drawable.ic_offline);
        setThumbImage(ContextCompat.getDrawable(getContext(), ThumbImageId));

        swipe_thumb_bg_color = attributes.getColor(R.styleable.SwipeButtonView_sb_thumb_bg_color, ContextCompat.
                getColor(getContext(), R.color.sb_thumb_button_color_default));
        setThumbBackgroundColor(swipe_thumb_bg_color);
        swipe_bg_color = attributes.getColor(R.styleable.SwipeButtonView_sb_swipe_bg_color, ContextCompat.
                getColor(getContext(), R.color.sb_swipe_bg_color_default));
        setSwipeBackgroundColor(swipe_bg_color);
        strokeColor = attributes.getColor(R.styleable.SwipeButtonView_sb_stroke_bg_color, ContextCompat.
                getColor(getContext(), R.color.sb_stroke_color_default));
        if (attributes.hasValue(R.styleable.SwipeButtonView_sb_stroke_bg_color)) {
            Utils.setDrawableStroke(swipe_bg_drawable, strokeColor);
        }

        if (swipe_reverse) {
            setreverse_180();
        }
    }

    public void setreverse_180() {
        setThumbImage(ContextCompat.getDrawable(getContext(), R.drawable.ic_online_reverse));
        swipe_controller.setRotation(180);
        set_text_position();
    }

    public void setreverse_0() {
        setThumbImage(ContextCompat.getDrawable(getContext(), R.drawable.ic_offline));
        swipe_controller.setRotation(0);
        set_text_position();
    }

    private void set_text_position() {
        LayoutParams params = ((LayoutParams) swipeTextView.getLayoutParams());
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        swipeTextView.setLayoutParams(params);
    }

    public void setTextColor(@ColorInt int color) {
        swipeTextView.setTextColor(color);
    }

    public void setText(CharSequence text) {
        swipeTextView.setText(text);
    }

    public void setTextSize(int size) {
        swipeTextView.setTextSize(size);
    }

    public TextView getTextView() {
        return swipeTextView;
    }

    public void setThumbImage(Drawable image) {
        swipeLayers.setDrawableByLayerId(R.id.thumb_image, image);
    }

    public void setThumbBackgroundColor(int color) {
        Utils.setDrawableColor(thumb_bg_drawable, color);
    }

    public void setSwipeBackgroundColor(int color) {
        Utils.setDrawableColor(swipe_bg_drawable, color);
    }

    public SwipeController getSlider() {
        return swipe_controller;
    }

    public void setOnSwipeCompleteListener_forward_reverse(OnSwipeCompleteListener listener_forward_reverse) {
        swipe_controller.setOnSwipeCompleteListener_forward_reverse(listener_forward_reverse, this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(enabled);
        }
        swipeLayers.setDrawableByLayerId(R.id.thumb_image, ContextCompat.getDrawable(getContext(), ThumbImageId));
        Utils.setDrawableColor(thumb_bg_drawable, swipe_thumb_bg_color);
        Utils.setDrawableColor(swipe_bg_drawable, swipe_bg_color);
    }
}