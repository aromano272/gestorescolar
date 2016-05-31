package com.example.aromano.adm_proj_gestorescolar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by aRomano on 31/05/2016.
 */
public class ExamScheduleAdapter extends ArrayAdapter<Exame> {
    public ExamScheduleAdapter(Context context, ArrayList<Exame> exames) {
        super(context, 0, exames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Exame exame = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout., parent, false);
        }

    }

}
