package com.example.ltmobile.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemMarginDecoration extends RecyclerView.ItemDecoration{
    private final int margin;

    public ItemMarginDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        outRect.top = margin;
        outRect.bottom = margin;
    }
}
