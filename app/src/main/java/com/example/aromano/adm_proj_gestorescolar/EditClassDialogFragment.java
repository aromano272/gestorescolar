package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 04/06/2016.
 */

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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class EditClassDialogFragment extends DialogFragment
        implements AddCadeiraDialogFragment.AddCadeiraDialogListener, ClassTimePickerFragment.ClassTimeDialogListener {
    private Aluno aluno;
    private Aula oldaula;
    private DBHelper db;
    private ArrayList<Cadeira> cadeiras;
    ArrayList<String> nomecadeiras = new ArrayList<>();
    private String[] diassemana = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sabado", "Domingo"};
    ArrayAdapter<String> cadeiraSpinnerAdapter;
    Spinner sp_cadeiras;
    Spinner sp_diasemana;
    Button btn_horaentrada;
    EditText et_sala;
    ImageView iv_addcadeira;
    String horaentrada;
    SimpleDateFormat sdf;
    int starttime = -1;

    TextView tv_invisibleError;

    public interface EditClassDialogListener {
        void onFinishEditClassDialog(Aula aula, Aula oldaula);
    }

    public static EditClassDialogFragment newInstance(Aluno aluno, Aula oldaula) {
        EditClassDialogFragment fragment = new EditClassDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        args.putParcelable("oldaula", oldaula);
        fragment.setArguments(args);
        return fragment;
    }

    public EditClassDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null) {
            aluno = getArguments().getParcelable("aluno");
            oldaula = getArguments().getParcelable("oldaula");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Editar Aula")
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
        View view = inflater.inflate(R.layout.fragment_addclass, null);

        sp_cadeiras = (Spinner) view.findViewById(R.id.sp_cadeiras);
        sp_diasemana = (Spinner) view.findViewById(R.id.sp_diasemana);
        btn_horaentrada = (Button) view.findViewById(R.id.btn_horaentrada);
        et_sala = (EditText) view.findViewById(R.id.et_sala);
        iv_addcadeira = (ImageView) view.findViewById(R.id.iv_addcadeira);
        tv_invisibleError = (TextView) view.findViewById(R.id.tv_invisibleError);
        sdf = new SimpleDateFormat("HH:mm");

        db = DBHelper.getInstance(getActivity());
        starttime = oldaula.getHoraentrada();

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
        sp_cadeiras.setAdapter(cadeiraSpinnerAdapter);

        ArrayAdapter<String> diassemanaSpinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, diassemana);
        diassemanaSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        sp_diasemana.setAdapter(diassemanaSpinnerAdapter);

        iv_addcadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCadeiraDialogFragment fragment = AddCadeiraDialogFragment.newInstance(aluno);
                fragment.setTargetFragment(EditClassDialogFragment.this, 300);
                fragment.show(getActivity().getSupportFragmentManager(), "fragment_add_cadeira");
            }
        });

        btn_horaentrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassTimePickerFragment fragment = ClassTimePickerFragment.newInstance(starttime, EditClassDialogFragment.this);
                fragment.show(getActivity().getSupportFragmentManager(), "fragment_starttime_picker");
            }
        });

        if(starttime < 10) {
            horaentrada = "0" + starttime + ":00";
        } else {
            horaentrada = String.valueOf(starttime) + ":00";
        }
        btn_horaentrada.setText(horaentrada);
        sp_diasemana.setSelection(oldaula.getDiaSemana());
        sp_cadeiras.setSelection(oldaula.getCadeira().getIdcadeira());
        et_sala.setText(oldaula.getSala());

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
                    } else if(horaentrada == null) {
                        btn_horaentrada.setError("Escolha uma hora de entrada");
                    } else if(et_sala.length() < 1) {
                        et_sala.setError("Escolha a sala");
                    } else if (!db.checkScheduleAvailability(aluno, new Aula(cadeiras.get((int)sp_cadeiras.getSelectedItemId()), (int) sp_diasemana.getSelectedItemId(), starttime, et_sala.getText().toString())) && !((oldaula.getHoraentrada() == starttime) && (oldaula.getDiaSemana() == (int) sp_diasemana.getSelectedItemId()))) {
                        btn_horaentrada.setError("Horário não disponivel");
                        Toast.makeText(getActivity(), "Horário nao disponivel, escolha outra hora/dia", Toast.LENGTH_LONG).show();
                    } else {
                        cadeira = cadeiras.get((int)sp_cadeiras.getSelectedItemId());
                        Aula aula = new Aula(oldaula.getIdaula(), cadeira, (int)sp_diasemana.getSelectedItemId(), starttime, et_sala.getText().toString().toUpperCase());
                        sendBackResult(aula);
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
            EditClassDialogFragment.this.cadeiras.remove(0);
            EditClassDialogFragment.this.nomecadeiras.remove(0);
        }
        tv_invisibleError.setError(null);
        EditClassDialogFragment.this.cadeiras.add(cadeira);
        EditClassDialogFragment.this.nomecadeiras.add(cadeira.getName());
        cadeiraSpinnerAdapter.notifyDataSetChanged();
        sp_cadeiras.setSelection(cadeiras.size()-1);
    }

    @Override
    public void onFinishClassStartTimeDialog(int starttime, boolean areMinutesSet) {
        if (areMinutesSet) {
            Toast.makeText(getActivity(), "De momento não é possivel definir os minutos, guardámos apenas as horas.", Toast.LENGTH_LONG).show();
        }
        this.starttime = starttime;
        if(starttime < 8) {
            btn_horaentrada.setError("A hora minima de entrada é às 8:00");
            Toast.makeText(getActivity(), "A hora minima de entrada é às 8:00.", Toast.LENGTH_LONG).show();
            horaentrada = null;
        } else if(starttime < 10) {
            horaentrada = "0" + starttime + ":00";
            btn_horaentrada.setText(horaentrada);
        } else {
            horaentrada = String.valueOf(starttime) + ":00";
            btn_horaentrada.setText(horaentrada);
        }
    }

    public void sendBackResult(Aula aula) {
        EditClassDialogListener listener = (EditClassDialogListener) getActivity();
        listener.onFinishEditClassDialog(aula, oldaula);
        dismiss();
    }
}



