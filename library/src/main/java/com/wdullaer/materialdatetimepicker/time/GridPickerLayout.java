package com.wdullaer.materialdatetimepicker.time;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private GridAdapter mHourAdapter;
    private GridAdapter mMinuteAdapter;
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

        ColorStateList textColor = ContextCompat.getColorStateList(context, R.color.mdtp_number_button_color_selector);

        GradientDrawable defaultNumberDrawable = null;
        if(timePickerDialog.isThemeDark()) {
            defaultNumberDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.mdtp_number_button_default);
            defaultNumberDrawable.setColor(getResources().getColor(R.color.mdtp_circle_background_dark_theme));
            textColor = ContextCompat.getColorStateList(context, R.color.mdtp_number_button_color_selector_dark);
        }

        GradientDrawable selectedNumberDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.mdtp_number_button_selected);
        selectedNumberDrawable.setColor(accentColor);

        GradientDrawable pressedNumberDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.mdtp_number_button_pressed);
        pressedNumberDrawable.setColor(accentColorDarker);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        final int[] hours = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        mHourAdapter = new GridAdapter(defaultNumberDrawable, pressedNumberDrawable, selectedNumberDrawable, textColor, hours, initialHour, new GridAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                mController.tryVibrate();
                int value = hours[position];
                setValueForItem(getCurrentItemShowing(), value);
                mListener.onValueSelected(getCurrentItemShowing(), value, true);
            }
        });
        mRecyclerView.setAdapter(mHourAdapter);


        final int[] minutes = {0, 15, 30, 45};
        mMinuteAdapter = new GridAdapter(defaultNumberDrawable, pressedNumberDrawable, selectedNumberDrawable, textColor, minutes, initialMinute, new GridAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                mController.tryVibrate();
                int value = minutes[position];
                setValueForItem(getCurrentItemShowing(), value);
                mListener.onValueSelected(getCurrentItemShowing(), value, true);
            }
        });

        // Initialize the currently-selected hour and minute.
        setValueForItem(HOUR_INDEX, initialHour);
        setValueForItem(MINUTE_INDEX, initialMinute);

        mTimeInitialized = true;
    }

    @Override
    public void setTime(int hours, int minutes) {
        throw new UnsupportedOperationException("Keyboard input mode is not supported.");
    }

    @Override
    public boolean trySettingInputEnabled(boolean inputEnabled) {
        throw new UnsupportedOperationException("Keyboard input mode is not supported.");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //RecyclerView will handle everything.
        return false;
    }

    /**
     * Set either minutes or hours as showing.
     *
     * @param animate True to animate the transition, false to show with no animation.
     */
    @Override
    public void setCurrentItemShowing(int index, boolean animate) {
        int lastIndex = getCurrentItemShowing();
        super.setCurrentItemShowing(index, animate);

        if (index != lastIndex) {
            if (index == MINUTE_INDEX) {
                mRecyclerView.setAdapter(mMinuteAdapter);
                mMinuteAdapter.notifyDataSetChanged();
            } else if (index == HOUR_INDEX) {
                mRecyclerView.setAdapter(mHourAdapter);
                mHourAdapter.notifyDataSetChanged();
            }
        }
    }
}
