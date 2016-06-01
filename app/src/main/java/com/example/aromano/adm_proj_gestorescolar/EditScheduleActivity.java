package com.example.aromano.adm_proj_gestorescolar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class EditScheduleActivity extends AppCompatActivity {
    static ScrollView sv_timeinfo;
    static HorizontalScrollView sv_weeksinfo;
    static boolean isActive = false;

    private ArrayList<Aula> aulasfreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classschedule);

        aulasfreq = getIntent().getParcelableArrayListExtra("aulasfreq");

        sv_timeinfo = (ScrollView) findViewById(R.id.sv_timeinfo);
        sv_weeksinfo = (HorizontalScrollView) findViewById(R.id.sv_weekdayinfo);
        LinearLayout layout_timeinfo = (LinearLayout) findViewById(R.id.layout_timeinfo);
        LinearLayout layout_weekdayinfo = (LinearLayout) findViewById(R.id.layout_weekdayinfo);
        TableLayout table_schedule = (TableLayout) findViewById(R.id.table_schedule);
        populateScheduleScaffold(layout_timeinfo, layout_weekdayinfo, table_schedule);

        if(getIntent().getParcelableArrayExtra("aulasfreq") != null) {
            populateScheduleClasses();
        }
    }

    private void populateScheduleScaffold(LinearLayout layout_timeinfo, LinearLayout layout_weekdayinfo, TableLayout table_schedule) {
        // TODO get the earliest class on the week and the latest, and populate layout_timeinfo according to min and max times
        int minTime = 8;
        int maxTime = 24;

        String[] weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};



        // TODO make it possible to set minutes
        for(int i = minTime; i <= maxTime; i++) {
            FrameLayout layout_time = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.class_schedule_timeinfo, null);
            TextView tv_time = (TextView) layout_time.findViewById(R.id.tv_time);

            String hour, time;
            if(i < 10) {
                hour = "0" + String.valueOf(i);
            } else {
                hour = String.valueOf(i);
            }

            time = hour + ":00";
            tv_time.setText(time);
            layout_timeinfo.addView(layout_time);
        }

        for(String weekday : weekdays) {
            FrameLayout layout_weekday = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.class_schedule_weekdayinfo, null);
            TextView tv_weekday = (TextView) layout_weekday.findViewById(R.id.tv_weekday);
            tv_weekday.setText(weekday);

            layout_weekdayinfo.addView(layout_weekday);
        }

        // we need to use framelayout because layoutparams actually has to be the type of the parent and not the view itself
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) table_schedule.getLayoutParams();
        // apparently if the tablelayout width is set to wrap_content it doesnt work, as tablerow's and their children dont seem to have width
        // maybe because w're using TwoDScrollView instead of a regular ScrollView
        lp.width = (int)getResources().getDimension(R.dimen.class_schedule_itemnode_width) * weekdays.length;
        table_schedule.setLayoutParams(lp);

        FrameLayout[][] itemnodesyx = new FrameLayout[maxTime-minTime+1][weekdays.length];

        for(int y = 0; y < itemnodesyx.length; y++) {
            TableRow tr_node = new TableRow(this);

            for(int x = 0; x < itemnodesyx[x].length; x++) {
                FrameLayout layout_itemnode = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.class_schedule_itemnode, null);
                final TextView tv_itemnode = (TextView) layout_itemnode.findViewById(R.id.tv_itemnode);

                // TODO maybe use the TextView reference to skip the findviewbyid afterwards? meh
                Log.d("debug", String.valueOf(x));
                itemnodesyx[y][x] = layout_itemnode;
                tr_node.addView(layout_itemnode);

                layout_itemnode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_itemnode.setText("bla");
                    }
                });

            }
            table_schedule.addView(tr_node);
        }
    }

    public void populateScheduleClasses() {

    }


    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    public static void scrollSideViews(int x, int y) {
        if((sv_timeinfo != null) && (sv_weeksinfo != null)) {
            sv_timeinfo.scrollTo(0, y);
            sv_weeksinfo.scrollTo(x, 0);
        }
    }
}
