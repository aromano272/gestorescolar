package com.example.aromano.adm_proj_gestorescolar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aRomano on 01/06/2016.
 */
public class AddEventDialogFragment extends DialogFragment implements AddCadeiraDialogFragment.AddCadeiraDialogListener {
    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Cadeira> cadeiras;
    ArrayList<String> nomecadeiras = new ArrayList<>();
    private String[] tipos = {"Exame", "Trabalho"};

    TextView tv_invisibleError;

    public interface AddEventDialogListener {
        void onFinishAddEventDialog(Evento evento);
    }

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
            .setTitle("Adicionar Evento")
            .setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Do nothing here because we override this button later to change the close behaviour.
                            //However, we still need this because on older versions of Android unless we
                            //pass a handler the button doesn't get instantiated
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

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_addevent, null);

        tv_invisibleError = (TextView) view.findViewById(R.id.tv_invisibleError);

        db = DBHelper.getInstance(getActivity());

        cadeiras = db.readCadeiras();

        if(cadeiras == null) {
            cadeiras = new ArrayList<>();
        }


        for (Cadeira cadeira : cadeiras) {
            nomecadeiras.add(cadeira.getName());
        }

        ArrayAdapter<String> cadeiraSpinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, nomecadeiras);
        cadeiraSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        Spinner sp_cadeiras = (Spinner) view.findViewById(R.id.sp_cadeiras);
        sp_cadeiras.setAdapter(cadeiraSpinnerAdapter);

        ArrayAdapter<String> tiposSpinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, tipos);
        tiposSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        Spinner sp_tipos = (Spinner) view.findViewById(R.id.sp_tipos);
        sp_tipos.setAdapter(tiposSpinnerAdapter);


        ImageView iv_addcadeira = (ImageView) view.findViewById(R.id.iv_addcadeira);
        iv_addcadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCadeiraDialogFragment fragment = AddCadeiraDialogFragment.newInstance(aluno);
                fragment.setTargetFragment(AddEventDialogFragment.this, 300);
                fragment.show(getActivity().getSupportFragmentManager(), "fragment_add_cadeira");
            }
        });

        dialog.setView(view);
        return dialog.create();
    }

    // we have to use this method override dialogbutton default functionality which dismisses dialog on click
    // regardless of wether if was succefull or not
    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog != null) {
            final EditText et_nome = (EditText) dialog.findViewById(R.id.et_nome);
            final EditText et_abbr = (EditText) dialog.findViewById(R.id.et_abbr);
            final EditText et_creditos = (EditText) dialog.findViewById(R.id.et_creditos);
            final Spinner sp_cadeiras = (Spinner) dialog.findViewById(R.id.sp_cadeiras);


            Button btn_positive = dialog.getButton(Dialog.BUTTON_POSITIVE);
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cadeira cadeira;
                    if (sp_cadeiras.getSelectedItemId() != AdapterView.INVALID_ROW_ID) {
                        cadeira = cadeiras.get((int)sp_cadeiras.getSelectedItemId());
                    } else {
                        tv_invisibleError.setError("Por favor adiciona uma cadeira");
                    }
                }
            });
        }
    }

    @Override
    public void onFinishAddCadeiraDialog(Cadeira cadeira) {
        db.createCadeiras(cadeira);
        cadeiras.add(cadeira);
        nomecadeiras.add(cadeira.getName());
        tv_invisibleError.setError(null);
        Log.d("debug", "cadeiras length: " + cadeiras.size());
    }

    public void sendBackResult(Evento evento) {
        AddEventDialogListener listener = (AddEventDialogListener) getTargetFragment();
        listener.onFinishAddEventDialog(evento);
        dismiss();
    }


}
