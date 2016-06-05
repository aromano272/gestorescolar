package com.example.aromano.adm_proj_gestorescolar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;


public class ClassTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    int starttime = -1;

    public interface ClassTimeDialogListener {
        void onFinishClassStartTimeDialog(int starttime, boolean areMinutesSet);
    }

    // apparently settargetfragment happens before default setarguments, so when we create
    // a fragment thourgh newinstance we then can no longer use settargetfragment on the returned object
    // since the method setarguments has already been called inside newInstance
    public static ClassTimePickerFragment newInstance(int starttime, Fragment targetFragment) {
        ClassTimePickerFragment fragment = new ClassTimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("starttime", starttime);
        fragment.setTargetFragment(targetFragment, 0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null) {
            starttime = getArguments().getInt("starttime");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, starttime, 0,
                DateFormat.is24HourFormat(getActivity()));
        dialog.setTitle("A que horas começa a aula?");
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // https://code.google.com/p/android/issues/detail?id=34860 known issue, this is called twice
        if (view.isShown()) {
            // TODO maybe warn the user, the minutes are not going to be used
            boolean areMinutesSet = false;
            if(minute != 0) {
                areMinutesSet = true;
            }
            starttime = hourOfDay;
            sendBackStartTimeResult(starttime, areMinutesSet);
        }
    }

    private void sendBackStartTimeResult(int starttime, boolean areMinutesSet) {
        ClassTimeDialogListener listener = (ClassTimeDialogListener) getTargetFragment();
        listener.onFinishClassStartTimeDialog(starttime, areMinutesSet);
        dismiss();
    }

}




/*
public class ClassTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    int[] starttime = null;
    int[] endtime = null;
    TimePickerDialog.OnTimeSetListener starttimeListener, endtimeListener;

    public interface ClassTimeDialogListener {
        void onFinishClassStartTimeDialog(int[] starttime);
        void onFinishClassEndTimeDialog(int[] endtime);
    }

    // apparently settargetfragment happens before default setarguments, so when we create
    // a fragment thourgh newinstance we then can no longer use settargetfragment on the returned object
    // since the method setarguments has already been called inside newInstance
    public static ClassTimePickerFragment newInstance(int[] starttime, int[] endtime, Fragment targetFragment) {
        ClassTimePickerFragment fragment = new ClassTimePickerFragment();
        Bundle args = new Bundle();
        args.putIntArray("starttime", starttime);
        args.putIntArray("endtime", endtime);
        fragment.setTargetFragment(targetFragment, 0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null) {
            starttime = getArguments().getIntArray("starttime");
            endtime = getArguments().getIntArray("endtime");
        }

        // http://stackoverflow.com/questions/3734981/multiple-datepickers-in-same-activity
        endtimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(view.isShown()) {
                    endtime[0] = hourOfDay;
                    endtime[1] = minute;
                    sendBackEndTimeResult(endtime);
                }
            }
        };

        starttimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(view.isShown()) {
                    starttime[0] = hourOfDay;
                    starttime[1] = minute;
                    sendBackStartTimeResult(starttime);
                    // seems we have to set getTargetFragment().getActivity() instead of just getActivity()
                    // because this dialog is going to be created from an instance of this same dialog, instead
                    // of being created from an instance of the AddClassDialogFragment
                    //TimePickerDialog dialog = new TimePickerDialog(getActivity(), endtimeListener, endtime[0], endtime[1], false);
                    TimePickerDialog dialog = new TimePickerDialog(getActivity(), endtimeListener,
                            endtime[0], endtime[1], DateFormat.is24HourFormat(getActivity()));
                    dialog.setTitle("A que horas acaba a aula?");
                    dialog.show();
                }
            }
        };
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // endtime should always be present, if starttime is not its because the
        // user pressed the endtime button, so we shoulnt display a second dialog
        if(starttime == null) {
            TimePickerDialog dialogend = new TimePickerDialog(getActivity(), endtimeListener, endtime[0], endtime[1],
                    DateFormat.is24HourFormat(getActivity()));
            dialogend.setTitle("A que horas acaba a aula?");
            return dialogend;
        } else {
            TimePickerDialog dialogstart = new TimePickerDialog(getActivity(), starttimeListener, starttime[0], starttime[1],
                    DateFormat.is24HourFormat(getActivity()));
            dialogstart.setTitle("A que horas começa a aula?");
            return dialogstart;
        }
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // https://code.google.com/p/android/issues/detail?id=34860 known issue, this is called twice

        if(starttime == null) {
            // dont show endtime dialog
            endtime[0] = hourOfDay;
            endtime[1] = minute;
            sendBackEndTimeResult(endtime);
        } else {
            // save starttime and show endtime dialog
            starttime[0] = hourOfDay;
            starttime[1] = minute;
            sendBackStartTimeResult(starttime);
            // seems we have to set getTargetFragment().getActivity() instead of just getActivity()
            // because this dialog is going to be created from an instance of this same dialog, instead
            // of being created from an instance of the AddClassDialogFragment
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), ClassTimePickerFragment.this, endtime[0], endtime[1], false);
            dialog.setTitle("A que horas acaba a aula?");
            dialog.show();
        }
    }

    private void sendBackStartTimeResult(int[] starttime) {
        ClassTimeDialogListener listener = (ClassTimeDialogListener) getTargetFragment();
        Log.d("debug", "listener==null: "+String.valueOf(listener == null));
        listener.onFinishClassStartTimeDialog(starttime);
        dismiss();
    }

    private void sendBackEndTimeResult(int[] endtime) {
        ClassTimeDialogListener listener = (ClassTimeDialogListener) getTargetFragment();
        listener.onFinishClassEndTimeDialog(endtime);
        dismiss();
    }
}
*/