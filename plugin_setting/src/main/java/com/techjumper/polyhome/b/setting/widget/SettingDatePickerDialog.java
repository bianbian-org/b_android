package com.techjumper.polyhome.b.setting.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.techjumper.polyhome.b.setting.R;

/**
 * Created by kevin on 16/5/31.
 */
public class SettingDatePickerDialog extends DatePickerDialog{

    public SettingDatePickerDialog(Context context, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, R.style.Dialog, listener, year, monthOfYear, dayOfMonth);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setAttributes(params);
    }
}
