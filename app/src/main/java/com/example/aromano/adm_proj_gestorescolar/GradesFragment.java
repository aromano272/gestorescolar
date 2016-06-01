package com.example.aromano.adm_proj_gestorescolar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class GradesFragment extends Fragment {

    private Aluno aluno;
    private DBHelper db;

    public static GradesFragment newInstance(Aluno aluno) {
        GradesFragment fragment = new GradesFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        fragment.setArguments(args);
        return fragment;
    }

    public GradesFragment() {
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
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_exam_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
