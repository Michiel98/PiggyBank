package com.piggebank.settingsPackage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class DatePreference extends DialogPreference {
    public DatePickerDialog datePickerDialog;

    private String date;

    public DatePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);
    }

    @Override
    public void setDialogLayoutResource(int dialogLayoutResId) {
        super.setDialogLayoutResource(dialogLayoutResId);
    }

    @Override
    protected View onCreateDialogView() {
        setDialogLayoutResource(android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);



        return null;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);


    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

    }

    public void setSummary(String newValue){
        String currentDate = newValue;
    }

}
