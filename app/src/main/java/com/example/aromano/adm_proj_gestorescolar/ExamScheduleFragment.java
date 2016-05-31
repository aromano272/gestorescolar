package com.example.aromano.adm_proj_gestorescolar;


import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;

public class ExamScheduleFragment extends Fragment {
    // TODO maybe implement users calendar integration https://developer.android.com/guide/topics/providers/calendar-provider.html
    // TODO https://guides.codepath.com/android/Interacting-with-the-Calendar

    // TODO change exames and trabalhos tables and classes to just Eventos, or try to get them to work with Eventos super class

    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Evento> eventos;
    ListView lv_exams;

    public static ExamScheduleFragment newInstance(Aluno aluno) {
        ExamScheduleFragment fragment = new ExamScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        fragment.setArguments(args);
        return fragment;
    }


    public ExamScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_schedule, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = DBHelper.getInstance(getActivity());
        eventos = db.readExames(aluno.getIdaluno());

        Evento asfkl = new Evento(new Cadeira("", ""), "", "");
        ((Trabalho) asfkl).getIdtrabalho();

        lv_exams = (ListView) view.findViewById(R.id.lv_exams);
        lv_exams.setAdapter(new ExamScheduleAdapter(getActivity(), eventos));

    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_exam_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exam_add:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
