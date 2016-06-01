package com.example.aromano.adm_proj_gestorescolar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by aRomano on 01/06/2016.
 */
public class AddEventDialogFragment extends DialogFragment {
    private Aluno aluno;
    private DBHelper db;

    public static AddEventDialogFragment newInstance(Aluno aluno) {
        AddEventDialogFragment fragment = new AddEventDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        fragment.setArguments(args);
        return fragment;
    }

    public AddEventDialogFragment() {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Cadeira> cadeiras = db.readCadeiras();
        //ArrayList<String> nomecadeiras = new ArrayList<>();
        String[] nomecadeiras = new String[cadeiras.size() + 1];
        for(int i = 0; i < cadeiras.size(); i++) {
            nomecadeiras[i] = cadeiras.get(i).getName();
        }
        nomecadeiras[cadeiras.size()] = "Adicionar nova";

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, nomecadeiras);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        Spinner sp_cadeiras = (Spinner) view.findViewById(R.id.sp_cadeiras);
        sp_cadeiras.setAdapter(spinnerArrayAdapter);
    }

}
