package com.wdullaer.materialdatetimepicker.time;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public abstract class PickerLayout extends FrameLayout {

    private static final String TAG = "PickerLayout";

    protected static final int AM = TimePickerDialog.AM;
    protected static final int PM = TimePickerDialog.PM;
    protected static final int HOUR_INDEX = TimePickerDialog.HOUR_INDEX;
    protected static final int MINUTE_INDEX = TimePickerDialog.MINUTE_INDEX;
    protected static final int AMPM_INDEX = TimePickerDialog.AMPM_INDEX;
    protected int mCurrentHoursOfDay;
    protected int mCurrentMinutes;
    protected boolean mIs24HourMode;
    protected int mCurrentItemShowing;

    public PickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void setOnValueSelectedListener(OnValueSelectedListener listener);

    public abstract void initialize(Context context, TimePickerDialog timePickerDialog, int initialHoursOfDay, int initialMinutes, boolean is24HourMode);

    public int getHours() {
        return mCurrentHoursOfDay;
    }

    public int getMinutes() {
        return mCurrentMinutes;
    }

    public int getIsCurrentlyAmOrPm() {
        if (getHours() < 12) {
            return AM;
        } else if (getHours() < 24) {
            return PM;
        }
        return -1;
    }

    /**
     * Set the internal value for the hour, minute, or AM/PM.
     */
    protected void setValueForItem(int index, int value) {
        if (index == HOUR_INDEX) {
            mCurrentHoursOfDay = value;
        } else if (index == MINUTE_INDEX) {
            mCurrentMinutes = value;
        } else if (index == AMPM_INDEX) {
            if (value == AM) {
                mCurrentHoursOfDay = getHours() % 12;
            } else if (value == PM) {
                mCurrentHoursOfDay = (getHours() % 12) + 12;
            }
        }
    }

    public abstract void setTime(int hours, int minutes);

    /**
     * Set the internal value as either AM or PM, and update the AM/PM circle displays.
     *
     * @param amOrPm
     */
    public void setAmOrPm(int amOrPm) {
        setValueForItem(AMPM_INDEX, amOrPm);
    }

    /**
     * Get the item (hours or minutes) that is currently showing.
     */
    public int getCurrentItemShowing() {
        if (mCurrentItemShowing != HOUR_INDEX && mCurrentItemShowing != MINUTE_INDEX) {
            Log.e(PickerLayout.TAG, "Current item showing was unfortunately set to " + mCurrentItemShowing);
            return -1;
        }
        return mCurrentItemShowing;
    }


    /**
     * Set either minutes or hours as showing.
     *
     * @param animate True to animate the transition, false to show with no animation.
     */
    public void setCurrentItemShowing(int index, boolean animate) {
        if (index != HOUR_INDEX && index != MINUTE_INDEX) {
            Log.e(PickerLayout.TAG, "TimePicker does not support view at index " + index);
            return;
        }

        mCurrentItemShowing = index;
    }

    /**
     * Set touch input as enabled or disabled, for use with keyboard mode.
     */
    public abstract boolean trySettingInputEnabled(boolean inputEnabled);

    public interface OnValueSelectedListener {
        void onValueSelected(int pickerIndex, int newValue, boolean autoAdvance);
    }
}
