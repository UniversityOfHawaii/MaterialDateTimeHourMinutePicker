package com.wdullaer.materialdatetimepicker.time;

import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public abstract class PickerLayout extends FrameLayout {

    protected static final int ENABLE_PICKER_INDEX = OnValueSelectedListener.ENABLE_PICKER_INDEX;
    private static final String TAG = "PickerLayout";

    protected static final int PM = TimePickerDialog.PM;
    protected static final int AM = TimePickerDialog.AM;
    protected static final int HOUR_INDEX = OnValueSelectedListener.HOUR_INDEX;
    protected static final int MINUTE_INDEX = OnValueSelectedListener.MINUTE_INDEX;
    protected static final int AMPM_INDEX = TimePickerDialog.AMPM_INDEX;
    protected final int TAP_TIMEOUT;
    protected int mCurrentHoursOfDay;
    protected int mCurrentMinutes;
    protected boolean mIs24HourMode;
    protected int mCurrentItemShowing;
    protected boolean mInputEnabled;
    protected float mDownX;
    protected float mDownY;
    protected int mLastValueSelected;
    protected boolean mDoingMove;
    protected boolean mDoingTouch;
    protected TimePickerController mController;
    protected Handler mHandler = new Handler();
    protected OnValueSelectedListener mListener;
    protected AnimatorSet mTransition;
    protected boolean mTimeInitialized;

    public PickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInputEnabled = true;
        mLastValueSelected = -1;
        mDoingMove = false;
        TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
        mTimeInitialized = false;
    }

    public void setOnValueSelectedListener(OnValueSelectedListener listener) {
        mListener = listener;
    }

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
        String KEY_MINUTE = "minute";
        String KEY_TITLE = "dialog_title";
        String KEY_CURRENT_ITEM_SHOWING = "current_item_showing";
        String KEY_IN_KB_MODE = "in_kb_mode";
        String KEY_DARK_THEME = "dark_theme";
        String KEY_ACCENT = "accent";
        String KEY_VIBRATE = "vibrate";
        String KEY_DISMISS = "dismiss";
        int HOUR_INDEX = 0;
        int MINUTE_INDEX = 1;
        // Also NOT a real index, just used for keyboard mode.
        int ENABLE_PICKER_INDEX = 3;
        // Delay before starting the pulse animation, in ms.
        int PULSE_ANIMATOR_DELAY = 300;

        void onValueSelected(int pickerIndex, int newValue, boolean autoAdvance);
    }
}
