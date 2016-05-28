package com.example.aromano.adm_proj_gestorescolar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassScheduleFragment extends Fragment {
    static ScrollView sv_timeinfo;
    static HorizontalScrollView sv_weeksinfo;

    public ClassScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_schedule_v3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ClassScheduleFragment.sv_timeinfo = (ScrollView) view.findViewById(R.id.sv_timeinfo);
        ClassScheduleFragment.sv_weeksinfo = (HorizontalScrollView) view.findViewById(R.id.sv_weekdayinfo);

        LinearLayout layout_timeinfo = (LinearLayout) view.findViewById(R.id.layout_timeinfo);
        LinearLayout layout_weekdayinfo = (LinearLayout) view.findViewById(R.id.layout_weekdayinfo);

        populateScheduleInfos(layout_timeinfo, layout_weekdayinfo);
    }

    private void populateScheduleInfos(LinearLayout layout_timeinfo, LinearLayout layout_weekdayinfo) {
        // TODO get the earliest class on the week and the latest, and populate layout_timeinfo according to min and max times
        float minTime = 8f;
        float maxTime = 24f;
        float timeIncrements = 1f;

        // TODO make it possible to change timeincrements to fully work with any class time length
        for(float i = minTime; i <= maxTime; i += timeIncrements) {
            FrameLayout layout_time = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.class_schedule_timeinfo, null);
            TextView tv_time = (TextView) layout_time.findViewById(R.id.tv_time);

            String hour, minutes, time;
            if(i < 10) {
                hour = "0" + String.valueOf((int)i);
            } else {
                hour = String.valueOf((int)i);
            }

            if(i % 1 == 0) {
                minutes = "00";
            } else {
                minutes = "30";
            }

            time = hour + ":" + minutes;

            tv_time.setText(time);

            layout_timeinfo.addView(layout_time);
        }


        // TODO check if theres classes on saturdays and sundays and remove from weekdays if thats the case
        String[] weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for(String weekday : weekdays) {
            FrameLayout layout_weekday = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.class_schedule_weekdayinfo, null);
            TextView tv_weekday = (TextView) layout_weekday.findViewById(R.id.tv_weekday);
            tv_weekday.setText(weekday);

            layout_weekdayinfo.addView(layout_weekday);
        }
    }





    public static void scrollSideViews(int x, int y) {
        if((sv_timeinfo != null) && (sv_weeksinfo != null)) {
            sv_timeinfo.scrollTo(0, y);
            sv_weeksinfo.scrollTo(x, 0);
        }
    }
}
