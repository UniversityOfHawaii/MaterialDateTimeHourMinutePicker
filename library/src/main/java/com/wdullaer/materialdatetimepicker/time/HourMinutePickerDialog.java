package com.wdullaer.materialdatetimepicker.time;

import com.wdullaer.materialdatetimepicker.R;

public class HourMinutePickerDialog extends TimePickerDialog {

    public static HourMinutePickerDialog newInstance(OnTimeSetListener callback,
                                                     int hourOfDay, int minute, boolean is24HourMode) {
        HourMinutePickerDialog ret = new HourMinutePickerDialog();
        ret.initialize(callback, hourOfDay, minute, is24HourMode);
        return ret;
    }

    protected int getLayoutResourceId() {
        return R.layout.mdtp_hour_minute_picker_dialog;
    }
}
