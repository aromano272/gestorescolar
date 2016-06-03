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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aRomano on 01/06/2016.
 */
public class AddEventDialogFragment extends DialogFragment implements AddCadeiraDialogFragment.AddCadeiraDialogListener, DateTimePickerFragment.DateTimeDialogListener {
    private Aluno aluno;
    private Evento evento;
    private DBHelper db;
    private ArrayList<Cadeira> cadeiras;
    ArrayList<String> nomecadeiras = new ArrayList<>();
    private String[] tipos = {"Exame", "Trabalho"};
    ArrayAdapter<String> cadeiraSpinnerAdapter;
    Calendar calendar;
    Button btn_calendar;
    String datetimeFormatted;
    Spinner sp_cadeiras;

    TextView tv_invisibleError;

    public interface AddEventDialogListener {
        void onFinishAddEventDialog(Evento evento);
    }

    public static AddEventDialogFragment newInstance(Aluno aluno, Evento evento) {
        AddEventDialogFragment fragment = new AddEventDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        args.putParcelable("evento", evento);
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
            evento = getArguments().getParcelable("evento");
            //Log.d("debug", "getArguments evento==null: " + String.valueOf(evento == null));
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



        // if an event is passed in
        if(evento != null) {
            // cadeira,tipo,data,sala,descricao
            sp_cadeiras.setSelection(evento.getCadeira().getIdcadeira() - 1);
            if (evento.getTipo().contentEquals(tipos[0])) {
                sp_tipos.setSelection(0);
            } else {
                sp_tipos.setSelection(1);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date data = sdf.parse(evento.getDatahora());
                btn_calendar.setText(evento.getDatahora());
                calendar = Calendar.getInstance();
                calendar.setTime(data);
            } catch (Exception e) {
                Log.d("debug", "addeventdialogfragment on evento != null BAK BKA BKAS");
            }
            EditText et_sala = (EditText) view.findViewById(R.id.et_sala);
            et_sala.setText(evento.getSala());
            EditText et_descricao = (EditText) view.findViewById(R.id.et_descricao);
            et_descricao.setText(evento.getDescricao());
        }

        btn_calendar = (Button) view.findViewById(R.id.btn_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calendar != null) {
                    int[] datetime = new int[5];
                    datetime[0] = calendar.get(Calendar.YEAR);
                    datetime[1] = calendar.get(Calendar.MONTH);
                    datetime[2] = calendar.get(Calendar.DAY_OF_MONTH);
                    datetime[3] = calendar.get(Calendar.HOUR_OF_DAY);
                    datetime[4] = calendar.get(Calendar.MINUTE);
                    DateTimePickerFragment fragment = DateTimePickerFragment.newInstance(datetime);
                    fragment.setTargetFragment(AddEventDialogFragment.this, 301);
                    fragment.show(getActivity().getSupportFragmentManager(), "fragment_datetime_edit_picker");
                } else {
                    DateTimePickerFragment fragment = new DateTimePickerFragment();
                    fragment.setTargetFragment(AddEventDialogFragment.this, 301);
                    fragment.show(getActivity().getSupportFragmentManager(), "fragment_datetime_picker");
                }
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
            final Spinner sp_tipos = (Spinner) dialog.findViewById(R.id.sp_tipos);
            final EditText et_sala = (EditText) dialog.findViewById(R.id.et_sala);
            final EditText et_descricao = (EditText) dialog.findViewById(R.id.et_descricao);


            Button btn_positive = dialog.getButton(Dialog.BUTTON_POSITIVE);
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cadeira cadeira;
                    if (sp_cadeiras.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
                        tv_invisibleError.setError("Por favor adiciona uma cadeira");
                    } else if(calendar == null) {
                        btn_calendar.setError("Escolha uma data");
                    } else if(evento != null) {
                        cadeira = cadeiras.get((int)sp_cadeiras.getSelectedItemId());
                        int idevento = evento.getIdevento();
                        evento = new Evento(idevento, cadeira, sp_tipos.getSelectedItem().toString(), datetimeFormatted, et_descricao.getText().toString(), et_sala.getText().toString());
                        sendBackResult(evento);
                    } else {
                        cadeira = cadeiras.get((int)sp_cadeiras.getSelectedItemId());
                        Evento evento = new Evento(cadeira, sp_tipos.getSelectedItem().toString(), datetimeFormatted, et_descricao.getText().toString(), et_sala.getText().toString());
                        sendBackResult(evento);
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
            // dummy item thta has to be removed
            Log.d("debug", "dummy cadeira removed");
            AddEventDialogFragment.this.cadeiras.remove(0);
            AddEventDialogFragment.this.nomecadeiras.remove(0);
        }
        tv_invisibleError.setError(null);
        AddEventDialogFragment.this.cadeiras.add(cadeira);
        AddEventDialogFragment.this.nomecadeiras.add(cadeira.getName());
        cadeiraSpinnerAdapter.notifyDataSetChanged();
        sp_cadeiras.setSelection(cadeiras.size()-1);
    }

    @Override
    public void onFinishDateTimeDialog(Calendar calendar) {
        this.calendar = calendar;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        datetimeFormatted = sdf.format(calendar.getTime());
        btn_calendar.setText(datetimeFormatted);
    }

    public void sendBackResult(Evento evento) {
        AddEventDialogListener listener = (AddEventDialogListener) getTargetFragment();
        listener.onFinishAddEventDialog(evento);
        dismiss();
    }
}
