package com.app.yellodriver.customview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Dynamic height ListView with scrolling disabled.
 */
public class NonScrollListView extends ListView {

    public NonScrollListView(Context context) {
        super(context);
    }

    public NonScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int MAX_VALUE = 2;

        int measureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> MAX_VALUE, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, measureSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}