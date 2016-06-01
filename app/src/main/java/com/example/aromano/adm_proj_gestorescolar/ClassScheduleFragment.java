package com.example.aromano.adm_proj_gestorescolar;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassScheduleFragment extends Fragment {
    static ScrollView sv_timeinfo;
    static HorizontalScrollView sv_weeksinfo;
    static boolean isActive = false;

    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Aula> aulasfreq;

    public static ClassScheduleFragment newInstance(Aluno aluno) {
        ClassScheduleFragment fragment = new ClassScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        fragment.setArguments(args);
        return fragment;
    }

    public ClassScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null) {
            aluno = getArguments().getParcelable("aluno");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = DBHelper.getInstance(getActivity());
        aulasfreq = db.readAulasFrequentadas(aluno);

        if(aulasfreq != null) {
            return inflater.inflate(R.layout.fragment_class_schedule, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_class_schedule_empty, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sv_timeinfo = (ScrollView) view.findViewById(R.id.sv_timeinfo);
        sv_weeksinfo = (HorizontalScrollView) view.findViewById(R.id.sv_weekdayinfo);
        LinearLayout layout_timeinfo = (LinearLayout) view.findViewById(R.id.layout_timeinfo);
        LinearLayout layout_weekdayinfo = (LinearLayout) view.findViewById(R.id.layout_weekdayinfo);
        TableLayout table_schedule = (TableLayout) view.findViewById(R.id.table_schedule);

        if(aulasfreq != null) {
            populateScheduleScaffold(layout_timeinfo, layout_weekdayinfo, table_schedule);
            populateScheduleClasses();
        } else {
            Button btn_addschedule = (Button) view.findViewById(R.id.btn_addschedule);
            btn_addschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSchedule();
                }
            });
        }
    }

    private void populateScheduleScaffold(LinearLayout layout_timeinfo, LinearLayout layout_weekdayinfo, TableLayout table_schedule) {
        // TODO get the earliest class on the week and the latest, and populate layout_timeinfo according to min and max times
        int minTime = 8;
        int maxTime = 24;
        //float timeIncrements = 1f;
        boolean saturdayClasses = false;
        boolean sundayClasses = false;


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        for (Aula aula : aulasfreq) {
            String horaentrada = aula.getHoraentrada();
            try {
                Date date = sdf.parse(horaentrada);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int horas = calendar.get(Calendar.HOUR_OF_DAY);
                if(horas < minTime) {
                    minTime = calendar.get(Calendar.HOUR_OF_DAY);
                } else if(horas > maxTime) {
                    maxTime = calendar.get(Calendar.HOUR_OF_DAY);
                }
                if(aula.getDiaSemana() == 5) {
                    saturdayClasses = true;
                } else if(aula.getDiaSemana() == 6) {
                    sundayClasses = true;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        String[] weekdays;
        if(saturdayClasses && sundayClasses) {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        } else if(saturdayClasses) {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        } else if(sundayClasses) {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Sunday"};
        } else {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        }

        // TODO make it possible to set minutes
        for(int i = minTime; i <= maxTime; i++) {
            FrameLayout layout_time = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.class_schedule_timeinfo, null);
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
            FrameLayout layout_weekday = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.class_schedule_weekdayinfo, null);
            TextView tv_weekday = (TextView) layout_weekday.findViewById(R.id.tv_weekday);
            tv_weekday.setText(weekday);

            layout_weekdayinfo.addView(layout_weekday);
        }

        FrameLayout[][] itemnodesyx = new FrameLayout[maxTime-minTime+1][weekdays.length];

        for(int y = 0; y < itemnodesyx.length; y++) {
            TableRow tr_node = new TableRow(getActivity());
            tr_node.setLayoutParams(new TableRow.LayoutParams(
                    (int)getResources().getDimension(R.dimen.class_schedule_itemnode_width),
                    (int)getResources().getDimension(R.dimen.class_schedule_itemnode_height)));
            for(int x = 0; x < itemnodesyx[x].length; x++) {
                FrameLayout layout_itemnode = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.class_schedule_itemnode, null);
                TextView tv_itemnode = (TextView) layout_itemnode.findViewById(R.id.tv_itemnode);

                // TODO maybe use the TextView reference to skip the findviewbyid afterwards? meh
                itemnodesyx[x][y] = layout_itemnode;
                tr_node.addView(layout_itemnode);
            }
            table_schedule.addView(tr_node);
        }
    }

    public void populateScheduleClasses() {

    }

    private void editSchedule() {
        Intent intent = new Intent(getActivity(), EditScheduleActivity.class);
        if(aulasfreq != null) {
            intent.putParcelableArrayListExtra("aulasfreq", aulasfreq);
        }
        startActivity(intent);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_exam_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
