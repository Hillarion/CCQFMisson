package com.devel.ccqf.ccqfmisson.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;
import com.devel.ccqf.ccqfmisson.R;


import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jo on 5/16/16.
 */
public class CustomProgramAdapter extends BaseAdapter {
    private ArrayList<Event> events;
    private LayoutInflater eventsInfo;

    public CustomProgramAdapter(Context c, ArrayList<Event>e){
        events = e;
        eventsInfo = LayoutInflater.from(c);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return events.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LinearLayout eventLayout = (LinearLayout)eventsInfo.inflate
                (R.layout.program_row_layout, parent, false);

        TextView timeStart = (TextView)eventLayout.findViewById(R.id.txtProgramHeureDebut);
        TextView timeEnd = (TextView)eventLayout.findViewById(R.id.txtProgramHeureFin);
        TextView title = (TextView)eventLayout.findViewById(R.id.txtProgramEventTitle);

        Event currentEvent = events.get(position);

        timeStart.setText(currentEvent.getDTStart());
        timeEnd.setText(currentEvent.getDTEnd());
        title.setText(currentEvent.getNom());

        eventLayout.setTag(position);
        return eventLayout;

    }


}