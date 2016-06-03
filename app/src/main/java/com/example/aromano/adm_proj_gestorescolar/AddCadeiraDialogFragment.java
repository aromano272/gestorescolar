package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 02/06/2016.
 */
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

/**
 * Created by aRomano on 01/06/2016.
 */
public class AddCadeiraDialogFragment extends DialogFragment {
    private Aluno aluno;
    private DBHelper db;

    public interface AddCadeiraDialogListener {
        void onFinishAddCadeiraDialog(Cadeira cadeira);
    }

    public static AddCadeiraDialogFragment newInstance(Aluno aluno) {
        AddCadeiraDialogFragment fragment = new AddCadeiraDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCadeiraDialogFragment() {
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_addcadeira, null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Adicionar Cadeira")
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

        db = DBHelper.getInstance(getActivity());


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

            Button btn_positive = dialog.getButton(Dialog.BUTTON_POSITIVE);
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO clean this mess up
                    String nome = et_nome.getText().toString().substring(0, 1).toUpperCase() + et_nome.getText().toString().substring(1).toLowerCase();;
                    String abbr = et_abbr.getText().toString().toUpperCase();
                    int creditos = 0;
                    try {
                        creditos = Integer.parseInt(et_creditos.getText().toString());
                    } catch (Exception e) {
                        Log.d("debug", "AddCadeiraDialogFragment creditos null");
                    }

                    if(nome.length() < 1) {
                        et_nome.setError("Por favor preencha este campo");
                        et_nome.requestFocus();
                    } else if(abbr.length() < 1) {
                        et_abbr.setError("Por favor preencha este campo");
                        et_abbr.requestFocus();
                    } else if(abbr.length() > 4) {
                        et_abbr.setError("A abreviatura nao pode conter mais que 4 caracteres");
                        et_abbr.requestFocus();
                    } else if(creditos <= 0) {
                        et_creditos.setError("Insira um numero maior que 0");
                        et_creditos.requestFocus();
                    } else if(db.readCadeiras(abbr) != null) {
                        et_abbr.setError("Cadeira já existe");
                        et_abbr.requestFocus();
                    } else {
                        Log.d("debug", "addcadeiradialogfragment on sendbackresult");
                        Cadeira cadeira = new Cadeira(nome, abbr, creditos);
                        sendBackResult(cadeira);
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    public void sendBackResult(Cadeira cadeira) {
        AddCadeiraDialogListener listener = (AddCadeiraDialogListener) getTargetFragment();
        listener.onFinishAddCadeiraDialog(cadeira);
        dismiss();
    }

}

