package com.github.davidmihola.experiment.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

import java.util.Arrays;

import com.github.davidmihola.experiment.R;

/**
 * Created by david on 04.04.14.
 */
public class FixedGridLayout extends FrameLayout {

    private static final String TAG = "";
    private final Context context;
    private float aspect;

    private static final Table<Integer, Integer, Integer> colors = ArrayTable.create(Arrays.asList(new Integer[]{0, 1, 2}), Arrays.asList(new Integer[]{0, 1, 2}));

    static {
        colors.put(0, 0, R.color.red);
        colors.put(0, 1, R.color.blue);
        colors.put(0, 2, R.color.red);
        colors.put(1, 0, R.color.blue);
        colors.put(1, 1, R.color.blue);
        colors.put(1, 2, R.color.blue);
        colors.put(2, 0, R.color.red);
        colors.put(2, 1, R.color.blue);
        colors.put(2, 2, R.color.red);
    }

    private Table<Integer, Integer, View> childViews = ArrayTable.create(Arrays.asList(new Integer[]{0, 1, 2}), Arrays.asList(new Integer[]{0, 1, 2}));
    private int mH;
    private int mW;
    private int mRowH;
    private int mColW;

    public FixedGridLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);

        this.context = context;

        createViewsFromData();
    }

    private void createViewsFromData() {
        TableAdapter adapter = new TableAdapter(context, colors);

        for (Integer row : colors.rowKeySet()) {
            for (Integer column : colors.columnKeySet()) {
                View view = adapter.getView(row, column);
                childViews.put(row, column, view);
                addView(view, new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
        }
    }
    private void init(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FixedAspectLayout);

        String aspectStr = a.getString(R.styleable.FixedAspectLayout_aspectRatio);

        Log.d(TAG, "aspectStr = " + aspectStr);

        if (aspectStr == null) {
            aspect = 1f;
        } else {
            String[] parts = aspectStr.split(":");
            if (parts.length == 1) {
                aspect = Float.parseFloat(parts[0]);
            } else if (parts.length == 2) {
                float num = Float.parseFloat(parts[0]);
                float den = Float.parseFloat(parts[1]);
                aspect = num / den;
            } else {
                throw new IllegalArgumentException("Aspect must be either a single number or an aspect ratio defined by two numbers seperated by a column");
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (w == 0) {
            h = 0;
        } else if (w / h < aspect) {
            h = (int) (w / aspect);
        } else {
            w = (int) (h * aspect);
        }

        mH = h;
        mW = w;

       mRowH = mH / childViews.rowKeySet().size();
       mColW = mW / childViews.columnKeySet().size();

        Log.d(TAG, String.valueOf(this));
        Log.d(TAG, String.format("%d, %d, %d, %d", mH, mW, mRowH, mColW));

        for (Integer row : childViews.rowKeySet()) {
            for (Integer column : childViews.columnKeySet()) {
                View view = childViews.get(row, column);
                Log.d(TAG, "view: " + String.valueOf(view));

//                measureChildWithMargins(childViews.get(row, column),
//                        MeasureSpec.makeMeasureSpec(w / childViews.columnKeySet().size(), MeasureSpec.EXACTLY), 0,
//                        MeasureSpec.makeMeasureSpec(h / childViews.rowKeySet().size(), MeasureSpec.EXACTLY), 0
                measureChild(childViews.get(row, column),
                        MeasureSpec.makeMeasureSpec(mColW, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mRowH, MeasureSpec.EXACTLY)
                );

                Log.d(TAG, "after measuring:" + view.getMeasuredHeight());
            }
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(h, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout");

        final int rowH = mH / childViews.rowKeySet().size();
        final int colW = mW / childViews.columnKeySet().size();

        for (Integer row : childViews.rowKeySet()) {
            for (Integer column : childViews.columnKeySet()) {

                View view = childViews.get(row, column);
                Log.d(TAG, "view: " + String.valueOf(view));
                view.layout((column * colW), (row * rowH), ((column + 1) * colW), ((row + 1) * rowH));
            }
        }
    }

    private class TableAdapter {

        private final Table<Integer, Integer, Integer> data;
        private final Context context;

        private TableAdapter(Context context, Table<Integer, Integer, Integer> data) {
            this.context = context;
            this.data = data;
        }

        private View getView(Integer row, Integer column) {
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.grid_item_color, null);
            view.setBackgroundColor(data.get(row, column));

            return view;
        }
    }
}