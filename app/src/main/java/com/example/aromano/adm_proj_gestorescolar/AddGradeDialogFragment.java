package com.example.aromano.adm_proj_gestorescolar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by aRomano on 01/06/2016.
 */
public class AddGradeDialogFragment extends DialogFragment implements AddCadeiraDialogFragment.AddCadeiraDialogListener {
    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Cadeira> cadeiras;
    ArrayList<String> nomecadeiras = new ArrayList<>();
    ArrayAdapter<String> cadeiraSpinnerAdapter;
    Spinner sp_cadeiras;
    TextView tv_invisibleError;
    EditText et_nota;

    public interface AddGradeDialogListener {
        void onFinishAddGradeDialog(Nota nota);
    }

    public static AddGradeDialogFragment newInstance(Aluno aluno) {
        AddGradeDialogFragment fragment = new AddGradeDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        fragment.setArguments(args);
        return fragment;
    }

    public AddGradeDialogFragment() {
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
            .setTitle("Adicionar Nota")
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
        View view = inflater.inflate(R.layout.fragment_addgrade, null);

        tv_invisibleError = (TextView) view.findViewById(R.id.tv_invisibleError);
        et_nota = (EditText) view.findViewById(R.id.et_nota);

        db = DBHelper.getInstance(getActivity());

        cadeiras = db.readCadeiras();

        if(cadeiras == null) {
            cadeiras = new ArrayList<>();
            // in order for the spinner to work properly the first time its instantiated without
            // any data, we have to set a dummy data and then delete it
            cadeiras.add(new Cadeira(-1,"", ""));
        }

        for (Cadeira cadeira : cadeiras) {
            nomecadeiras.add(cadeira.getName());
        }

        cadeiraSpinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, nomecadeiras);
        cadeiraSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        sp_cadeiras = (Spinner) view.findViewById(R.id.sp_cadeiras);
        sp_cadeiras.setAdapter(cadeiraSpinnerAdapter);

        ImageView iv_addcadeira = (ImageView) view.findViewById(R.id.iv_addcadeira);
        iv_addcadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCadeiraDialogFragment fragment = AddCadeiraDialogFragment.newInstance(aluno);
                fragment.setTargetFragment(AddGradeDialogFragment.this, 300);
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
            Button btn_positive = dialog.getButton(Dialog.BUTTON_POSITIVE);
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cadeira cadeira;
                    if (cadeiras.get(0).getIdcadeira() == -1) {
                        tv_invisibleError.setError("Por favor adiciona uma cadeira");
                    } else if (et_nota.getText().toString().length() < 1) {
                        et_nota.setError("Por favor adiciona um valor");
                    } else if (Float.parseFloat(et_nota.getText().toString()) < 0 || Float.parseFloat(et_nota.getText().toString()) > 20) {
                        et_nota.setError("Introduza um valor entre 0 e 20");
                    } else {
                        cadeira = cadeiras.get((int)sp_cadeiras.getSelectedItemId());
                        Nota nota = new Nota(aluno, cadeira, Float.parseFloat(et_nota.getText().toString()));
                        sendBackResult(nota);
                    }
                }
            });
        }
    }

    @Override
    public void onFinishAddCadeiraDialog(Cadeira cadeira) {
        cadeira.setIdcadeira(db.createCadeiras(cadeira));
        db.createMatriculas(aluno, cadeira);
        if(cadeiras.get(0).getIdcadeira() == -1) {
            // dummy item that has to be removed
            Log.d("debug", "dummy cadeira removed");
            AddGradeDialogFragment.this.cadeiras.remove(0);
            AddGradeDialogFragment.this.nomecadeiras.remove(0);
        }
        tv_invisibleError.setError(null);
        AddGradeDialogFragment.this.cadeiras.add(cadeira);
        AddGradeDialogFragment.this.nomecadeiras.add(cadeira.getName());
        cadeiraSpinnerAdapter.notifyDataSetChanged();
        sp_cadeiras.setSelection(cadeiras.size()-1);
    }

    public void sendBackResult(Nota nota) {
        AddGradeDialogListener listener = (AddGradeDialogListener) getTargetFragment();
        listener.onFinishAddGradeDialog(nota);
        dismiss();
    }
}
