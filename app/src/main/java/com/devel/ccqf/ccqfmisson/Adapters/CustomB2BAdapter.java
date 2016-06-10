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

public class CustomB2BAdapter extends BaseAdapter {
    private ArrayList<Event> events;
    private LayoutInflater eventsInfo;

    public CustomB2BAdapter(Context c, ArrayList<Event>e){
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
                (R.layout.mon_agenda_row_layout, parent, false);

        TextView title = (TextView)eventLayout.findViewById(R.id.txtListEventTitle);
        TextView time = (TextView)eventLayout.findViewById(R.id.txtListEventTime);
        TextView details = (TextView)eventLayout.findViewById(R.id.txtListEventDetails);
        TextView adress = (TextView)eventLayout.findViewById(R.id.txtListEventAdress);

        Event currentEvent = events.get(position);

        //Utilisation d'un constructeur custom dans Event
        title.setText(currentEvent.getNom());
        time.setText(currentEvent.getDTStart());
        details.setText(currentEvent.getDTEnd());
        adress.setText(currentEvent.getCompagnie());

        eventLayout.setTag(position);
        return eventLayout;

    }


}
