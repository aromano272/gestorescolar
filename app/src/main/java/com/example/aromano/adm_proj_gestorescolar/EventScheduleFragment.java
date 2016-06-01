package com.example.aromano.adm_proj_gestorescolar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class EventScheduleFragment extends Fragment {
    // TODO maybe implement users calendar integration https://developer.android.com/guide/topics/providers/calendar-provider.html
    // TODO https://guides.codepath.com/android/Interacting-with-the-Calendar

    // TODO change exames and trabalhos tables and classes to just Eventos, or try to get them to work with Eventos super class

    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Evento> eventos;
    ListView lv_exams;

    public static EventScheduleFragment newInstance(Aluno aluno) {
        EventScheduleFragment fragment = new EventScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        fragment.setArguments(args);
        return fragment;
    }


    public EventScheduleFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = DBHelper.getInstance(getActivity());
        eventos = db.readEventos(aluno);

        //TODO debug
        ArrayList<Evento> eventos = new ArrayList<>();
        Cadeira c1 = new Cadeira("Matemática", "MAT");
        Cadeira c2 = new Cadeira("Bases de dados", "BD");
        Evento e1 = new Evento(0, c1, "Trabalho", "16/01/2016", "", null);
        Evento e2 = new Evento(1, c2, "Exame", "26/02/2016", "teste bla", "A5");
        Evento e3 = new Evento(2, c2, "Trabalho", "03/03/2016", "teste bla1312", null);
        eventos.add(e1);
        eventos.add(e2);
        eventos.add(e3);

        lv_exams = (ListView) view.findViewById(R.id.lv_exams);
        lv_exams.setAdapter(new EventScheduleAdapter(getActivity(), eventos));
    }

    private void createEvent() {
        // TODO make popup to add

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
                createEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
