package com.wdullaer.materialdatetimepicker.time;

import com.wdullaer.materialdatetimepicker.R;

public class HourMinutePickerDialog extends TimePickerDialog {

    public static HourMinutePickerDialog newInstance(OnTimeSetListener callback,
                                                     int initialHour, int initialMinute) {
        HourMinutePickerDialog ret = new HourMinutePickerDialog();
        ret.initialize(callback, initialHour, initialMinute, false);
        return ret;
    }

    protected int getLayoutResourceId() {
        return R.layout.mdtp_hour_minute_picker_dialog;
    }
}
