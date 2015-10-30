package com.wdullaer.materialdatetimepicker.time;

import com.wdullaer.materialdatetimepicker.R;
import com.wdullaer.materialdatetimepicker.Utils;

public class HourMinutePickerDialog extends TimePickerDialog {

    public static HourMinutePickerDialog newInstance(OnTimeSetListener callback,
                                                     int initialHour, int initialMinute) {
        HourMinutePickerDialog hourMinutePickerDialog = new HourMinutePickerDialog();
        hourMinutePickerDialog.initialize(callback, initialHour, initialMinute, false);
        return hourMinutePickerDialog;
    }

    protected int getLayoutResourceId() {
        return R.layout.mdtp_hour_minute_picker_dialog;
    }

    @Override
    protected void setHour(int value, boolean announce) {
        String format;
            format = "%02d";

        CharSequence text = String.format(format, value);
        mHourView.setText(text);
        mHourSpaceView.setText(text);
        if (announce) {
            Utils.tryAccessibilityAnnounce(mTimePicker, text);
        }
    }

    @Override
    protected void updateAmPmDisplay(int amOrPm) {
        String text = "";
        mAmPmTextView.setText(text);
        mAmPmHitspace.setContentDescription(text);
    }
}
