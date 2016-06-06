package com.example.aromano.adm_proj_gestorescolar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private DBHelper db;
    private ArrayList<Aula> aulasfreq;
    private Aluno aluno;
    private FrameLayout[][] itemnodesyx;
    private Aula[][] classnodesyx;
    private String[] weekdays;
    private int minStartTime;
    private int maxStartTime;
    private SimpleDateFormat sdf;

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
        sdf = new SimpleDateFormat("HH:mm");

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
            populateScheduleClassesArray();
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
        minStartTime = 8;
        maxStartTime = 23;
        weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
/*
        boolean saturdayClasses = false;
        boolean sundayClasses = false;

        for (Aula aula : aulasfreq) {
            int horaentrada = aula.getHoraentrada();
            if(horaentrada < minStartTime) {
                minStartTime = horaentrada;
            } else if(horaentrada > maxStartTime) {
                maxStartTime = horaentrada;
            }
            if(aula.getDiaSemana() == 5) {
                saturdayClasses = true;
            } else if(aula.getDiaSemana() == 6) {
                sundayClasses = true;
            }
        }

        if(saturdayClasses && sundayClasses) {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        } else if(saturdayClasses) {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        } else if(sundayClasses) {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Sunday"};
        } else {
            weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        }
*/
        // TODO make it possible to set minutes
        for(int i = minStartTime; i <= maxStartTime; i++) {
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

        // we need to use framelayout because layoutparams actually has to be the type of the parent and not the view itself
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) table_schedule.getLayoutParams();
        // apparently if the tablelayout width is set to wrap_content it doesnt work, as tablerow's and their children dont seem to have width
        // maybe because w're using TwoDScrollView instead of a regular ScrollView
        lp.width = (int)getResources().getDimension(R.dimen.class_schedule_itemnode_width) * weekdays.length;
        table_schedule.setLayoutParams(lp);

        itemnodesyx = new FrameLayout[maxStartTime - minStartTime + 1][weekdays.length];
        classnodesyx = new Aula[maxStartTime - minStartTime + 1][weekdays.length];

        for(int y = 0; y < itemnodesyx.length; y++) {
            TableRow tr_node = new TableRow(getActivity());

            for(int x = 0; x < itemnodesyx[x].length; x++) {
                FrameLayout layout_itemnode = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.class_schedule_itemnode, null);

                // TODO maybe use the TextView reference to skip the findviewbyid afterwards? meh
                Log.d("debug", String.valueOf(x));
                itemnodesyx[y][x] = layout_itemnode;
                tr_node.addView(layout_itemnode);
            }
            table_schedule.addView(tr_node);
        }
    }

    private void populateScheduleClassesArray() {
        for (Aula aula : aulasfreq) {
            int nodex = aula.getDiaSemana();
            int nodey = aula.getHoraentrada() - minStartTime;
            classnodesyx[nodey][nodex] = aula;
        }
    }

    private void populateScheduleClasses() {
        for (int y = 0; y < classnodesyx.length; y++) {
            for (int x = 0; x < classnodesyx[y].length; x++) {
                if (classnodesyx[y][x] != null) {
                    final Aula aula = classnodesyx[y][x];
                    TextView tv_classname = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_classname);
                    TextView tv_sala = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_sala);

                    tv_classname.setText(aula.getCadeira().getAbbr());
                    tv_sala.setText(aula.getSala());
                }
            }
        }
    }

    private void editSchedule() {
        Intent intent = new Intent(getActivity(), EditClassScheduleActivity.class);
        if(aulasfreq != null) {
            intent.putParcelableArrayListExtra("aulasfreq", aulasfreq);
        }
        intent.putExtra("aluno", aluno);
        startActivityForResult(intent, 0);
    }

    private void redraw() {
        for(int y = 0; y < itemnodesyx.length; y++) {
            for(int x = 0; x < itemnodesyx[x].length; x++) {
                TextView tv_classname = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_classname);
                TextView tv_sala = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_sala);
                tv_classname.setText("");
                tv_sala.setText("");
            }
        }
        for (int y = 0; y < classnodesyx.length; y++) {
            for (int x = 0; x < classnodesyx[y].length; x++) {
                classnodesyx[y][x] = null;
            }
        }
        aulasfreq = db.readAulasFrequentadas(aluno);
        populateScheduleClassesArray();
        populateScheduleClasses();
    }
    /*
    private void redraw() {
        for (int y = 0; y < classnodesyx.length; y++) {
            for (int x = 0; x < classnodesyx[y].length; x++) {
                if (classnodesyx[y][x] != null) {
                    final Aula aula = classnodesyx[y][x];
                    TextView tv_classname = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_classname);
                    TextView tv_sala = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_sala);

                    tv_classname.setText("");
                    tv_sala.setText("");
                }
            }
        }
        aulasfreq = db.readAulasFrequentadas(aluno);
        populateScheduleClassesArray();
        populateScheduleClasses();
    }
*/

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if(resultCode == Activity.RESULT_OK) {
                    redraw();
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_class, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_class_edit:
                editSchedule();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
