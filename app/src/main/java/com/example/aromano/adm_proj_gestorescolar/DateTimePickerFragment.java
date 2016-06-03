package com.example.aromano.adm_proj_gestorescolar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by aRomano on 03/06/2016.
 */
public class DateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Calendar calendar;

    public interface DateTimeDialogListener {
        void onFinishDateTimeDialog(Calendar calendar);
    }

    public static DateTimePickerFragment newInstance(int[] datetime) {
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        Bundle args = new Bundle();
        args.putIntArray("datetime", datetime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        calendar = Calendar.getInstance();

        if(getArguments() != null) {
            int[] datetime = getArguments().getIntArray("datetime");
            calendar.set(datetime[0], datetime[1], datetime[2], datetime[3], datetime[4]);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // https://code.google.com/p/android/issues/detail?id=34860 known issue, this is called twice
        if (view.isShown()) {
            calendar.set(year, monthOfYear, dayOfMonth);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hours, minutes, DateFormat.is24HourFormat(getActivity()));
            timePickerDialog.show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // https://code.google.com/p/android/issues/detail?id=34860 known issue, this is called twice
        if (view.isShown()) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(year, month, day, hourOfDay, minute);
            sendBackResult(calendar);
        }
    }

    public void sendBackResult(Calendar calendar) {
        DateTimeDialogListener listener = (DateTimeDialogListener) getTargetFragment();
        listener.onFinishDateTimeDialog(calendar);
        dismiss();
    }
}
