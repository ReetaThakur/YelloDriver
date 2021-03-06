package com.app.yellodriver.util.spacingdecorator;


import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Item decoration to add horizontal devider spacing into Horizontal recycler view.
 */
public class HorizontalSpacingDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public HorizontalSpacingDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }

        final int itemCount = state.getItemCount();

        outRect.top = space;
        outRect.bottom = space;
        outRect.left = space;

        if (itemPosition == itemCount - 1) {
            outRect.right = space;
        } else {
            outRect.right = 0;
        }
    }
}