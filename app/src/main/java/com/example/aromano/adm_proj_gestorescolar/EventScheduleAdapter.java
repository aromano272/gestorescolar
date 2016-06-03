package com.example.aromano.adm_proj_gestorescolar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aRomano on 31/05/2016.
 */
public class EventScheduleAdapter extends ArrayAdapter<Evento> {
    private Context context;
    private Fragment targetFragment;

    public interface EventScheduleAdapterListener {
        void onMenuEditClicked(int position);
        void onMenuDeleteClicked(int position);
        void onEventClicked(int position);
    }

    public EventScheduleAdapter(Context context, Fragment targetFragment, ArrayList<Evento> eventos) {
        super(context, 0, eventos);
        this.context = context;
        this.targetFragment = targetFragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Evento evento = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_schedule_item, parent, false);
        }

        TextView tv_tipoevento = (TextView) convertView.findViewById(R.id.tv_tipoevento);
        TextView tv_cadeira = (TextView) convertView.findViewById(R.id.tv_cadeira);
        TextView tv_datahora = (TextView) convertView.findViewById(R.id.tv_datahora);
        TextView tv_sala = (TextView) convertView.findViewById(R.id.tv_sala);

        String tipoevento = evento.getTipo().substring(0, 2);
        tv_tipoevento.setText(tipoevento);
        tv_cadeira.setText(evento.getCadeira().getName());
        tv_datahora.setText(evento.getDatahora());
        if(evento.getSala() != null && evento.getSala().length() > 0) {
            tv_sala.setText("Sala: " + evento.getSala());
        } else {
            tv_sala.setText("");
        }



        // if theres no description we just remove the arrow
        // we setVisibility INVISIBLE instead of GONE, because GONE removes from the screen entirely, which could affect the RelativeLayout
        if(evento.getDescricao() != null && evento.getDescricao().length() > 0) {
            convertView.findViewById(R.id.iv_rightarrow).setVisibility(ImageView.VISIBLE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO launch description
                    EventScheduleAdapterListener listener = (EventScheduleAdapterListener) targetFragment;
                    listener.onEventClicked(position);
                }
            });
        } else {
            convertView.findViewById(R.id.iv_rightarrow).setVisibility(ImageView.INVISIBLE);
        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v, position);
                return false;
            }
        });

        return convertView;
    }

    private void onMenuEditClicked(int position) {
        EventScheduleAdapterListener listener = (EventScheduleAdapterListener) targetFragment;
        listener.onMenuEditClicked(position);
    }

    private void onMenuDeleteClicked(int position) {
        EventScheduleAdapterListener listener = (EventScheduleAdapterListener) targetFragment;
        listener.onMenuDeleteClicked(position);
    }


    private void showPopupMenu(View view, final int position) {
        PopupMenu popup = new PopupMenu(this.context, view);
        popup.getMenuInflater().inflate(R.menu.fragment_event_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_event_popup_edit:
                        onMenuEditClicked(position);
                        return true;
                    case R.id.menu_event_popup_delete:
                        onMenuDeleteClicked(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}
