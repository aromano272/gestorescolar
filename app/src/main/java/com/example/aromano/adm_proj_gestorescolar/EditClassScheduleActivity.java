package com.example.aromano.adm_proj_gestorescolar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EditClassScheduleActivity extends AppCompatActivity implements
        AddClassDialogFragment.AddClassDialogListener, EditClassDialogFragment.EditClassDialogListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classschedule);

        aulasfreq = getIntent().getParcelableArrayListExtra("aulasfreq");
        aluno = getIntent().getParcelableExtra("aluno");

        db = DBHelper.getInstance(this);
        sdf = new SimpleDateFormat("HH:mm");

        sv_timeinfo = (ScrollView) findViewById(R.id.sv_timeinfo);
        sv_weeksinfo = (HorizontalScrollView) findViewById(R.id.sv_weekdayinfo);
        LinearLayout layout_timeinfo = (LinearLayout) findViewById(R.id.layout_timeinfo);
        LinearLayout layout_weekdayinfo = (LinearLayout) findViewById(R.id.layout_weekdayinfo);
        TableLayout table_schedule = (TableLayout) findViewById(R.id.table_schedule);
        populateScheduleScaffold(layout_timeinfo, layout_weekdayinfo, table_schedule);

        if(getIntent().getParcelableArrayListExtra("aulasfreq") != null) {
            populateScheduleClassesArray();
            populateScheduleClasses();
        }
    }

    private void populateScheduleScaffold(LinearLayout layout_timeinfo, LinearLayout layout_weekdayinfo, TableLayout table_schedule) {
        // TODO get the earliest class on the week and the latest, and populate layout_timeinfo according to min and max times
        minStartTime = 8;
        maxStartTime = 23;

        weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};



        // TODO make it possible to set minutes
        for(int i = minStartTime; i <= maxStartTime; i++) {
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

        itemnodesyx = new FrameLayout[maxStartTime - minStartTime + 1][weekdays.length];
        classnodesyx = new Aula[maxStartTime - minStartTime + 1][weekdays.length];

        for(int y = 0; y < itemnodesyx.length; y++) {
            TableRow tr_node = new TableRow(this);

            for(int x = 0; x < itemnodesyx[x].length; x++) {
                FrameLayout layout_itemnode = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.class_schedule_itemnode, null);

                // TODO maybe use the TextView reference to skip the findviewbyid afterwards? meh
                Log.d("debug", String.valueOf(x));
                itemnodesyx[y][x] = layout_itemnode;
                tr_node.addView(layout_itemnode);

                final int starttime = minStartTime + y;
                final int diasemana = x;

                layout_itemnode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddClassDialogFragment fragment = AddClassDialogFragment.newInstance(aluno, starttime, diasemana);
                        fragment.show(getSupportFragmentManager(), "fragment_add_class");
                    }
                });

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
        for(int y = 0; y < classnodesyx.length; y++) {
            for(int x = 0; x < classnodesyx[y].length; x++) {
                if(classnodesyx[y][x] != null) {
                    final Aula aula = classnodesyx[y][x];
                    TextView tv_classname = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_classname);
                    TextView tv_sala = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_sala);

                    tv_classname.setText(aula.getCadeira().getAbbr());
                    tv_sala.setText(aula.getSala());

                    itemnodesyx[y][x].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditClassDialogFragment fragment = EditClassDialogFragment.newInstance(aluno, aula);
                            fragment.show(getSupportFragmentManager(), "fragment_edit_class");
                        }
                    });
                }
            }
        }
    }

    private void redraw() {
        for (int y = 0; y < classnodesyx.length; y++) {
            for (int x = 0; x < classnodesyx[y].length; x++) {
                if (classnodesyx[y][x] != null) {
                    final Aula aula = classnodesyx[y][x];
                    TextView tv_classname = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_classname);
                    TextView tv_sala = (TextView) itemnodesyx[y][x].findViewById(R.id.tv_sala);

                    tv_classname.setText("");
                    tv_sala.setText("");

                    itemnodesyx[y][x].setOnClickListener(null);
                }
            }
        }
        aulasfreq = db.readAulasFrequentadas(aluno);
        populateScheduleClassesArray();
        populateScheduleClasses();
    }

    @Override
    public void onFinishAddClassDialog(final Aula aula) {
        Log.d("debug", "onFinishAddClassDialog");
        aula.setIdaula(db.createAulas(aula));
        db.createAulasFrequentadas(aluno, aula);
        int nodex = aula.getDiaSemana();
        int nodey = aula.getHoraentrada() - minStartTime;
        classnodesyx[nodey][nodex] = aula;
        TextView tv_classname = (TextView) itemnodesyx[nodey][nodex].findViewById(R.id.tv_classname);
        TextView tv_sala = (TextView) itemnodesyx[nodey][nodex].findViewById(R.id.tv_sala);

        tv_classname.setText(aula.getCadeira().getAbbr());
        tv_sala.setText(aula.getSala());

        itemnodesyx[nodey][nodex].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditClassDialogFragment fragment = EditClassDialogFragment.newInstance(aluno, aula);
                fragment.show(getSupportFragmentManager(), "fragment_edit_class");
            }
        });
    }

    @Override
    public void onFinishEditClassDialog(final Aula aula, Aula oldaula) {
        Log.d("debug", "onFinishEditClassDialog");
        db.updateAulas(aula);
        Log.d("debug", "BLABLA " + aula.getIdaula());
        Log.d("debug", "BLABLA " + aula.getDiaSemana());
        Log.d("debug", "BLABLA " + aula.getHoraentrada());

        int oldnodex = oldaula.getDiaSemana();
        int oldnodey = oldaula.getHoraentrada() - minStartTime;
        classnodesyx[oldnodey][oldnodex] = null;
        TextView tv_oldclassname = (TextView) itemnodesyx[oldnodey][oldnodex].findViewById(R.id.tv_classname);
        TextView tv_oldsala = (TextView) itemnodesyx[oldnodey][oldnodex].findViewById(R.id.tv_sala);

        tv_oldclassname.setText("");
        tv_oldsala.setText("");

        itemnodesyx[oldnodey][oldnodex].setOnClickListener(null);

        int nodex = aula.getDiaSemana();
        int nodey = aula.getHoraentrada() - minStartTime;
        classnodesyx[nodey][nodex] = aula;
        TextView tv_classname = (TextView) itemnodesyx[nodey][nodex].findViewById(R.id.tv_classname);
        TextView tv_sala = (TextView) itemnodesyx[nodey][nodex].findViewById(R.id.tv_sala);

        tv_classname.setText(aula.getCadeira().getAbbr());
        tv_sala.setText(aula.getSala());

        itemnodesyx[nodey][nodex].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditClassDialogFragment fragment = EditClassDialogFragment.newInstance(aluno, aula);
                fragment.show(getSupportFragmentManager(), "fragment_edit_class");
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_editclass, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_class_edit:
                setResult(Activity.RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


/*
public class EditClassScheduleActivity extends AppCompatActivity implements AddClassDialogFragment.AddClassDialogListener {
    static ScrollView sv_timeinfo;
    static HorizontalScrollView sv_weeksinfo;
    static boolean isActive = false;

    private DBHelper db;
    private ArrayList<Aula> aulasfreq;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classschedule);

        aulasfreq = getIntent().getParcelableArrayListExtra("aulasfreq");
        aluno = getIntent().getParcelableExtra("aluno");

        db = DBHelper.getInstance(this);

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
        int minStartTime = 8;
        int maxStartTime = 23;

        String[] weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};



        // TODO make it possible to set minutes
        for(int i = minStartTime; i <= maxStartTime; i++) {
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

        FrameLayout[][] itemnodesyx = new FrameLayout[maxStartTime - minStartTime + 1][weekdays.length];
        final int[] itemnodesyStarttime = new int[maxStartTime - minStartTime + 1];

        for(int y = 0; y < itemnodesyx.length; y++) {
            TableRow tr_node = new TableRow(this);

            for(int x = 0; x < itemnodesyx[x].length; x++) {
                FrameLayout layout_itemnode = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.class_schedule_itemnode, null);
                final TextView tv_itemnode = (TextView) layout_itemnode.findViewById(R.id.tv_itemnode);

                // TODO maybe use the TextView reference to skip the findviewbyid afterwards? meh
                Log.d("debug", String.valueOf(x));
                itemnodesyx[y][x] = layout_itemnode;
                tr_node.addView(layout_itemnode);
                //itemnodesyStarttime[y] = minStartTime + y;
                final int starttime[] = new int[] { minStartTime + y, 0 };
                final int endtime[] = new int[] { minStartTime + y + 1, 0 };
                final int diasemana = x;

                layout_itemnode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Call add new dialog
                        AddClassDialogFragment fragment = AddClassDialogFragment.newInstance(aluno, starttime, endtime, diasemana);
                        fragment.show(getSupportFragmentManager(), "fragment_time_picker");
                    }
                });

            }
            table_schedule.addView(tr_node);
        }

    }

    public void populateScheduleClasses() {

    }

    @Override
    public void onFinishAddClassDialog(Aula aula) {
        db.createAulas(aula);
        db.createAulasFrequentadas(aluno, aula);

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
*/