/*
public class AddClassDialogFragment extends DialogFragment
        implements AddCadeiraDialogFragment.AddCadeiraDialogListener, ClassTimePickerFragment.ClassTimeDialogListener {
    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Cadeira> cadeiras;
    ArrayList<String> nomecadeiras = new ArrayList<>();
    private String[] diassemana = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sabado", "Domingo"};
    ArrayAdapter<String> cadeiraSpinnerAdapter;
    String datetimeFormatted;
    Spinner sp_cadeiras;
    Spinner sp_diasemana;
    Button btn_horaentrada;
    Button btn_horasaida;
    EditText et_sala;
    ImageView iv_addcadeira;
    String horaentrada;
    String horasaida;
    int[] starttime = null;
    int[] endtime = null;
    SimpleDateFormat sdf;
    int diasemana;

    TextView tv_invisibleError;

    public interface AddClassDialogListener {
        void onFinishAddClassDialog(Aula aula);
    }

    public static AddClassDialogFragment newInstance(Aluno aluno, int[] starttime, int[] endtime, int diasemana) {
        AddClassDialogFragment fragment = new AddClassDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("aluno", aluno);
        args.putIntArray("starttime", starttime);
        args.putIntArray("endtime", endtime);
        args.putInt("diasemana", diasemana);
        fragment.setArguments(args);
        return fragment;
    }

    public AddClassDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null) {
            aluno = getArguments().getParcelable("aluno");
            starttime = getArguments().getIntArray("starttime");
            endtime = getArguments().getIntArray("endtime");
            diasemana = getArguments().getInt("diasemana");
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
        View view = inflater.inflate(R.layout.fragment_addclass, null);

        sp_cadeiras = (Spinner) view.findViewById(R.id.sp_cadeiras);
        sp_diasemana = (Spinner) view.findViewById(R.id.sp_diasemana);
        btn_horaentrada = (Button) view.findViewById(R.id.btn_horaentrada);
        btn_horasaida = (Button) view.findViewById(R.id.btn_horasaida);
        et_sala = (EditText) view.findViewById(R.id.et_sala);
        iv_addcadeira = (ImageView) view.findViewById(R.id.iv_addcadeira);
        tv_invisibleError = (TextView) view.findViewById(R.id.tv_invisibleError);
        sdf = new SimpleDateFormat("HH:mm");

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
        sp_cadeiras.setAdapter(cadeiraSpinnerAdapter);

        ArrayAdapter<String> diassemanaSpinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, diassemana);
        diassemanaSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        sp_diasemana.setAdapter(diassemanaSpinnerAdapter);

        iv_addcadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCadeiraDialogFragment fragment = AddCadeiraDialogFragment.newInstance(aluno);
                fragment.setTargetFragment(AddClassDialogFragment.this, 300);
                fragment.show(getActivity().getSupportFragmentManager(), "fragment_add_cadeira");
            }
        });

        btn_horaentrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassTimePickerFragment fragment = ClassTimePickerFragment.newInstance(starttime, endtime, AddClassDialogFragment.this);
                fragment.show(getActivity().getSupportFragmentManager(), "fragment_starttime_picker");
            }
        });

        btn_horasaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassTimePickerFragment fragment = ClassTimePickerFragment.newInstance(null, endtime, AddClassDialogFragment.this);
                fragment.show(getActivity().getSupportFragmentManager(), "fragment_endtime_picker");
            }
        });

        if(starttime[0] < 10) {
            horaentrada = "0" + starttime[0];
        } else {
            horaentrada = String.valueOf(starttime[0]);
        }
        if(starttime[1] < 10) {
            horaentrada += ":0" + starttime[1];
        } else {
            horaentrada += ":" + starttime[1];
        }
        btn_horaentrada.setText(horaentrada);
        if(endtime[0] < 10) {
            horasaida = "0" + endtime[0];
        } else {
            horasaida = String.valueOf(endtime[0]);
        }
        if(endtime[1] < 10) {
            horasaida += ":0" + endtime[1];
        } else {
            horasaida += ":" + endtime[1];
        }
        btn_horasaida.setText(horasaida);

        sp_diasemana.setSelection(diasemana);

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
                    if (sp_cadeiras.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
                        tv_invisibleError.setError("Por favor adiciona uma cadeira");
                    } else if(horaentrada == null) {
                        btn_horaentrada.setError("Escolha uma hora de entrada");
                    } else if(horasaida == null) {
                        btn_horasaida.setError("Escolha uma hora de saida");
                    } else if(et_sala.length() < 1) {
                        et_sala.setError("Escolha a sala");
                    } else {
                        cadeira = cadeiras.get((int)sp_cadeiras.getSelectedItemId());
                        Aula aula = new Aula(cadeira, (int)sp_diasemana.getSelectedItemId(), horaentrada, horasaida, et_sala.getText().toString());
                        sendBackResult(aula);
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
            AddClassDialogFragment.this.cadeiras.remove(0);
            AddClassDialogFragment.this.nomecadeiras.remove(0);
        }
        tv_invisibleError.setError(null);
        AddClassDialogFragment.this.cadeiras.add(cadeira);
        AddClassDialogFragment.this.nomecadeiras.add(cadeira.getName());
        cadeiraSpinnerAdapter.notifyDataSetChanged();
        sp_cadeiras.setSelection(cadeiras.size()-1);
    }

    @Override
    public void onFinishClassStartTimeDialog(int[] starttime) {
        this.starttime = starttime;
        if(starttime[0] < 10) {
            horaentrada = "0" + starttime[0];
        } else {
            horaentrada = String.valueOf(starttime[0]);
        }
        if(starttime[1] < 10) {
            horaentrada += ":0" + starttime[1];
        } else {
            horaentrada += ":" + starttime[1];
        }
        btn_horaentrada.setText(horaentrada);
    }

    @Override
    public void onFinishClassEndTimeDialog(int[] endtime) {
        this.endtime = endtime;
        if(endtime[0] < 10) {
            horasaida = "0" + endtime[0];
        } else {
            horasaida = String.valueOf(endtime[0]);
        }
        if(endtime[1] < 10) {
            horasaida += ":0" + endtime[1];
        } else {
            horasaida += ":" + endtime[1];
        }
        btn_horasaida.setText(horasaida);
    }

    public void sendBackResult(Aula aula) {
        AddClassDialogListener listener = (AddClassDialogListener) getActivity();
        listener.onFinishAddClassDialog(aula);
        dismiss();
    }
}
*/