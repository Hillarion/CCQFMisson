package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomEventAdapter;
import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonAgenda extends AppCompatActivity {
    private ListView listViewEvents;
    private ArrayList<Event> events;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_agenda);

        listViewEvents = (ListView) findViewById(R.id.listViewEvents);


    }

    public void setListViewEvents() {
        CustomEventAdapter adapter = new CustomEventAdapter(this, dummyList());
        listViewEvents.setAdapter(adapter);
    }
    public ArrayList<Event> dummyList(){
        ArrayList<Event> n = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date d = Calendar.getInstance().getTime();
        String date = df.format(d);

        for(int i = 0 ; i < 10 ; i++ ){
            n.add(new Event("Title", date));
        }
        return n;
    }
}
