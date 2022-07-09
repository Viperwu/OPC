package com.viper.base.databinding.recyclerview.layout_manager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FullyLinearLayoutManager extends LinearLayoutManager {
    private int[] mMeasureDimension = new int[2];

    public FullyLinearLayoutManager(Context context) {
        super(context);
    }

    public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FullyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
    public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
        //super.onMeasure(recycler, state, widthSpec, heightSpec);

        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);
        int width = 0;
        int height = 0;
        for (int i=0;i<getItemCount();i++){
            measureScrapChild(recycler,i,View.MeasureSpec.makeMeasureSpec(i,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(i,View.MeasureSpec.UNSPECIFIED),mMeasureDimension);
            if (getOrientation()==HORIZONTAL){
                width = width+mMeasureDimension[0];
                if (i==0){
                    height = mMeasureDimension[i];
                }
            }else {
                height = height+ mMeasureDimension[1];
                if (i==0){
                    width = mMeasureDimension[0];
                }
            }
        }
        switch (widthMode){
            case View.MeasureSpec.EXACTLY:width =widthSize;;

            case View.MeasureSpec.AT_MOST:;
            case View.MeasureSpec.UNSPECIFIED:;
        }
        switch (heightMode){
            case View.MeasureSpec.EXACTLY:height =heightSize;;

            case View.MeasureSpec.AT_MOST:;
            case View.MeasureSpec.UNSPECIFIED:;
        }
        setMeasuredDimension(width,height);

    }

    private void measureScrapChild(@NonNull RecyclerView.Recycler recycler,int position,int widthSpec,int heightSpec,int[] mMeasureDimension){
        try {
            View view = recycler.getViewForPosition(0);
            if (view!=null){
                RecyclerView.LayoutParams p =(RecyclerView.LayoutParams) view.getLayoutParams();
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,getPaddingLeft()+getPaddingRight(),p.width);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,getPaddingTop()+getPaddingBottom(),p.height);
                view.measure(childWidthSpec,childHeightSpec);
                mMeasureDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                mMeasureDimension[1] = view.getHeight() + p.topMargin + p.bottomMargin;
                recycler.recycleView(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
