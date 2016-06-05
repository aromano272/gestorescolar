package com.example.aromano.adm_proj_gestorescolar;


import android.content.DialogInterface;
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

public class EventScheduleFragment extends Fragment implements AddEventDialogFragment.AddEventDialogListener, EditEventDialogFragment.EditEventDialogListener, EventScheduleAdapter.EventScheduleAdapterListener {
    // TODO maybe implement users calendar integration https://developer.android.com/guide/topics/providers/calendar-provider.html
    // TODO https://guides.codepath.com/android/Interacting-with-the-Calendar

    // TODO change exames and trabalhos tables and classes to just Eventos, or try to get them to work with Eventos super class

    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Evento> eventos = new ArrayList<>();
    ListView lv_exams;
    EventScheduleAdapter eventScheduleAdapter;

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
        if(db.readEventos(aluno) != null) {
            eventos = db.readEventos(aluno);
        }

        lv_exams = (ListView) view.findViewById(R.id.lv_exams);
        eventScheduleAdapter = new EventScheduleAdapter(getActivity(), EventScheduleFragment.this, eventos);
        lv_exams.setAdapter(eventScheduleAdapter);
    }

    private void showAddEventDialog() {
        AddEventDialogFragment fragment = AddEventDialogFragment.newInstance(aluno);
        fragment.setTargetFragment(EventScheduleFragment.this, 300);
        fragment.show(getActivity().getSupportFragmentManager(), "fragment_add_event");
    }

    private void showEditEventDialog(Evento evento) {
        EditEventDialogFragment fragment = EditEventDialogFragment.newInstance(aluno, evento);
        fragment.setTargetFragment(EventScheduleFragment.this, 300);
        fragment.show(getActivity().getSupportFragmentManager(), "fragment_edit_event");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_event_add:
                showAddEventDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFinishAddEventDialog(Evento evento) {
        evento.setIdevento(db.createEventos(evento));
        EventScheduleFragment.this.eventos.add(evento);
        eventScheduleAdapter.notifyDataSetChanged();
        Log.d("debug", "onFinishEditEventDialog new" + evento.getDatahora());
    }

    @Override
    public void onFinishEditEventDialog(Evento evento) {
        db.updateEventos(evento);
        EventScheduleFragment.this.eventos.remove(evento.getIdevento()-1);
        EventScheduleFragment.this.eventos.add(evento.getIdevento()-1, evento);
        eventScheduleAdapter.notifyDataSetChanged();
        Log.d("debug", "onFinishEditEventDialog edit" + evento.getDatahora());
    }

    @Override
    public void onMenuEditClicked(int position) {
        Evento evento = eventos.get(position);
        showEditEventDialog(evento);
    }

    @Override
    public void onMenuDeleteClicked(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Apagar Evento?")
                .setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.deleteEventos(position);
                                EventScheduleFragment.this.eventos.remove(position);
                                eventScheduleAdapter.notifyDataSetChanged();
                            }
                        }
                )
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        dialog.show();
    }

    @Override
    public void onEventClicked(int position) {
        Log.d("debug", "onItemClicked");
    }
}
