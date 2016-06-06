package com.example.aromano.adm_proj_gestorescolar;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class GradesFragment extends Fragment implements
        AddGradeDialogFragment.AddGradeDialogListener {

    private Aluno aluno;
    private DBHelper db;
    private ArrayList<Nota> notas;
    BarChart chart_grades;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = DBHelper.getInstance(getActivity());
        notas = db.readNotas(aluno);
        chart_grades = (BarChart) view.findViewById(R.id.chart_grades);
        chart_grades.setNoDataText("Nenhuma nota encontrada, adiciona uma primeiro.");
        chart_grades.setDoubleTapToZoomEnabled(false);


        if(notas != null) {
            chart_grades.setData(generateData());
            chart_grades.setVisibleXRangeMaximum(10f);
            chart_grades.setVisibleYRangeMaximum(20f, YAxis.AxisDependency.LEFT);
            chart_grades.moveViewToY(0, YAxis.AxisDependency.LEFT);
            chart_grades.invalidate();
        } else {
            notas = new ArrayList<>();
        }
    }

    private BarData generateData() {
        ArrayList<IBarDataSet> cadeiras = new ArrayList<>();
        ArrayList<String> cadeirasx = new ArrayList<>();
        ArrayList<Integer> numeroDeNotasPorCadeira = new ArrayList<>();

        // numero de notas por cadeira
        for(int i = 0; i < notas.size(); i++) {
            Nota nota = notas.get(i);
            if(i == 0) {
                cadeirasx.add(nota.getCadeira().getName());
                numeroDeNotasPorCadeira.add(0,1);
            } else if (cadeirasx.get(i - 1).contentEquals(nota.getCadeira().getName())) {
                cadeirasx.add(nota.getCadeira().getName());
                int indexactual = numeroDeNotasPorCadeira.size() - 1;
                int numeroactual = numeroDeNotasPorCadeira.get(indexactual);
                numeroDeNotasPorCadeira.set(indexactual, numeroactual + 1);
            } else {
                cadeirasx.add(nota.getCadeira().getName());
                int indexactual = numeroDeNotasPorCadeira.size() - 1;
                numeroDeNotasPorCadeira.add(indexactual + 1, 1);
            }
        }

        int notasAdicionadas = 0;

        for(int i = 0; i < numeroDeNotasPorCadeira.size(); i++) {
            ArrayList<BarEntry> notasDeCadaCadeira = new ArrayList<>();
            BarDataSet cadeira;

            // por cada nota na cadeira 'i'
            for(int j = 0; j < numeroDeNotasPorCadeira.get(i); j++) {
                Nota nota = notas.get(notasAdicionadas);
                notasDeCadaCadeira.add(new BarEntry(nota.getNota(), notasAdicionadas));
                notasAdicionadas++;
            }
            cadeira = new BarDataSet(notasDeCadaCadeira, cadeirasx.get(notasAdicionadas-1));
            cadeira.setAxisDependency(YAxis.AxisDependency.LEFT);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            cadeira.setColor(color);
            cadeira.setBarSpacePercent(0f);

            cadeiras.add(cadeira);
        }


        BarData data = new BarData(cadeirasx, cadeiras);
        data.setGroupSpace(0f);
        return data;
    }

    private void showAddGradeDialog() {
        AddGradeDialogFragment fragment = AddGradeDialogFragment.newInstance(aluno);
        fragment.setTargetFragment(GradesFragment.this, 300);
        fragment.show(getActivity().getSupportFragmentManager(), "fragment_add_grade");
    }
/*
    private void showEditGradeDialog(Nota nota) {
        EditGradeDialogFragment fragment = EditGradeDialogFragment.newInstance(aluno, nota);
        fragment.setTargetFragment(GradeScheduleFragment.this, 300);
        fragment.show(getActivity().getSupportFragmentManager(), "fragment_edit_grade");
    }
  */


    @Override
    public void onFinishAddGradeDialog(Nota nota) {
        nota.setIdnota(db.createNotas(nota));
        notas.add(nota);
        notas = db.readNotas(aluno);
        chart_grades.setData(generateData());
        chart_grades.setVisibleXRangeMaximum(10f);
        chart_grades.setVisibleYRangeMaximum(20f, YAxis.AxisDependency.LEFT);
        chart_grades.moveViewToY(0, YAxis.AxisDependency.LEFT);
        chart_grades.invalidate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_grades, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_grades_add:
                showAddGradeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
