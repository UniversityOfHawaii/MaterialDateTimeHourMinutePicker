package com.wdullaer.materialdatetimepicker.time;

import com.wdullaer.materialdatetimepicker.R;

public class HourMinutePickerDialog extends TimePickerDialog {

    public static HourMinutePickerDialog newInstance(OnTimeSetListener callback,
                                                     int hourOfDay, int minute) {
        HourMinutePickerDialog ret = new HourMinutePickerDialog();
        ret.initialize(callback, hourOfDay, minute, false);
        return ret;
    }

    protected int getLayoutResourceId() {
        return R.layout.mdtp_hour_minute_picker_dialog;
    }
}
