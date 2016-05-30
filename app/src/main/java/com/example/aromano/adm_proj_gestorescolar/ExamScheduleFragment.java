package com.example.aromano.adm_proj_gestorescolar;


import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

public class ExamScheduleFragment extends Fragment {
    // TODO maybe implement users calendar integration https://developer.android.com/guide/topics/providers/calendar-provider.html
    // TODO https://guides.codepath.com/android/Interacting-with-the-Calendar

    private Aluno ALUNO;
    private DBHelper db;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, "Title lul");
                intent.putExtra(CalendarContract.Events.DESCRIPTION, "Description lul");
                startActivity(intent);
            }
        });

        CalendarView calendar = (CalendarView) view.findViewById(R.id.calendar);
        calendar.setShowWeekNumber(false);
    }
}
