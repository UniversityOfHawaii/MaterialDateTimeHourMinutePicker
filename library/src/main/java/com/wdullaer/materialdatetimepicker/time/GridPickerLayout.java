package com.wdullaer.materialdatetimepicker.time;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wdullaer.materialdatetimepicker.R;

public class GridPickerLayout extends PickerLayout implements View.OnTouchListener {

    private final Context context;
    private final AttributeSet attrs;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomAdapter mAdapter;

    public GridPickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
    }

    @Override
    public void setOnValueSelectedListener(OnValueSelectedListener listener) {

    }

    @Override
    public void initialize(Context context, TimePickerDialog timePickerDialog, int initialHoursOfDay, int initialMinutes, boolean is24HourMode) {
        inflate(getContext(), R.layout.grid_picker_layout, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        //mLayoutManager = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        setRecyclerViewLayoutManager();

        int[] hours = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        mAdapter = new CustomAdapter(hours);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void setTime(int hours, int minutes) {

    }

    @Override
    public boolean trySettingInputEnabled(boolean inputEnabled) {
        throw new UnsupportedOperationException("Keyboard input mode is not supported.");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
