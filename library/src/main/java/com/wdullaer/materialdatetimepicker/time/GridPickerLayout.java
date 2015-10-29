package com.wdullaer.materialdatetimepicker.time;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.wdullaer.materialdatetimepicker.R;
import com.wdullaer.materialdatetimepicker.Utils;

public class GridPickerLayout extends PickerLayout implements OnTouchListener {

    private static final String TAG = "GridPickerLayout";

    private final Context context;
    private final AttributeSet attrs;
    private RecyclerView mRecyclerView;
    private GridAdapter mAdapter;
    private View rootView;

    public GridPickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        this.context = context;
        this.attrs = attrs;
    }

    @Override
    public void initialize(Context context, TimePickerDialog timePickerDialog, int initialHour, int initialMinute, boolean is24HourMode) {
        rootView = inflate(getContext(), R.layout.grid_picker_layout, this);
        mController = timePickerDialog;

        //We have to calculate the darker pressed version of the accent color in code so
        //we modify the colors here and give them the the GridAdapter to set on the state list
        //for each button.
        int accentColor = mController.getAccentColor();
        int accentColorDarker = Utils.darkenColor(accentColor);

        GradientDrawable selectedNumberDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.mdtp_number_button_selected);
        selectedNumberDrawable.setColor(accentColor);

        GradientDrawable pressedNumberDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.mdtp_number_button_pressed);
        pressedNumberDrawable.setColor(accentColorDarker);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        final int[] hours = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        mAdapter = new GridAdapter(pressedNumberDrawable, selectedNumberDrawable, hours, initialHour, new GridAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                mController.tryVibrate();
                int value = hours[position];
                setValueForItem(getCurrentItemShowing(), value);
                mListener.onValueSelected(getCurrentItemShowing(), value, true);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
