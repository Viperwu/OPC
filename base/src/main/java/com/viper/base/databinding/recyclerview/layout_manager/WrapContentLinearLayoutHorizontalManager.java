package com.viper.base.databinding.recyclerview.layout_manager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;


public class WrapContentLinearLayoutHorizontalManager extends LinearLayoutManager {

    public WrapContentLinearLayoutHorizontalManager(Context context) {
        super(context);
    }

    public WrapContentLinearLayoutHorizontalManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapContentLinearLayoutHorizontalManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        //super.smoothScrollToPosition(recyclerView, state, position);
        RecyclerView.SmoothScroller smoothScroller = new SmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(HORIZONTAL);
    }


    private static class SmoothScroller extends LinearSmoothScroller{

        public SmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {


          //  return super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference);

            return (boxStart+(boxEnd-boxStart)/2) - (viewStart+(viewEnd-viewStart)/2);
        }


        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            //return super.calculateSpeedPerPixel(displayMetrics);
            return 100f/displayMetrics.densityDpi;
        }
    }



}
